package org.example.workspaceservice.service;

import jakarta.mail.MessagingException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.AcceptRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface UserWorkspaceService {
    UserWorkspace inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) throws MessagingException;
    Void deleteCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest);
    void acceptToJoinWorkspace(String userId,UUID workspaceId, Boolean isAccept) throws MessagingException;
}
