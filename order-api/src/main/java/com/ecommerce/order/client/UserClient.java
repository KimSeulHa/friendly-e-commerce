package com.ecommerce.order.client;

import com.ecommerce.order.client.customer.BalanceDto;
import com.ecommerce.order.client.customer.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-api",url = "${feign.client.url.user-api}")
public interface UserClient {

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN")String token);

    @PostMapping("/balance")
    public ResponseEntity<String> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN")String token,
                                                @RequestBody BalanceDto balanceDto);

}
