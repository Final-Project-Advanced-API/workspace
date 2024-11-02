package org.example.workspaceservice.service.serviceimp;

import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.NotificationClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.BadRequestException;
import org.example.workspaceservice.exception.ConflictException;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.Workspace;
import org.example.workspaceservice.model.entity.WorkspaceElastic;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceElasticRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserWorkspaceServiceImp implements UserWorkspaceService {
	private final UserWorkspaceRepository userWorkspaceRepository;
	private final UserClient userClient;
	private final NotificationClient notificationClient;
	private final WorkspaceRepository workspaceRepository;
	private final WorkspaceElasticRepository workspaceElasticRepository;

	public UUID getCurrentUser() {
		return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
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
		return userClient.getUserByEmail("Bearer " + token, email);
	}

	public ResponseEntity<?> sendNotificationToUser(NotificationRequest notificationRequest) {
		String token = retrieveToken();
		return notificationClient.sendNotificationToUser("Bearer " + token, notificationRequest);
	}

	ApiResponse<UserResponse> getUserById(UUID userId) {
		String tokenValue = retrieveToken();
		return userClient.getUserById("Bearer " + tokenValue, userId);
	}

	@Override
	public Void inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) {
		ApiResponse<UserResponse> user = getUserByEmail(userWorkspaceRequest.getEmail());
		if (user == null) {
			throw new NotFoundException("User email not found!");
		}
		WorkspaceElastic elastic = workspaceElasticRepository.findById(userWorkspaceRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace "+userWorkspaceRequest.getWorkspaceId()+" not found!"));
	    if (!elastic.getCreatedBy().equals(getCurrentUser())){
			throw new ForbiddenException("You don't have permission to invite user into this workspace!");
		}
		Optional<UserWorkspace> existUser = userWorkspaceRepository.findByUserIdAndWorkspaceWorkspaceId(UUID.fromString(user.getPayload().getUserId()), userWorkspaceRequest.getWorkspaceId());
		if (existUser.isPresent()) {
			throw new ConflictException("User already exists join this workspace!");
		}
		Workspace workspace = workspaceRepository.findById(userWorkspaceRequest.getWorkspaceId()).get();
		ApiResponse<UserResponse> userSender = getUserById(getCurrentUser());
		if(userSender != null){
			NotificationRequest nr = new NotificationRequest();
			nr.setMessage("STACK NOTES");
			nr.setTitle(userSender.getPayload().getUsername() + " has added you to the \"" + elastic.getWorkspaceName() + "\" workspace on Stack Notes.");
			nr.setSenderId(getCurrentUser().toString());
			nr.setReceiverId(user.getPayload().getUserId());
			sendNotificationToUser(nr);
		}
		UserWorkspace userWorkspace = new UserWorkspace();
		userWorkspace.setUserId(UUID.fromString(user.getPayload().getUserId()));
		userWorkspace.setWorkspace(workspace);
		userWorkspace.setIsAdmin(false);
		userWorkspaceRepository.save(userWorkspace);
		return null;
	}

	@Override
	public Void removeCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest) {
		WorkspaceElastic elastic = workspaceElasticRepository.findById(removeUserRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace "+removeUserRequest.getWorkspaceId()+" not found!"));
		Optional<UserWorkspace> user = userWorkspaceRepository.findByUserIdAndWorkspaceWorkspaceId(
				removeUserRequest.getUserId(), removeUserRequest.getWorkspaceId());
		if (user.isEmpty()) {
			throw new NotFoundException("User id " + removeUserRequest.getUserId() + " not found!");
		}
		if (!elastic.getCreatedBy().equals(getCurrentUser())) {
			throw new ForbiddenException("You don't have permission to remove user from this workspace!");
		}
		if (elastic.getCreatedBy().equals(removeUserRequest.getUserId())) {
			throw new ForbiddenException("You don't have permission to remove admin from this workspace!");
		}
		userWorkspaceRepository.delete(user.get());
		return null;
	}


	@Override
	public UserWorkspace getUserByUserIdAndWorkspaceId(UUID userId, UUID workspaceId) {
		Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceWorkspaceId(userId, workspaceId);
		if (userWorkspace.isEmpty()) {
			throw new NotFoundException("User and workspace id not found!");
		}
		return userWorkspace.get();
	}
}
