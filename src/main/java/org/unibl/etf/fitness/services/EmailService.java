package org.unibl.etf.fitness.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.unibl.etf.fitness.models.dto.AdvisorMailDTO;

public interface EmailService {
    @Async
    void sendEmail(MimeMessage message, MimeMessageHelper helper, String to);

    @Async
    void sendVerificationEmail(String token,String to);

    @Async
    @Scheduled
    void sendNewFitnessProgramsForCategory();

    @Async
    void sendAdvisorMail(AdvisorMailDTO request);
}
