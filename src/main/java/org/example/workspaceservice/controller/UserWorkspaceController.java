package org.example.workspaceservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/userworkspaces")
@AllArgsConstructor
@SecurityRequirement(name = "stack-notes")
@CrossOrigin
public class UserWorkspaceController {
    private final UserWorkspaceService userWorkspaceService;

    @PostMapping
    @Operation(summary = "invite collaborator into workspace")
    public ResponseEntity<?> inviteCollaboratorIntoWorkspace(@RequestBody @Valid UserWorkspaceRequest userWorkspaceRequest) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Invite collaborator into workspace successfully")
                .payload(userWorkspaceService.inviteCollaboratorIntoWorkspace(userWorkspaceRequest))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "remove collaborator from workspace")
    public ResponseEntity<?> removeCollaboratorFromWorkspace(@RequestBody @Valid RemoveUserRequest removeUserRequest) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Remove collaborator from workspace successfully")
                .payload(userWorkspaceService.removeCollaboratorFromWorkspace(removeUserRequest))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}