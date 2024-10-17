package org.example.workspaceservice.service.serviceimp;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.DocumentClient;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.Workspace;
import org.example.workspaceservice.model.request.WorkspaceRequest;
import org.example.workspaceservice.model.response.*;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.WorkspaceService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class WorkspaceServiceImp implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final ModelMapper modelMapper;
    private final DocumentClient documentClient;
    private final UserClient userClient;
    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @Override
    public WorkspaceResponse createWorkspace(WorkspaceRequest workspaceRequest) {
        Workspace workspace = modelMapper.map(workspaceRequest, Workspace.class);
        workspace.setCreatedAt(LocalDateTime.now());
        workspace.setUpdatedAt(LocalDateTime.now());
        workspace = workspaceRepository.save(workspace);
        UserWorkspace userWorkspace = modelMapper.map(workspace, UserWorkspace.class);
        userWorkspace.setUserId(UUID.fromString(getCurrentUser()));
        userWorkspace.setIsAdmin(true);
        userWorkspaceRepository.save(userWorkspace);
        return modelMapper.map(workspace, WorkspaceResponse.class);
    }

    @Override
    public List<WorkspaceResponse> getAllWorkspace() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        List<WorkspaceResponse> workspaceResponses = new ArrayList<>();
        workspaces.forEach(workspace -> {
            WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
            List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspace.getWorkspaceId());
            List<UserResponse> userResponses = new ArrayList<>();
            userWorkspaces.forEach(userWorkspace -> {
                ApiResponse<UserResponse> userApiResponse = userClient.getUserById(userWorkspace.getUserId());
                if (userApiResponse != null && userApiResponse.getPayload() != null) {
                    UserResponse userResponse = modelMapper.map(userApiResponse.getPayload(), UserResponse.class);
                    userResponse.setIsAdmin(userWorkspace.getIsAdmin());
                    userResponses.add(userResponse);
                }
            });
            workspaceResponse.setUsers(userResponses);
            workspaceResponses.add(workspaceResponse);
        });
        return workspaceResponses;
    }


    @Override
    public WorkspaceResponse updateWorkspace(UUID workspaceId, WorkspaceRequest workspaceRequest) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), workspaceId);
        if (userWorkspace.isPresent()) {
            if (!userWorkspace.get().getIsAdmin()) {
                throw new ForbiddenException("Not allowed to update workspace");
            }
        }
        modelMapper.map(workspaceRequest, workspace);
        workspace.setUpdatedAt(LocalDateTime.now());
        workspace = workspaceRepository.save(workspace);
        return modelMapper.map(workspace, WorkspaceResponse.class);
    }

    @Transactional
    @Override
    public Void deleteWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()), workspaceId);
        if (userWorkspace.isPresent()) {
            if (!userWorkspace.get().getIsAdmin()) {
                throw new ForbiddenException("Not allowed to delete workspace");
            }
        }
        workspaceRepository.delete(workspace);
        userWorkspaceRepository.deleteByWorkspaceId(workspaceId);
        documentClient.deleteDocumentByWorkspaceId(workspaceId);
        return null;
    }

    @Override
    public WorkspaceResponse getWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
        WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspaceId);
        List<UserResponse> userResponses = new ArrayList<>();
        userWorkspaces.forEach(userWorkspace -> {
            ApiResponse<UserResponse> userApiResponse = userClient.getUserById(userWorkspace.getUserId());
            if (userApiResponse != null && userApiResponse.getPayload() != null) {
                UserResponse userResponse = modelMapper.map(userApiResponse.getPayload(), UserResponse.class);
                userResponse.setIsAdmin(userWorkspace.getIsAdmin());
                userResponses.add(userResponse);
            }
        });
        workspaceResponse.setUsers(userResponses);
        return workspaceResponse;
    }

}
