package com.ecommerce.cms.user.service.seller;

import com.ecommerce.cms.user.domain.entity.Seller;
import com.ecommerce.cms.user.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(long id, String email){
        return sellerRepository.findByIdAndEmail(id,email);
    }

}
