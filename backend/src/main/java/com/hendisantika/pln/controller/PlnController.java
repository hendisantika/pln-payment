package com.hendisantika.pln.controller;

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

import com.hendisantika.pln.dto.*;
import com.hendisantika.pln.entity.PlnBill;
import com.hendisantika.pln.service.PlnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pln")
@RequiredArgsConstructor
@Slf4j
public class PlnController {

    private final PlnService plnService;

    /**
     * Cek tagihan PLN (inquiry)
     * POST /api/pln/inquiry
     */
    @PostMapping("/inquiry")
    public ResponseEntity<ApiResponse<InquiryResponse>> inquiry(@Valid @RequestBody InquiryRequest request) {
        log.info("Inquiry request for customer: {}", request.getCustomerId());
        InquiryResponse response = plnService.inquiry(request.getCustomerId(), request.getProductCode());
        return ResponseEntity.ok(ApiResponse.ok("Inquiry berhasil", response));
    }

    /**
     * Bayar tagihan PLN
     * POST /api/pln/pay
     */
    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<PaymentResponse>> pay(@Valid @RequestBody PaymentRequest request) {
        log.info("Payment request for bill: {}, customer: {}", request.getBillId(), request.getCustomerId());
        PaymentResponse response = plnService.pay(request.getBillId(), request.getCustomerId());
        return ResponseEntity.ok(ApiResponse.ok("Pembayaran berhasil", response));
    }

    /**
     * Riwayat pembayaran
     * GET /api/pln/history?customerId=xxx
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<PlnBill>>> history(
            @RequestParam(required = false) String customerId) {
        log.info("History request for customer: {}", customerId);
        List<PlnBill> bills = plnService.getHistory(customerId);
        return ResponseEntity.ok(ApiResponse.ok("Riwayat ditemukan", bills));
    }
}
