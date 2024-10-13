package org.example.workspaceservice.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveUserRequest {
    @NotNull
    @NotBlank
    private UUID userId;
    @NotNull
    @NotBlank
    private UUID workspaceId;
}
