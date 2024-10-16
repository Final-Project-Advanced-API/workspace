package org.example.workspaceservice.Client.fallback;

import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class UserClientFallback implements UserClient {
    @Override
    public ApiResponse<UserResponse> getUserByEmail(String email) {
        return ApiResponse.<UserResponse>builder()
                .message("INTERNAL SERVER ERROR")
                .payload(new UserResponse(UUID.randomUUID(), "unknown", "unknown", "unknown", LocalDate.now(), "unknown", "unknown", LocalDateTime.now(), LocalDateTime.now()))
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
