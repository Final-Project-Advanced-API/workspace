package org.example.workspaceservice.model.request;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRequest {
    @NotBlank(message = "Workspace cannot be blank. Please provide a title.")
    @NotNull(message = "Workspace cannot be null. A valid workspace is required.")
    @NotEmpty(message = "Workspace cannot be empty. A valid workspace is required.")
    @Size(max = 255, message = "Workspace length must be between 2 and 255 characters.")
    private String workspaceName;
}
