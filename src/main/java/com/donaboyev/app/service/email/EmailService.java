package com.donaboyev.app.service.email;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public Boolean send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("plcomuchun@gmail.com");
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email",e);
            throw new IllegalStateException("Failed to send email");
        }
    }

}
