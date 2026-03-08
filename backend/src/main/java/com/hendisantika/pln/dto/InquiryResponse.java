package com.hendisantika.pln.dto;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponse {

    private String customerId;
    private String customerName;
    private String tariff;
    private String power;
    private String period;
    private BigDecimal amount;
    private BigDecimal adminFee;
    private BigDecimal totalAmount;
    private String status;
    private String referenceId;
    private String message;
}
