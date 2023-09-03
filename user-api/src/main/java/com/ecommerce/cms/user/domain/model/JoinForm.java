package com.ecommerce.cms.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinForm {
    private String name;
    private String email;
    private String passwd;
    private String phone;
    private LocalDate birth;
}
