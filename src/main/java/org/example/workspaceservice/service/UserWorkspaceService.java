package org.example.workspaceservice.service;

import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;

import java.util.UUID;

public interface UserWorkspaceService {
    Void inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest);
    Void removeCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest);
    UserWorkspace getUserByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);
}
