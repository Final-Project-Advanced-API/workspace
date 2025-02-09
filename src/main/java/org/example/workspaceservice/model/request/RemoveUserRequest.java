package org.example.workspaceservice.model.request;
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
    private UUID userId;
    private UUID workspaceId;
}
