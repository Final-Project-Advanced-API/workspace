package org.example.workspaceservice.model.response;
import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWorkspaceResponse {
    private UUID userRoleId;
    private UUID userId;
    private UUID workspaceId;
    private Boolean isAdmin;
}
