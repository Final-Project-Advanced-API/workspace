package org.example.workspaceservice.service;

import jakarta.mail.MessagingException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.thymeleaf.context.Context;

import java.lang.reflect.Method;

public interface MailSenderService {
    void sendMail(String toEmail, String workspaceId, Boolean isAccept) throws MessagingException;
}
