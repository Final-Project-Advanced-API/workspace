package org.example.workspaceservice.service.serviceimp;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.Client.DocumentClient;
import org.example.workspaceservice.exception.ForbiddenException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.entity.Workspace;
import org.example.workspaceservice.model.request.WorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.DocumentResponse;
import org.example.workspaceservice.model.response.UserWorkspaceResponse;
import org.example.workspaceservice.model.response.WorkspaceResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.WorkspaceService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

    public String getCurrentUser(){
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
        userWorkspace.setIsAccept(true);
        userWorkspaceRepository.save(userWorkspace);
        return modelMapper.map(workspace, WorkspaceResponse.class);
    }

    @Override
    public List<WorkspaceResponse> getAllWorkspace() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        List<WorkspaceResponse> workspaceResponses = new ArrayList<>();
        for (Workspace workspace : workspaces) {
            WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
            List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspace.getWorkspaceId());
            List<UserWorkspaceResponse> userWorkspaceResponses = userWorkspaces.stream()
                    .map(userWorkspace -> modelMapper.map(userWorkspace, UserWorkspaceResponse.class))
                    .collect(Collectors.toList());
            workspaceResponse.setUsers(userWorkspaceResponses);
            ApiResponse<List<DocumentResponse>> lstDocument;
            try {
                lstDocument = documentClient.getAllDocumentByWorkspaceId(workspace.getWorkspaceId());
            } catch (FeignException e) {
                throw new RuntimeException("Error fetching documents for workspace: " + e.getMessage(), e);
            }
            workspaceResponse.setDocuments(lstDocument.getPayload());
            workspaceResponses.add(workspaceResponse);
        }
        return workspaceResponses;
    }


    @Override
    public WorkspaceResponse updateWorkspace(UUID workspaceId, WorkspaceRequest workspaceRequest) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id "+ workspaceId +" not found"));
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()),workspaceId);
        if(userWorkspace.isPresent()){
            if(!userWorkspace.get().getIsAdmin()){
                throw new ForbiddenException("Not allowed to update workspace");
            }
        }
        modelMapper.map(workspaceRequest, workspace);
        workspace.setUpdatedAt(LocalDateTime.now());
        workspace = workspaceRepository.save(workspace);
        return modelMapper.map(workspace, WorkspaceResponse.class);
    }

    @Override
    public Void deleteWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace id "+ workspaceId +" not found"));
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(UUID.fromString(getCurrentUser()),workspaceId);
        if(userWorkspace.isPresent()){
            if(!userWorkspace.get().getIsAdmin()){
                throw new ForbiddenException("Not allowed to delete workspace");
            }
        }
        ApiResponse<List<DocumentResponse>> lstDocument = documentClient.getAllDocumentByWorkspaceId(workspaceId);
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspace.getWorkspaceId());
        workspaceRepository.delete(workspace);
        userWorkspaceRepository.deleteAll(userWorkspaces);
        for(DocumentResponse documentResponse: lstDocument.getPayload()){
            documentClient.deleteDocument(documentResponse.getDocumentId());
        }
        return null;
    }

    @Override
    public WorkspaceResponse getWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace id " + workspaceId + " not found"));
        ApiResponse<List<DocumentResponse>> lstDocument;
        try {
            // Attempt to fetch documents associated with the workspace
            lstDocument = documentClient.getAllDocumentByWorkspaceId(workspaceId);
        } catch (FeignException e) {
            // Handle Feign exceptions, such as connection refused or not found
            throw new RuntimeException("Error fetching documents for workspace: " + e.getMessage(), e);
        }
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByWorkspaceId(workspace.getWorkspaceId());
        WorkspaceResponse workspaceResponse = modelMapper.map(workspace, WorkspaceResponse.class);
        List<UserWorkspaceResponse> userWorkspaceResponses = userWorkspaces.stream()
                .map(userWorkspace -> modelMapper.map(userWorkspace, UserWorkspaceResponse.class))
                .collect(Collectors.toList());
        workspaceResponse.setUsers(userWorkspaceResponses);
        workspaceResponse.setDocuments(lstDocument.getPayload());
        return workspaceResponse;
    }
}
