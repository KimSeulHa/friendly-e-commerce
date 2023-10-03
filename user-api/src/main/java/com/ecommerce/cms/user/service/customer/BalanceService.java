package com.ecommerce.cms.user.service.customer;

import com.ecommerce.cms.user.domain.entity.BalanceHistory;
import com.ecommerce.cms.user.domain.model.customer.BalanceDto;
import com.ecommerce.cms.user.domain.repository.BalanceHistoryRepository;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceHistoryRepository balanceRepository;
    private final CustomerRepository customerRepository;

    /**
     * 적립금 충전 및 사용
     * @param customerId
     * @param balanceDto
     * @return BalanceHistory
     * @throws CustomException
     */
    @Transactional(noRollbackFor = {CustomException.class})
    public BalanceHistory changeBalance(Long customerId, BalanceDto balanceDto) throws CustomException{
        BalanceHistory balanceHistory =
                        balanceRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                                .orElse(BalanceHistory.builder()
                                                .money(0)
                                                .balance(0)
                                                .customer(customerRepository.findById(customerId)
                                                                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER)))
                                                .build());

        int balance = balanceHistory.getBalance() + balanceDto.getMoney();
        if(balance < 0){
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        balanceHistory =  BalanceHistory.builder()
                .money(balanceDto.getMoney())
                .balance(balance)
                .WhereToUse(balanceDto.getFrom())
                .description(balanceDto.getDescription())
                .customer(balanceHistory.getCustomer())
                .build();

        balanceHistory.getCustomer().setBalance(balance);

        balanceRepository.save(balanceHistory);

        return balanceHistory;
    }
}
