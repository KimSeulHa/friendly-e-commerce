package com.ecommerce.cms.user.config.filter;

import com.ecommerce.cms.user.service.CustomerService;
import com.ecommerce.domain.common.UserVo;
import com.ecommerce.domain.config.JwtAuthenticationProvider;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomerFilter implements Filter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");

        System.out.println("plz ... check ...token"+token);
        if(!jwtAuthenticationProvider.checkValidToken(token)){
            throw new ServletException("Invalid Access!");
        }
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
        customerService.findByIdAndEmail(userVo.getId(),userVo.getEmail())
                .orElseThrow(()->new ServletException("Invalid Access!"));

        //다음으로 전송
        chain.doFilter(request,response);
    }
}
