package org.example.workspaceservice.service.serviceimp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.AcceptRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.service.MailSenderService;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.reflect.Method;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MailSenderServiceImp implements MailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Context context;

    @Override
    public void sendMail(String toEmail, String workspaceId, Boolean isAccept) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        context.setVariable("toEmail", toEmail);
        context.setVariable("workspaceId", workspaceId);
        context.setVariable("isAccept",isAccept);
        String processedHtml = templateEngine.process("invite", context);
        MimeMessageHelper mailMessage = new MimeMessageHelper(message, true, "UTF-8");
        mailMessage.setTo(toEmail);
        mailMessage.setText(processedHtml, true);
        javaMailSender.send(message);
    }
}
