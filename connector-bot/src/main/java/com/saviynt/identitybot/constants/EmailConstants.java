package com.saviynt.identitybot.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EmailConstants {

    @Value("${password.reset.email.content}")
    private String passwordResetEmailContent;

    @Value("${password.reset.email.subject}")
    private String passwordResetEmailSubject;

    @Value("${password.reset.url}")
    private String passwordResetUrl;

    @Value("${password.reset.success.email.subject}")
    private String passwordResetSuccessEmailSubject;

    @Value("${password.reset.success.email.content}")
    private String passwordResetSuccessEmailContent;

}
