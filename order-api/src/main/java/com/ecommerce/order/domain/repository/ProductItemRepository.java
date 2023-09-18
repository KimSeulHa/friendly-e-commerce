package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {
}
