package org.example.workspaceservice.Client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service",url = "http://localhost:9999")
public class UserClient {
}
