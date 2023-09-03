package com.ecommerce.cms.user.client.mailgun;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
public class SendMailForm {
    private String from;
    private String to;
    private String subject;
    private String text;

}
