package com.hendisantika.pln.service;

/**
 * Created by IntelliJ IDEA.
 * Project : pln-payment
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 08/03/26
 * Time: 08.38
 * To change this template use File | Settings | File Templates.
 */

import com.hendisantika.pln.dto.InquiryResponse;
import com.hendisantika.pln.dto.PaymentResponse;
import com.hendisantika.pln.entity.PlnBill;
import com.hendisantika.pln.enums.BillStatus;
import com.hendisantika.pln.repository.PlnBillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlnService {

    private final IakApiService iakApiService;
    private final PlnBillRepository plnBillRepository;

    private static final BigDecimal ADMIN_FEE = new BigDecimal("2500");

    /**
     * Inquiry tagihan PLN — cek apakah ada tagihan yang belum dibayar
     */
    @Transactional
    public InquiryResponse inquiry(String customerId, String productCode) {
        log.info("Inquiry PLN untuk customer: {}", customerId);

        // Cek apakah sudah ada tagihan UNPAID di database
        var existingBill = plnBillRepository.findByCustomerIdAndStatus(customerId, BillStatus.UNPAID);
        if (existingBill.isPresent()) {
            PlnBill bill = existingBill.get();
            return buildInquiryResponse(bill, "Tagihan ditemukan (dari cache)");
        }

        // Panggil IAK API untuk inquiry
        try {
            Map<String, Object> iakResponse = iakApiService.inquiryPostpaid(customerId);
            return processInquiryResponse(customerId, productCode, iakResponse);
        } catch (Exception e) {
            log.error("Error calling IAK API, using mock data: {}", e.getMessage());
            return processInquiryMock(customerId, productCode);
        }
    }

    /**
     * Proses response dari IAK API
     */
    private InquiryResponse processInquiryResponse(String customerId, String productCode,
                                                    Map<String, Object> iakResponse) {
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) iakResponse.getOrDefault("data", iakResponse);

        String customerName = String.valueOf(data.getOrDefault("customer_name", "PELANGGAN PLN"));
        String tariff = String.valueOf(data.getOrDefault("segment_power", "R1/900VA"));
        BigDecimal amount = new BigDecimal(String.valueOf(data.getOrDefault("nominal", "150000")));
        String period = String.valueOf(data.getOrDefault("period", "-"));
        String trId = String.valueOf(data.getOrDefault("tr_id", ""));

        BigDecimal totalAmount = amount.add(ADMIN_FEE);

        PlnBill bill = PlnBill.builder()
                .customerId(customerId)
                .customerName(customerName)
                .tariff(tariff)
                .power(extractPower(tariff))
                .period(period)
                .amount(amount)
                .adminFee(ADMIN_FEE)
                .totalAmount(totalAmount)
                .iakTrId(trId)
                .productCode(productCode)
                .referenceId("PLN-" + customerId + "-" + System.currentTimeMillis())
                .status(BillStatus.UNPAID)
                .inquiryAt(LocalDateTime.now())
                .build();

        plnBillRepository.save(bill);

        return buildInquiryResponse(bill, "Tagihan ditemukan");
    }

    /**
     * Mock data untuk development/testing tanpa IAK API
     */
    private InquiryResponse processInquiryMock(String customerId, String productCode) {
        log.info("Using mock data for customer: {}", customerId);

        PlnBill bill = PlnBill.builder()
                .customerId(customerId)
                .customerName("HENDI SANTIKA")
                .tariff("R1")
                .power("900VA")
                .period("202603")
                .amount(new BigDecimal("185000"))
                .adminFee(ADMIN_FEE)
                .totalAmount(new BigDecimal("187500"))
                .productCode(productCode)
                .referenceId("PLN-" + customerId + "-" + System.currentTimeMillis())
                .status(BillStatus.UNPAID)
                .inquiryAt(LocalDateTime.now())
                .build();

        plnBillRepository.save(bill);

        return buildInquiryResponse(bill, "Tagihan ditemukan (mock)");
    }

    /**
     * Bayar tagihan PLN
     */
    @Transactional
    public PaymentResponse pay(Long billId, String customerId) {
        log.info("Payment PLN untuk bill: {}, customer: {}", billId, customerId);

        PlnBill bill = plnBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Tagihan tidak ditemukan"));

        if (!bill.getCustomerId().equals(customerId)) {
            throw new RuntimeException("Nomor pelanggan tidak cocok dengan tagihan");
        }

        if (bill.getStatus() == BillStatus.PAID) {
            return PaymentResponse.builder()
                    .billId(bill.getId())
                    .customerId(bill.getCustomerId())
                    .customerName(bill.getCustomerName())
                    .totalAmount(bill.getTotalAmount())
                    .status("ALREADY_PAID")
                    .referenceId(bill.getReferenceId())
                    .paidAt(bill.getPaidAt())
                    .message("Tagihan sudah dibayar sebelumnya")
                    .build();
        }

        // Panggil IAK API untuk payment
        try {
            if (bill.getIakTrId() != null && !bill.getIakTrId().isEmpty()) {
                iakApiService.payPostpaid(customerId, bill.getIakTrId());
            }
        } catch (Exception e) {
            log.warn("IAK payment API error (proceeding with mock): {}", e.getMessage());
        }

        // Update status jadi PAID
        bill.setStatus(BillStatus.PAID);
        bill.setPaidAt(LocalDateTime.now());
        plnBillRepository.save(bill);

        return PaymentResponse.builder()
                .billId(bill.getId())
                .customerId(bill.getCustomerId())
                .customerName(bill.getCustomerName())
                .totalAmount(bill.getTotalAmount())
                .status("PAID")
                .referenceId(bill.getReferenceId())
                .paidAt(bill.getPaidAt())
                .message("Pembayaran berhasil")
                .build();
    }

    /**
     * Ambil riwayat pembayaran berdasarkan customer ID
     */
    public List<PlnBill> getHistory(String customerId) {
        if (customerId != null && !customerId.isBlank()) {
            return plnBillRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        }
        return plnBillRepository.findAllByOrderByCreatedAtDesc();
    }

    private InquiryResponse buildInquiryResponse(PlnBill bill, String message) {
        return InquiryResponse.builder()
                .customerId(bill.getCustomerId())
                .customerName(bill.getCustomerName())
                .tariff(bill.getTariff())
                .power(bill.getPower())
                .period(bill.getPeriod())
                .amount(bill.getAmount())
                .adminFee(bill.getAdminFee())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus().name())
                .referenceId(bill.getReferenceId())
                .message(message)
                .build();
    }

    private String extractPower(String tariff) {
        if (tariff == null) return "-";
        if (tariff.contains("/")) {
            return tariff.split("/")[1];
        }
        return tariff;
    }
}
