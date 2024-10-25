package org.example.workspaceservice.model.response;
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
