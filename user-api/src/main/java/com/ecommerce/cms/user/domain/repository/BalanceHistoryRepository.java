package com.ecommerce.cms.user.domain.repository;

import com.ecommerce.cms.user.domain.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory,Long> {
    Optional<BalanceHistory> findFirstByCustomer_IdOrderByIdDesc(@RequestParam("customer_id")Long CustomerId);

}
