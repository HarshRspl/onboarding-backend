package com.rspl.onboarding.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String fromEmail;
    @Value("${app.onboarding-link-base}") private String linkBase;

    @Async
    public void sendOnboardingLink(String to, String name, String token, String designation) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(fromEmail); h.setTo(to);
            h.setSubject("Welcome to RSPL Health – Complete Your Onboarding");
            String link = linkBase + "/" + token;
            h.setText("<h2>Dear " + name + "</h2><p>Click to complete onboarding:</p><a href='" + link + "'>Complete Onboarding</a>", true);
            mailSender.send(msg);
            log.info("Email sent to: {}", to);
        } catch (Exception e) { log.error("Email failed: {}", e.getMessage()); }
    }

    @Async
    public void sendApprovalEmail(String to, String name) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(fromEmail); h.setTo(to);
            h.setSubject("Onboarding Approved – Welcome to RSPL Health!");
            h.setText("<h2>Congratulations " + name + "!</h2><p>Your onboarding is approved. Welcome aboard!</p>", true);
            mailSender.send(msg);
        } catch (Exception e) { log.error("Approval email failed: {}", e.getMessage()); }
    }

    @Async
    public void sendRejectionEmail(String to, String name, String reason) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(fromEmail); h.setTo(to);
            h.setSubject("RSPL Health – Onboarding Update");
            h.setText("<h2>Dear " + name + "</h2><p>Reason: " + reason + "</p>", true);
            mailSender.send(msg);
        } catch (Exception e) { log.error("Rejection email failed: {}", e.getMessage()); }
    }
}
