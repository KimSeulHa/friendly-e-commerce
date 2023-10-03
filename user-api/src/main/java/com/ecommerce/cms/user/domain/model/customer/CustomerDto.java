package com.ecommerce.cms.user.domain.model.customer;

import com.ecommerce.cms.user.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto {
    private long id;
    private String email;
    private Integer balance;

    public static CustomerDto from(Customer customer){
        return new CustomerDto(customer.getId(), customer.getEmail(),
                customer.getBalance() == null? 0 : customer.getBalance());
    }

}
