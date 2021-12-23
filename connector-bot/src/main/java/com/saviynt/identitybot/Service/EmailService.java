package com.saviynt.identitybot.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Async
    public void sendText(String toEmail, String subject, String message) {
        SimpleMailMessage textMessage = new SimpleMailMessage();
        textMessage.setTo(toEmail);
        textMessage.setSubject(subject);
        textMessage.setText(message);
        textMessage.setFrom(fromEmailAddress);
        try {
            mailSender.send(textMessage);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
        }
    }

}
