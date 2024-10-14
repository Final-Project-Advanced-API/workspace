package org.example.workspaceservice.model.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptRequest {
    @Email
    @NotBlank
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@gmail\\.com$",
            message = "Email is valid")
    private String email;

    private UUID workspaceId;

    private Boolean isAccept;
}
