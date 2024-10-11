package org.example.workspaceservice.service;

import jakarta.mail.MessagingException;

import java.util.UUID;

public interface MailSenderService {
    void sendMail(String toEmail, UUID userId, String workspaceId, Boolean isAccept) throws MessagingException;
}
