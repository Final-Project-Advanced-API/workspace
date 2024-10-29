package org.example.workspaceservice.model.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String title;
    private String message;
    private String senderId;
    private String receiverId;
}