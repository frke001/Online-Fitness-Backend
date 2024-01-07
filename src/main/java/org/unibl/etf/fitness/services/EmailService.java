package org.unibl.etf.fitness.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendEmail(MimeMessage message, MimeMessageHelper helper, String to);

    @Async
    void sendVerificationEmail(String token,String to);
}
