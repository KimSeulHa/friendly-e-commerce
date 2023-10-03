package com.ecommerce.order.domain.repository;


import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.QProduct;
import com.ecommerce.order.domain.repository.ProductRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchByName(String name) {
        String search = "%"+name+"%";
        QProduct product = QProduct.product;

        return queryFactory.selectFrom(product)
                .where(product.name.like(search))
                .fetch();
    }
}
