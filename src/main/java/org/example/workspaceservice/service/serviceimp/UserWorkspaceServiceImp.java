package org.example.workspaceservice.service.serviceimp;

import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.NotificationClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ConflictException;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.Workspace;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    private String retrieveToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            return jwtAuthToken.getToken().getTokenValue();
        }
        return null;
    }
    public ApiResponse<UserResponse> getUserByEmail(String email) {
        String token = retrieveToken();
        return userClient.getUserByEmail("Bearer " + token,email);
    }
    public ResponseEntity<?> sendNotificationToUser(NotificationRequest notificationRequest) {
        String token = retrieveToken();
        return notificationClient.sendNotificationToUser("Bearer " + token,notificationRequest);
    }

    @Override
    public Void inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) {
        ApiResponse<UserResponse> user = getUserByEmail(userWorkspaceRequest.getEmail());
        if (user.getPayload().getUserId() == null) {
            throw new NotFoundException("User email not found!");
        }
        workspaceRepository.findById(userWorkspaceRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace id " + userWorkspaceRequest.getWorkspaceId() + " not found!"));
        Optional<UserWorkspace> admin = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), userWorkspaceRequest.getWorkspaceId());
        if (admin.isEmpty()) {
            throw new ForbiddenException("You don't have permission to access this workspace!");
        }
        if (!admin.get().getIsAdmin()) {
            throw new ForbiddenException("Collaborator is not allowed invite member into this workspace!");
        }
        Optional<UserWorkspace> existUser = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(user.getPayload().getUserId()), userWorkspaceRequest.getWorkspaceId());
        if (existUser.isPresent()) {
            throw new ConflictException("User already exists join this workspace!");
        }
        NotificationRequest nr = new NotificationRequest();
        nr.setMessage("Stack Notes");
        nr.setTitle("Someone add to workspace");
        nr.setSenderId(getCurrentUser());
        nr.setReceiverId(user.getPayload().getUserId());
        sendNotificationToUser(nr);
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
        Optional<Workspace> workspace = workspaceRepository.findById(removeUserRequest.getWorkspaceId());
        if (workspace.isEmpty()) {
            throw new NotFoundException("Workspace id " + removeUserRequest.getWorkspaceId() + " not found!");
        }
        Optional<UserWorkspace> user = userWorkspaceRepository.findByUserIdAndWorkspaceId(
                removeUserRequest.getUserId(), removeUserRequest.getWorkspaceId());
        if (user.isEmpty()) {
            throw new NotFoundException("User id " + removeUserRequest.getUserId() + " not found!");
        }
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(
                UUID.fromString(getCurrentUser()), removeUserRequest.getWorkspaceId());
        if (userWorkspace.isEmpty()) {
            throw new ForbiddenException("You don't have permission to access this workspace!");
        }
        if (!userWorkspace.get().getIsAdmin()) {
            throw new ForbiddenException("User not allowed to remove collaborator from this workspace!");
        }
        if(userWorkspace.get().getUserId().equals(removeUserRequest.getUserId())) {
            throw new ForbiddenException("User not allowed to remove admin from this workspace!");
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
