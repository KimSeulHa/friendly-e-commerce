package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchByName(String name);

}
