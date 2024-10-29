package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.NotificationClientFallback;
import org.example.workspaceservice.config.FeignClientConfig;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service",url = "http://localhost:8082",configuration = FeignClientConfig.class,fallback = NotificationClientFallback.class)
@Primary
public interface NotificationClient {
    @PostMapping("/api/v1/notifications")
    ResponseEntity<?> sendNotificationToUser(@RequestBody NotificationRequest notificationRequest);
}
