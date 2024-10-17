package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.UserClientFallback;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient(name = "user-service",url = "http://localhost:8081",configuration = UserClientFallback.class)
@Primary
public interface UserClient {
    @GetMapping("/api/v1/users/email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam String email);

    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable UUID userId);
}
