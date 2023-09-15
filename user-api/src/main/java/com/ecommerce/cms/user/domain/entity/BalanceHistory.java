package com.ecommerce.cms.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class BalanceHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
    private Customer customer;

    private Integer money;

    private Integer balance;

    private String WhereToUse;

    private String description;


}
