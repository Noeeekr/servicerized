package com.github.noeeekr.servicerized.identity.services.notification;

import java.util.List;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailTemplateService {
    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
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