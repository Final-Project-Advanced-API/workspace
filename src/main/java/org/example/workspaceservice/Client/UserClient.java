package org.example.workspaceservice.Client;


import org.example.workspaceservice.config.FeignClientConfig;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",url = "http://localhost:9090",fallback = UserClientFallback.class,configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/api/v1/users/email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam String email);
}
