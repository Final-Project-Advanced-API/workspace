package org.example.workspaceservice.service;

import org.example.workspaceservice.model.request.WorkspaceRequest;
import org.example.workspaceservice.model.response.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {
    WorkspaceResponse createWorkspace(WorkspaceRequest workspaceRequest);
    List<WorkspaceResponse> getAllWorkspace();
    WorkspaceResponse updateWorkspace(UUID workspaceId,WorkspaceRequest workspaceRequest);
    Void deleteWorkspace(UUID workspaceId);
    WorkspaceResponse getWorkspace(UUID workspaceId);
    Void updateStatusWorkspace(UUID workspaceId, Boolean isPrivate);
}
