package org.example.workspaceservice.Client.fallback;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.http.HttpStatus;
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
		return ApiResponse.<UserResponse>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).payload(defaultResponse()).build();
	}


	public UserResponse defaultResponse() {
		return UserResponse.builder()
				.userId("unknown")
				.fullName("unknown")
				.profile("unknown")
				.gender("unknown")
				.email("unknown@gmail.com")
				.dob("unknown")
				.profile("unknown")
				.dob("unknown")
				.build();
	}
}
