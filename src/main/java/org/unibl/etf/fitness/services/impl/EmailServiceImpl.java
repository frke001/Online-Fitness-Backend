package org.unibl.etf.fitness.services.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.services.EmailService;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${account.verification.url}")
    private String accountVerificationUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async
    @Override
    public void sendEmail(MimeMessage message, MimeMessageHelper helper, String to) {
        try {
            helper.setFrom(fromMail);
            helper.setTo(to);
            this.mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public void sendVerificationEmail(String token, String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setSubject("Account verification, Online Fitness");
            ClassPathResource htmlPath = new ClassPathResource("AccountVerification.html");
            var html= Files.readString(Path.of(htmlPath.getFile().getAbsolutePath()));
            html=html.replace("validation.url",accountVerificationUrl+token);
            helper.setText(html,true);
            helper.setFrom(fromMail);
            helper.setTo(to);
            this.mailSender.send(message);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
