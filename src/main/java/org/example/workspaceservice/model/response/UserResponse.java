package org.example.workspaceservice.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID userId;
    private String userName;
    private String fullName;
    private String gender;
    private LocalDate dob;
    private String profile;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
