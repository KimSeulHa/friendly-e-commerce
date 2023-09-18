package com.ecommerce.order.domain.model;

import com.ecommerce.order.domain.entity.ProductItem;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto {
    private Long id;
    private String name;
    private Integer price;
    private Integer count;

    public static ProductItemDto from(ProductItem item){
        return ProductItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .count(item.getCount())
                .build();
    }
}
