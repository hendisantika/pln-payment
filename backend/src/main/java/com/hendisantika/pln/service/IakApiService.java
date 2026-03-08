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

import com.hendisantika.pln.config.IakConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IakApiService {

    private final RestClient iakRestClient;
    private final IakConfig iakConfig;

    /**
     * Cek tagihan PLN Postpaid (inquiry)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> inquiryPostpaid(String customerId) {
        String sign = generateSign(customerId);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("commands", "inq-pasca");
        requestBody.put("username", iakConfig.getUsername());
        requestBody.put("code", "PLNPOSTPAID");
        requestBody.put("hp", customerId);
        requestBody.put("ref_id", generateRefId(customerId));
        requestBody.put("sign", sign);

        log.debug("IAK Inquiry request for customer: {}", customerId);

        return iakRestClient.post()
                .uri("/api/v1/bill/check")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
    }

    /**
     * Bayar tagihan PLN Postpaid
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> payPostpaid(String customerId, String trId) {
        String sign = generateSign(customerId);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("commands", "pay-pasca");
        requestBody.put("username", iakConfig.getUsername());
        requestBody.put("code", "PLNPOSTPAID");
        requestBody.put("hp", customerId);
        requestBody.put("tr_id", trId);
        requestBody.put("sign", sign);

        log.debug("IAK Payment request for customer: {}", customerId);

        return iakRestClient.post()
                .uri("/api/v1/bill/check")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
    }

    /**
     * Generate MD5 sign: md5(username + api_key + ref_id)
     */
    private String generateSign(String refId) {
        String raw = iakConfig.getUsername() + iakConfig.getApiKey() + refId;
        return md5(raw);
    }

    private String generateRefId(String customerId) {
        return "PLN-" + customerId + "-" + System.currentTimeMillis();
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
}
