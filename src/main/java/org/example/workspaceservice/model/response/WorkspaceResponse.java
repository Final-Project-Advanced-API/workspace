package org.example.workspaceservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResponse {
    private UUID workspaceId;
    private String workspaceName;
    private Boolean isPrivate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserWorkspaceResponse> users;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DocumentResponse> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
