package com.ecommerce.cms.user.domain.repository;

import com.ecommerce.cms.user.domain.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);

    Optional<Seller> findByIdAndEmail(Long id, String email);
}
