package com.ecommerce.order.domain.entity;

import com.ecommerce.order.domain.model.AddProductForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private long sellerId;

    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductItem> productItems = new ArrayList<>();

    public static Product of(Long sellerId, AddProductForm form){
       return Product.builder()
               .sellerId(sellerId)
               .name(form.getName())
               .description(form.getDescription())
               .productItems(form.getProductItemForms().stream()
                       .map(productItemForm -> ProductItem.of(sellerId,productItemForm)).collect(Collectors.toList())
               ).build();
    }
}
