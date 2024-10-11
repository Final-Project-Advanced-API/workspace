package org.example.workspaceservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.example.workspaceservice.model.request.AcceptRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.example.workspaceservice.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/userworkspace")
@AllArgsConstructor
@SecurityRequirement(name = "myauth")
public class UserWorkspaceController {
    private final UserWorkspaceService userWorkspaceService;
    private final WorkspaceService workspaceService;
    @PostMapping
    @Operation(summary = "invite collaborator into workspace")
    public ResponseEntity<?> inviteCollaboratorIntoWorkspace(@RequestBody UserWorkspaceRequest userWorkspaceRequest){
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
    @Operation(summary = "delete collaborator from workspace")
    public ResponseEntity<?> deleteCollaboratorFromWorkspace(@RequestBody RemoveUserRequest removeUserRequest){
        ApiResponse<?> response = ApiResponse.builder()
                .message("Delete collaborator from workspace successfully")
                .payload(userWorkspaceService.deleteCollaboratorFromWorkspace(removeUserRequest))
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept")
    @Operation(summary = "accept to join workspace")
    public ResponseEntity<?> acceptToJoinWorkspace(@RequestParam String email, @RequestParam UUID workspaceId,@RequestParam Boolean isAccept){
        userWorkspaceService.acceptToJoinWorkspace(email,workspaceId,isAccept);
        return ResponseEntity.ok().build();
    }
}