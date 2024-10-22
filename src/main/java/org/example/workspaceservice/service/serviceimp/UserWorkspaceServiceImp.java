package org.example.workspaceservice.service.serviceimp;

import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.NotificationClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.WorkspaceElastic;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceElasticRepository;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserWorkspaceServiceImp implements UserWorkspaceService {
    private final WorkspaceElasticRepository workspaceElasticRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Void inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) {
        Optional<UserWorkspace> admin = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), userWorkspaceRequest.getWorkspaceId());
        if (admin.isPresent()) {
            if (!admin.get().getIsAdmin()) {
                throw new ForbiddenException("Not allowed to invite a collaborator");
            }
        } else {
            throw new NotFoundException("User workspace not found");
        }
        ApiResponse<UserResponse> user = userClient.getUserByEmail(userWorkspaceRequest.getEmail());
        if (user.getPayload().getUserId() == null) {
            throw new NotFoundException("User email not found");
        }
        WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(userWorkspaceRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace id " + userWorkspaceRequest.getWorkspaceId() + " not found"));
        notificationClient.sendNotificationToUser(new NotificationRequest("Stack Notes","Someone add to workspace "+workspaceElastic.getWorkspaceName(),getCurrentUser(),user.getPayload().getUserId()));
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
        Optional<UserWorkspace> admin = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), removeUserRequest.getWorkspaceId());
        if (admin.isPresent()) {
            if (!admin.get().getIsAdmin()) {
                throw new ForbiddenException("Not allowed to remove a collaborator");
            }
        } else {
            throw new NotFoundException("User workspace not found");
        }
        userWorkspaceRepository.deleteByUserIdAndWorkspaceId(removeUserRequest.getUserId(), removeUserRequest.getWorkspaceId());
        return null;
    }

}
