package com.hendisantika.pln.repository;

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

import com.hendisantika.pln.entity.PlnBill;
import com.hendisantika.pln.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlnBillRepository extends JpaRepository<PlnBill, Long> {

    List<PlnBill> findByCustomerIdOrderByCreatedAtDesc(String customerId);

    Optional<PlnBill> findByCustomerIdAndStatus(String customerId, BillStatus status);

    List<PlnBill> findByStatusOrderByCreatedAtDesc(BillStatus status);

    List<PlnBill> findAllByOrderByCreatedAtDesc();
}
