package com.ecommerce.cms.user.domain.repository;

import com.ecommerce.cms.user.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{
}
