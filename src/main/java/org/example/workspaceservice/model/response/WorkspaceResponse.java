package org.example.workspaceservice.model.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.workspaceservice.model.entity.UserWorkspace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkspaceResponse {
    private UUID workspaceId;
    private String workspaceName;
    private Boolean isPrivate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserResponse> users;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
