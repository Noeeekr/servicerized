package com.github.noeeekr.servicerized.identity.services.notification;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class NotificationService {
    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private final EmailTemplateService template;

    /**
     * sendConfirmation creates a email confirmation of type "Click to confirm". Type "Click to
     * confirm" contains a button with a link to the confirmation endpoint.
     * 
     * @param to The email address of the target user.
     * @param messageBody The confirmation endpoint.
     * 
     * @throws MessagingException Check {@link JavaMailSender#send(MimeMessage...)} for more
     *         information about the exceptions thrown.
     */
    public void sendConfirmation(@NonNull String to) throws MessagingException, NullPointerException {
        String messageBody = this.template.buildConfirmationEmailHtml("Usuário", "");
        Objects.requireNonNull(messageBody, "Confirmation e-mail message body must exist");

        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper messageBuilder = new MimeMessageHelper(message, true, "UTF-8");

        messageBuilder.setTo(to);
        messageBuilder.setSubject("Confirmar Conta em Servicerized");
        messageBuilder.setText(messageBody, true);

        mailSender.send(message);
    }
}
