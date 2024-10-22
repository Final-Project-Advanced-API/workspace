package org.example.workspaceservice.Client;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/api/v1/notifications")
    ApiResponse<?> sendNotificationToUser(@RequestBody NotificationRequest notificationRequest);
}
