package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.NotificationClientFallback;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-service",url = "http://localhost:8082",fallback = NotificationClientFallback.class)
@Primary
public interface NotificationClient {
    @PostMapping("/api/v1/notifications")
    ResponseEntity<?> sendNotificationToUser(@RequestHeader("Authorization") String authorization,@RequestBody NotificationRequest notificationRequest);
}
