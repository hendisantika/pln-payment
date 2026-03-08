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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequest {

    @NotBlank(message = "Nomor pelanggan wajib diisi")
    @Pattern(regexp = "^\\d{10,13}$", message = "Nomor pelanggan harus 10-13 digit angka")
    private String customerId;

    @NotBlank(message = "Tipe produk wajib diisi")
    private String productCode; // PLNPOSTPAID atau PLNPREPAID
}
