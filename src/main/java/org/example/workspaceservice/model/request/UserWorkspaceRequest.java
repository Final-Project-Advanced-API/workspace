package org.example.workspaceservice.model.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkspaceRequest {
    @Email
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@gmail\\.com$",
            message = "Invalid email!")
    private String email;
    private UUID workspaceId;
}
