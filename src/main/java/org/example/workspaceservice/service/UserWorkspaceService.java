package org.example.workspaceservice.service;

import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
public interface UserWorkspaceService {
    UserWorkspace inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest);
    Void removeCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest);
}
