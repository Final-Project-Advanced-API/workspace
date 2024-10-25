package org.example.workspaceservice.service.serviceimp;

import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.NotificationClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ConflictException;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserWorkspaceServiceImp implements UserWorkspaceService {
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    private final WorkspaceRepository workspaceRepository;

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Void inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) {
        ApiResponse<UserResponse> user = userClient.getUserByEmail(userWorkspaceRequest.getEmail());
        if (user.getPayload().getUserId() == null) {
            throw new NotFoundException("User email not found!");
        }
        workspaceRepository.findById(userWorkspaceRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace id " + userWorkspaceRequest.getWorkspaceId() + " not found"));
        Optional<UserWorkspace> admin = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), userWorkspaceRequest.getWorkspaceId());
        if (admin.isEmpty()) {
            throw new NotFoundException("Workspace id " + userWorkspaceRequest.getWorkspaceId() + " not found!");
        }
        if (!admin.get().getIsAdmin()) {
            throw new ForbiddenException("User not allowed invite collaborator this workspace.");
        }
        Optional<UserWorkspace> existUser = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(user.getPayload().getUserId()), userWorkspaceRequest.getWorkspaceId());
        if (existUser.isPresent()) {
            throw new ConflictException("Collaborator already exists join this workspace");
        }
        NotificationRequest nr = new NotificationRequest();
        nr.setMessage("Stack Notes");
        nr.setTitle("Someone add to workspace");
        nr.setSenderId(getCurrentUser());
        nr.setReceiverId(user.getPayload().getUserId());
        notificationClient.sendNotificationToUser(nr);
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUserId(UUID.fromString(user.getPayload().getUserId()));
        userWorkspace.setWorkspaceId(userWorkspaceRequest.getWorkspaceId());
        userWorkspace.setIsAdmin(false);
        userWorkspaceRepository.save(userWorkspace);
        return null;
    }

    @Transactional
    @Override
    public Void removeCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest) {
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(
                UUID.fromString(getCurrentUser()), removeUserRequest.getWorkspaceId());
        if (userWorkspace.isEmpty()) {
            throw new NotFoundException("Workspace id " + removeUserRequest.getWorkspaceId() + " not found!");
        }
        if (!userWorkspace.get().getIsAdmin()) {
            throw new ForbiddenException("User not allowed to remove collaborator from this workspace.");
        }
        if(userWorkspace.get().getUserId().equals(removeUserRequest.getUserId())) {
            throw new ForbiddenException("User not allowed to remove admin from this workspace.");
        }
        userWorkspaceRepository.deleteByUserIdAndWorkspaceId(removeUserRequest.getUserId(), removeUserRequest.getWorkspaceId());
        return null;
    }


    @Override
    public UserWorkspace getUserByUserIdAndWorkspaceId(UUID userId, UUID workspaceId) {
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(userId, workspaceId);
        if (userWorkspace.isEmpty()) {
            throw new NotFoundException("User and workspace id not found!");
        }
        return userWorkspace.get();
    }

}
