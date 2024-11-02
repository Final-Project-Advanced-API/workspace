package org.example.workspaceservice.service.serviceimp;

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
		Workspace workspace = modelMapper.map(workspaceRequest, Workspace.class);
		workspace.setIsPrivate(true);
		workspace.setCreatedAt(LocalDateTime.now());
		workspace.setUpdatedAt(LocalDateTime.now());
		workspaceRepository.save(workspace);
		// add to user-workspace
		UserWorkspace userWorkspace = new UserWorkspace();
		userWorkspace.setWorkspace(workspace);
		userWorkspace.setUserId(getCurrentUser());
		userWorkspace.setIsAdmin(true);
		userWorkspaceRepository.save(userWorkspace);
		// add to elastic
		WorkspaceResponse workspaceResponse = new WorkspaceResponse();
		List<UserResponse> userResponses = new ArrayList<>();
		WorkspaceElastic elastic = new WorkspaceElastic();
		elastic.setWorkspaceId(workspace.getWorkspaceId());
		elastic.setWorkspaceName(workspace.getWorkspaceName());
		elastic.setIsPrivate(true);
		elastic.setCreatedBy(getCurrentUser());
		elastic.setCreatedAt(LocalDateTime.now());
		elastic.setUpdatedAt(LocalDateTime.now());
		workspaceElasticRepository.save(elastic);
		ApiResponse<UserResponse> user = getUserById(getCurrentUser());
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
			Optional<UserWorkspace> lstUserWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceWorkspaceId(getCurrentUser(), workspace.getWorkspaceId());
			if (lstUserWorkspace.isPresent()) {
				WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
				List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceWorkspaceId(workspace.getWorkspaceId());
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
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found!"));
		if (!workspaceElastic.getCreatedBy().equals(getCurrentUser())) {
			throw new ForbiddenException("You don't have permission to update this workspace!");
		}
		Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found!"));
		modelMapper.map(workspaceRequest, workspace);
		workspace.setUpdatedAt(LocalDateTime.now());
		workspaceRepository.save(workspace);
		modelMapper.map(workspaceRequest, workspaceElastic);
		workspaceElastic.setCreatedAt(workspaceElastic.getCreatedAt());
		workspaceElastic.setUpdatedAt(LocalDateTime.now());
		workspaceElasticRepository.save(workspaceElastic);
		return modelMapper.map(workspaceElastic, WorkspaceResponse.class);
	}

	@Override
	public Void deleteWorkspace(UUID workspaceId) {
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		if (!workspaceElastic.getCreatedBy().equals(getCurrentUser())) {
			throw new ForbiddenException("You don't have permission to delete this workspace!");
		}
		workspaceRepository.deleteById(workspaceId);
		deleteDocumentByWorkspaceId(workspaceId);
		workspaceElasticRepository.delete(workspaceElastic);
		return null;
	}

	@Override
	public WorkspaceResponse getWorkspace(UUID workspaceId) {
		WorkspaceElastic workspaceElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		Optional<UserWorkspace> lstUserWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceWorkspaceId(getCurrentUser(), workspaceId);
		if (lstUserWorkspace.isEmpty()) {
			throw new ForbiddenException("You don't have permission to access this workspace!");
		}
		WorkspaceResponse workspaceResponse = modelMapper.map(workspaceElastic, WorkspaceResponse.class);
		List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceWorkspaceId(workspaceId);
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
	public Void updateStatusWorkspace(UUID workspaceId) {
		WorkspaceElastic existElastic = workspaceElasticRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		if (!existElastic.getCreatedBy().equals(getCurrentUser())) {
			throw new ForbiddenException("You don't have permission to update this workspace!");
		}
		Workspace existWorkspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
		if (Boolean.TRUE.equals(existElastic.getIsPrivate())) {
			existElastic.setIsPrivate(false);
			existWorkspace.setIsPrivate(false);
		} else {
			existElastic.setIsPrivate(true);
			existWorkspace.setIsPrivate(true);
		}
		workspaceElasticRepository.save(existElastic);
		workspaceRepository.save(existWorkspace);
		return null;
	}
}
