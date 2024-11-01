package org.example.workspaceservice.Client.fallback;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
public class UserClientFallback implements UserClient {

	@Override
	public ApiResponse<UserResponse> getUserByEmail(String authorization, String email) {
		return null;
	}

	@Override
	public ApiResponse<UserResponse> getUserById(String authorization, UUID userId) {
		return null;
	}
}
