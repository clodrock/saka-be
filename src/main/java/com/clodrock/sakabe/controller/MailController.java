package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.model.MailRequest;
import com.clodrock.sakabe.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody MailRequest request) throws MessagingException {
        mailService.sendMail(request.getSendTo(), request.getSubject(), request.getContent());
        return "Mail sent successfully!";
    }
}