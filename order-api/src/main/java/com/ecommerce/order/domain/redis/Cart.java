package com.ecommerce.order.domain.redis;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductCartForm;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@RedisHash("cart")
public class Cart {
    @Id
    private Long customerId;
    private List<Product> Products = new ArrayList<>();
    private List<String> msg = new ArrayList<>();

    public void addMsg(String msg){
        this.msg.add(msg);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product{
        private Long id;
        private Long sellerId;
        private String name;
        private String description;
        private List<ProductItem> productItems = new ArrayList<>();

        public static Product from(AddProductCartForm form) {
            return Product.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .sellerId(form.getSellerId())
                    .description(form.getDescription())
                    .productItems(form.getProductItems().stream()
                            .map(productItem -> ProductItem.from(productItem)).collect(Collectors.toList()))
                            //.map(ProductItem::from).collect(Collectors.toList()))
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItem{
        private Long id;
        private String name;
        private Integer count;
        private Integer price;

        public static ProductItem from(AddProductCartForm.ProductItem form){
            return ProductItem.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .price(form.getPrice())
                    .count(form.getCount())
                    .build();
        }
    }
}
