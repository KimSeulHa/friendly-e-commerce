package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Optional<Customer> findByIdAndEmail(long id, String email){
        return customerRepository.findByEmail(email).stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}
