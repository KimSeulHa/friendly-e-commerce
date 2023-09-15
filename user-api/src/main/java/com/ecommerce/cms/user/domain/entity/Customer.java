package com.ecommerce.cms.user.domain.entity;

import com.ecommerce.cms.user.domain.model.JoinForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String passwd;
    private LocalDate birthDay;
    @Column(unique = true)
    private String email;
    private String phone;

    private LocalDateTime verifyExpiredDate;
    private String verificationCode;
    private boolean verify;

    @Column(columnDefinition = "int default 0 ")
    private Integer balance;

    public static Customer form(JoinForm joinForm){
        return Customer.builder()
                .email(joinForm.getEmail().toLowerCase(Locale.ROOT))
                .name(joinForm.getName())
                .birthDay(joinForm.getBirth())
                .passwd(joinForm.getPasswd())
                .phone(joinForm.getPhone())
                .verify(false)
                .build();
    }

}
