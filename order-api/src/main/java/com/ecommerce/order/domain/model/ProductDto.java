package com.ecommerce.order.domain.model;

import com.ecommerce.order.domain.entity.Product;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductItemDto> items;

    public static ProductDto from(Product product){
        List<ProductItemDto> productItemDtos = product.getProductItems().stream()
                .map(item -> ProductItemDto.from(item)).collect(Collectors.toList());

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .items(productItemDtos)
                .build();
    }

    public static ProductDto withOutItemsFrom(Product product){

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }
}
