package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendMail() {
        emailService.sendNotification("praveenwppe@gmail.com","testing JMS","mail received");
    }
}
