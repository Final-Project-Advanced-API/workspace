package org.example.workspaceservice.Client.fallback;

import org.example.workspaceservice.Client.NotificationClient;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationClientFallback implements NotificationClient {
	@Override
	public ResponseEntity<?> sendNotificationToUser(NotificationRequest notificationRequest) {
		return null;
	}
}
