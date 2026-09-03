package com.github.noeeekr.servicerized.identity.services.notification;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailTemplateService {
    @Autowired
    private final SpringTemplateEngine templateEngine;

    public EmailTemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildConfirmationEmailHtml(String userName, String confirmUrl) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("confirmationUrl", confirmUrl);

        context.setVariable("additionalParagraphs", List.of(
                "Este link continuará ativo pelas próximas 24 horas por razões de segurança.",
                "Se você encontrar algum problema ao acessar sua conta, por favor contate o nosso time de suporte."));

        return templateEngine.process("confirmation-email", context);
    }
}