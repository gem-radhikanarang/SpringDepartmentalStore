package com.admin.SpringBootDepartmentalStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public final void sendEmail(final String toEmail, final String subject, final String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("narangradhika2001@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);


    }

}
