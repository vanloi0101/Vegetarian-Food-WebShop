package com.devteria.identityservice.service;

import com.devteria.identityservice.entity.Order;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";

    @Value("${app.mail.from:}")
    private String fromEmail;

    @Value("${app.mail.oauth-access-token:}")
    private String accessToken;

    public void sendOrderConfirmationEmail(String to, Order order) {
        if (fromEmail == null || fromEmail.isBlank() || accessToken == null || accessToken.isBlank()) {
            log.warn("Bỏ qua gửi email: cấu hình app.mail.from và app.mail.oauth-access-token (hoặc trong application.yaml).");
            return;
        }
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(
                    props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, accessToken);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Order Confirmation - VeggieFood");
            message.setText("Your order #" + order.getId() + " has been placed successfully!");

            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST, 587, fromEmail, accessToken);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            log.info("Order confirmation email sent to {}", to);

        } catch (MessagingException e) {
            log.error("Error sending email: ", e);
        }
    }
}
