package org.example.workspaceservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.workspaceservice.model.request.WorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@SecurityRequirement(name = "stack-notes")
@CrossOrigin
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    @Operation(summary = "create workspace")
    public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceRequest workspaceRequest) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Create workspace successfully")
                .payload(workspaceService.createWorkspace(workspaceRequest))
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "get all workspace")
    public ResponseEntity<?> getAllWorkspace() {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Get all workspace successfully")
                .payload(workspaceService.getAllWorkspace())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    @Operation(summary = "get workspace")
    public ResponseEntity<?> getWorkspace(@PathVariable UUID workspaceId) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Get workspace successfully")
                .payload(workspaceService.getWorkspace(workspaceId))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{workspaceId}")
    @Operation(summary = "update workspace")
    public ResponseEntity<?> updateWorkspace(@PathVariable UUID workspaceId,@RequestBody WorkspaceRequest workspaceRequest) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Update workspace successfully")
                .payload(workspaceService.updateWorkspace(workspaceId,workspaceRequest))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}")
    @Operation(summary = "delete workspace")
    public ResponseEntity<?> deleteWorkspace(@PathVariable UUID workspaceId) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Delete workspace successfully")
                .payload(workspaceService.deleteWorkspace(workspaceId))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
