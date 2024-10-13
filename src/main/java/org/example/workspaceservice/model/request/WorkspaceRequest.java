package org.example.workspaceservice.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRequest {
    @NotNull
    @NotBlank
    private String workspaceName;

    @NotNull
    @Pattern(regexp = "^(true|false)$", message = "must be 'true' or 'false'")
    private Boolean isPrivate;
}
