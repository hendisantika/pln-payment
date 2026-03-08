package com.hendisantika.pln.entity;

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

import com.hendisantika.pln.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pln_bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlnBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, length = 20)
    private String customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "tariff")
    private String tariff;

    @Column(name = "power")
    private String power;

    @Column(name = "period")
    private String period;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "admin_fee", precision = 10, scale = 2)
    private BigDecimal adminFee;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "iak_tr_id")
    private String iakTrId;

    @Column(name = "product_code", length = 30)
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BillStatus status;

    @Column(name = "inquiry_at")
    private LocalDateTime inquiryAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
