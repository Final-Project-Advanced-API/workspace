package org.example.workspaceservice.Client.fallback;

import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public ApiResponse<UserResponse> getUserByEmail(String email) {

        return ApiResponse.<UserResponse>builder()
                .message("User service is currently unavailable. Returning fallback data.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)// 503 Service Unavailable
                .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .payload(getUnknownUserResponse()) // Provide fallback UserResponse
                .timestamp(LocalDateTime.now())
                .build();
    }



    @Override
    public ApiResponse<UserResponse> getUserById(UUID userId) {
        return null;
    }

    private UserResponse getUnknownUserResponse() {
        UserResponse unknownUser = new UserResponse();
        unknownUser.setUserId(null);
        unknownUser.setUsername("unknown");
        unknownUser.setFullName("unknown");
        unknownUser.setGender("unknown");
        unknownUser.setDob("unknown");
        unknownUser.setEmail("unknown");
        unknownUser.setCreatedDate("unknown");
        unknownUser.setUpdatedDate("unknown");
        unknownUser.setProfile("unknown");
        unknownUser.setBio("unknown");
        return unknownUser;
    }
}

