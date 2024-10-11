package org.example.workspaceservice.service;

import jakarta.mail.MessagingException;
public interface MailSenderService {
    void sendMail(String toEmail, String workspaceId, Boolean isAccept) throws MessagingException;
}
