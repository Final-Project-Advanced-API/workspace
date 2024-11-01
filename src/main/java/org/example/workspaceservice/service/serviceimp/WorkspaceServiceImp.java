package org.example.workspaceservice.service.serviceimp;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.DocumentClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.Workspace;
import org.example.workspaceservice.model.entity.WorkspaceElastic;
import org.example.workspaceservice.model.request.WorkspaceRequest;
import org.example.workspaceservice.model.response.*;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceElasticRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.WorkspaceService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class WorkspaceServiceImp implements WorkspaceService {
	private final WorkspaceRepository workspaceRepository;
	private final UserWorkspaceRepository userWorkspaceRepository;
	private final WorkspaceElasticRepository workspaceElasticRepository;
	private final ModelMapper modelMapper;
	private final DocumentClient documentClient;
	private final UserClient userClient;

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

	ApiResponse<UserResponse> getUserById(UUID userId) {
		String tokenValue = retrieveToken();
		return userClient.getUserById("Bearer " + tokenValue, userId);
	}

	void deleteDocumentByWorkspaceId(UUID workspaceId) {
		String tokenValue = retrieveToken();
		documentClient.deleteDocumentByWorkspaceId("Bearer " + tokenValue, workspaceId);
	}


	@Override
	public WorkspaceResponse createWorkspace(WorkspaceRequest workspaceRequest) {
		// add to workspace
		Workspace workspace = new Workspace();
		workspace.setWorkspaceName(workspaceRequest.getWorkspaceName());
		workspace.setIsPrivate(true);
		workspace.setCreatedAt(LocalDateTime.now());
		workspace.setUpdatedAt(LocalDateTime.now());
		Workspace ws = workspaceRepository.save(workspace);
		// add to user-workspace
		UserWorkspace userWorkspace = modelMapper.map(workspace, UserWorkspace.class);
		userWorkspace.setUserId(UUID.fromString(getCurrentUser()));
		userWorkspace.setIsAdmin(true);
		userWorkspaceRepository.save(userWorkspace);
		// add to elastic
		WorkspaceResponse workspaceResponse = new WorkspaceResponse();
		List<UserResponse> userResponses = new ArrayList<>();
		WorkspaceElastic elastic = workspaceElasticRepository.save(modelMapper.map(ws, WorkspaceElastic.class));
		ApiResponse<UserResponse> user = getUserById(UUID.fromString(getCurrentUser()));
		if (user != null) {
			UserResponse userResponse = user.getPayload();
			userResponse.setIsAdmin(true);
			userResponses.add(userResponse);
			workspaceResponse.setUsers(userResponses);
		}
		modelMapper.map(elastic, workspaceResponse);
		return workspaceResponse;
	}

	@Override
	public List<WorkspaceResponse> getAllWorkspace() {
		// Find all workspaces from ElasticSearch
		Iterable<WorkspaceElastic> workspaceElasticsIterable = workspaceElasticRepository.findAll();
		List<WorkspaceElastic> workspaceElastics = new ArrayList<>();
		workspaceElasticsIterable.forEach(workspaceElastics::add);
		List<WorkspaceResponse> workspaceResponses = new ArrayList<>();
		workspaceElastics.forEach(workspace -> {
			Optional<UserWorkspace> lstUserWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(
					UUID.fromString(getCurrentUser()), workspace.getWorkspaceId()
			);
			if (lstUserWorkspace.isPresent()) {
				WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
				List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspace.getWorkspaceId());
				List<UserResponse> userResponses = new ArrayList<>();
				if (!userWorkspaces.isEmpty()) {
					userWorkspaces.forEach(userWorkspace -> {
						ApiResponse<UserResponse> userApiResponse = getUserById(userWorkspace.getUserId());
						if (userApiResponse != null && userApiResponse.getPayload() != null) {
							UserResponse userResponse = modelMapper.map(userApiResponse.getPayload(), UserResponse.class);
							userResponse.setIsAdmin(userWorkspace.getIsAdmin());
							userResponses.add(userResponse);
						}
					});
				}
				workspaceResponse.setUsers(userResponses);
				workspaceResponses.add(workspaceResponse);
			}
		});
		return workspaceResponses;
	}

	@Override
	public WorkspaceResponse updateWorkspace(UUID workspaceId, WorkspaceRequest workspaceRequest) {
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId)
				.orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found!"));
		Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), workspaceId);
		if (userWorkspace.isEmpty()) {
			throw new ForbiddenException("You don't have permission to access this workspace!");
		}
		if (!userWorkspace.get().getIsAdmin()) {
			throw new ForbiddenException("You are collaborator allowed to update this workspace!");
		}
		Workspace workspace = workspaceRepository.findById(workspaceId)
				.orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found!"));
		modelMapper.map(workspaceRequest, workspace);
		workspace.setUpdatedAt(LocalDateTime.now());
		workspaceRepository.save(workspace);
		modelMapper.map(workspaceRequest, workspaceElastic);
		workspaceElastic.setCreatedAt(workspaceElastic.getCreatedAt());
		workspaceElastic.setUpdatedAt(LocalDateTime.now());
		workspaceElasticRepository.save(workspaceElastic);
		return modelMapper.map(workspaceElastic, WorkspaceResponse.class);
	}

	@Transactional
	@Override
	public Void deleteWorkspace(UUID workspaceId) {
		//find elastic
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), workspaceId);
		if (userWorkspace.isEmpty()) {
			throw new ForbiddenException("You don't have permission to access this workspace!");
		}
		if (!userWorkspace.get().getIsAdmin()) {
			throw new ForbiddenException("You are collaborator allowed to delete this workspace!");
		}
		try {
			deleteDocumentByWorkspaceId(workspaceId);
		} catch (FeignException.NotFound ignored) {
		}
		//delete workspace
		workspaceRepository.deleteById(workspaceId);
		//delete user-workspace
		userWorkspaceRepository.deleteByWorkspaceId(workspaceId);
		//delete elastic
		workspaceElasticRepository.delete(workspaceElastic);
		return null;
	}

	@Override
	public WorkspaceResponse getWorkspace(UUID workspaceId) {
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		Optional<UserWorkspace> lstUserWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(
				UUID.fromString(getCurrentUser()), workspaceId);
		if (lstUserWorkspace.isEmpty()) {
			throw new ForbiddenException("You don't have permission to access this workspace!");
		}
		WorkspaceResponse workspaceResponse = modelMapper.map(workspaceElastic, WorkspaceResponse.class);
		List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspaceId);
		List<UserResponse> userResponses = new ArrayList<>();
		userWorkspaces.forEach(userWorkspace -> {
			ApiResponse<UserResponse> userApiResponse = getUserById(userWorkspace.getUserId());
			if (userApiResponse != null && userApiResponse.getPayload() != null) {
				UserResponse userResponse = modelMapper.map(userApiResponse.getPayload(), UserResponse.class);
				userResponse.setIsAdmin(userWorkspace.getIsAdmin());
				userResponses.add(userResponse);
			}
		});
		workspaceResponse.setUsers(userResponses);
		return workspaceResponse;
	}

	@Override
	public Void updateStatusWorkspace(UUID workspaceId, Boolean isPrivate) {
		WorkspaceElastic existElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), workspaceId);
		if (userWorkspace.isEmpty()) {
			throw new ForbiddenException("You don't have permission to access this workspace!");
		}
		if (!userWorkspace.get().getIsAdmin()) {
			throw new ForbiddenException("You are collaborator allowed to update this workspace!");
		}
		existElastic.setIsPrivate(isPrivate);
		workspaceElasticRepository.save(existElastic);
		Workspace existWorkspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		existWorkspace.setIsPrivate(isPrivate);
		workspaceRepository.save(existWorkspace);
		return null;
	}
}
