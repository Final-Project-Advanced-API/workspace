package org.example.workspaceservice.model.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRequest {
    @NotBlank(message = "Workspace name must not be blank")
    @Size(max = 100, message = "Workspace name must be less than 100 characters")
    private String workspaceName;
    private Boolean isPrivate;
}
