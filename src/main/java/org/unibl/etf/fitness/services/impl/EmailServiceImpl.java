package org.unibl.etf.fitness.services.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.models.entities.CategoryEntity;
import org.unibl.etf.fitness.models.entities.FitnessProgramEntity;
import org.unibl.etf.fitness.models.entities.SubscriptionEntity;
import org.unibl.etf.fitness.repositories.CategoryRepository;
import org.unibl.etf.fitness.repositories.FitnessProgramRepository;
import org.unibl.etf.fitness.repositories.SubscriptionRepository;
import org.unibl.etf.fitness.services.EmailService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${account.verification.url}")
    private String accountVerificationUrl;

    private final SubscriptionRepository subscriptionRepository;
    private final FitnessProgramRepository fitnessProgramRepository;
    public EmailServiceImpl(JavaMailSender mailSender, SubscriptionRepository subscriptionRepository, FitnessProgramRepository fitnessProgramRepository) {
        this.mailSender = mailSender;
        this.subscriptionRepository = subscriptionRepository;
        this.fitnessProgramRepository = fitnessProgramRepository;

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

    @Override
    @Async
    @Scheduled(cron = "0 30 20 * * ?")
    public void sendNewFitnessProgramsForCategory() {
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findAll();
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfYesterday = LocalDateTime.of(currentDate,LocalTime.MIN);
        Date startDate = Date.from(startOfYesterday.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime endOfYesterday = LocalDateTime.of(currentDate, LocalTime.MAX);
        Date endDate = Date.from(endOfYesterday.atZone(ZoneId.systemDefault()).toInstant());

        subscriptions.forEach(el -> {
            try {
                List<FitnessProgramEntity> fitnessPrograms = fitnessProgramRepository
                     .getAllByCategoryIdAndCreationDateIsAfterAndCreationDateIsBefore(el.getCategory().getId(), startDate, endDate);
                if(fitnessPrograms.size() != 0) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setSubject("Notification, new fitness programs for category: " + el.getCategory().getName());
                    String text = "Fitness programs:\n\n";
                    for (var fp : fitnessPrograms) {
                        text = text + "\t * " + fp.getName() + "\n";
                    }
                    message.setText(text);
                    message.setFrom(fromMail);
                    message.setTo(el.getClient().getMail());
                    this.mailSender.send(message);
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        });
    }
}
