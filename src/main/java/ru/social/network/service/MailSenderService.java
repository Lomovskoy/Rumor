package ru.social.network.service;

import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public interface MailSenderService {
    void send(String emailTo, String subject, String message);
}
