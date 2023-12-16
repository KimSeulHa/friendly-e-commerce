package com.ecommerce.order.client.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BalanceDto {
    private String description;
    private String from;
    private Integer money;

}
