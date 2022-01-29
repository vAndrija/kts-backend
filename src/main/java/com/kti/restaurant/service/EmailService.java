package com.kti.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setSubject(subject);
            MimeMessageHelper helper =  new MimeMessageHelper(msg,true);
            helper.setTo(toEmail);
            helper.setFrom("andrijavojinovicpa@gmail.com");
            helper.setSubject(subject);
            helper.setText(message,true);
            javaMailSender.send(msg);
        } catch (MessagingException ex) {
            System.out.println("Error while sending email");
        }
    }


    public void sendRegistrationMail(String toEmail, String subject, String token) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setSubject(subject);
            MimeMessageHelper helper =  new MimeMessageHelper(msg,true);
            helper.setTo(toEmail);
            helper.setFrom("andrijavojinovicpa@gmail.com");
            helper.setSubject(subject);
            String message = "<h1>Dobro došli na sistem</h1><p>Vaša lozinka za prijavu: "+token+"</p>";
            helper.setText(message,true);
            javaMailSender.send(msg);
        } catch (MessagingException ex) {
            System.out.println("Error while sending email");
        }
    }

    public void sendResetLinkMail(String toEmail, String subject, String token) {
        String path = "http://localhost:4200/auth/password-reset/";
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setSubject(subject);
            MimeMessageHelper helper =  new MimeMessageHelper(msg,true);
            helper.setTo(toEmail);
            helper.setFrom("andrijavojinovicpa@gmail.com");
            helper.setSubject(subject);
            String message = "<h1>Resetovanje lozinke</h1><p>Da biste resetovali lozinku, kliknite na sledeći link: " +
                    "<a href='"+path+token+"'>"+path+token+"</a>"+"</p>";
            helper.setText(message,true);
            javaMailSender.send(msg);
        } catch (MessagingException ex) {
            System.out.println("Error while sending email");
        }
    }
}
