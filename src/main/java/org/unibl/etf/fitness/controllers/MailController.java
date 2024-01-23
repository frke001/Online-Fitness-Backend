package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitness.models.dto.AdvisorMailDTO;
import org.unibl.etf.fitness.services.EmailService;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public void sendAdvisorMail(@RequestBody AdvisorMailDTO request){
        emailService.sendAdvisorMail(request);
    }
}
