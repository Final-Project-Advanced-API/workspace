package org.example.workspaceservice.model.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkspaceResponse {
    private UUID userRoleId;
    private UUID userId;
    private Boolean isAccept;
    private Boolean isAdmin;
}
