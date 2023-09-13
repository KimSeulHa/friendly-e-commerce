package com.ecommerce.cms.user.domain.model.seller;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.entity.Seller;
import com.ecommerce.cms.user.domain.model.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SellerDto {
    private long id;
    private String email;

    public static SellerDto from(Seller seller){
        return new SellerDto(seller.getId(), seller.getEmail());
    }
}
