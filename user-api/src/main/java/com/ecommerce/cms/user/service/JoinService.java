package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final CustomerRepository customerRepository;
    public Customer Join(JoinForm joinForm){
        return customerRepository.save(Customer.form(joinForm));
    }
}
