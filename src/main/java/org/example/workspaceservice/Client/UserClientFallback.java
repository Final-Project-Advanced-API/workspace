package org.example.workspaceservice.Client;

import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserClientFallback implements UserClient {
    @Override
    public ApiResponse<UserResponse> getUserByEmail(String email) {
        return ApiResponse.<UserResponse>builder()
                .message("INTERNAL SERVER ERROR")
                .payload(new UserResponse(UUID.fromString("unkown"),"unknow","unknow","unknow", LocalDate.now(),"no","no",LocalDateTime.now(),LocalDateTime.now()))
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
