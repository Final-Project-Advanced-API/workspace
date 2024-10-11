package org.example.workspaceservice.service;

import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.AcceptRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface UserWorkspaceService {
    UserWorkspace inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest);

    Void deleteCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest);

    UserWorkspace acceptToJoinWorkspace(String email,UUID workspaceId, Boolean isAccept);
}
