package org.example.workspaceservice.service.serviceimp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.service.MailSenderService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MailSenderServiceImp implements MailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(String toEmail,UUID userId, String workspaceId, Boolean isAccept) throws MessagingException {
        Context context = new Context();
        context.setVariable("userId", userId);
        context.setVariable("workspaceId", workspaceId);
        context.setVariable("isAccept",isAccept);
        String process = templateEngine.process("invite", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("Stack Note");
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.setTo(toEmail);
        javaMailSender.send(mimeMessage);
    }
}
