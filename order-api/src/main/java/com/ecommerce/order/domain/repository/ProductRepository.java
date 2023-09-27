package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.entity.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Primary
public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryCustom {
    @EntityGraph(attributePaths = {"productItems"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findBySellerIdAndId(Long sellerId, Long id);
    @EntityGraph(attributePaths = {"productItems"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findWithProductItemsById(Long id);
}
