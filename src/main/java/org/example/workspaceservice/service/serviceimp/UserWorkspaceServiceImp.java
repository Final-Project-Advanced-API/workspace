package org.example.workspaceservice.service.serviceimp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.workspaceservice.Client.UserClient;
import org.example.workspaceservice.exception.ConflictException;
import org.example.workspaceservice.exception.NotFoundException;
import org.example.workspaceservice.model.entity.UserWorkspace;
import org.example.workspaceservice.model.request.AcceptRequest;
import org.example.workspaceservice.model.request.RemoveUserRequest;
import org.example.workspaceservice.model.request.UserWorkspaceRequest;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.UserResponse;
import org.example.workspaceservice.repository.UserWorkspaceRepository;
import org.example.workspaceservice.repository.WorkspaceRepository;
import org.example.workspaceservice.service.MailSenderService;
import org.example.workspaceservice.service.UserWorkspaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserWorkspaceServiceImp implements UserWorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserClient userClient;
    private final MailSenderService mailSenderService;


    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @SneakyThrows
    @Override
    public UserWorkspace inviteCollaboratorIntoWorkspace(UserWorkspaceRequest userWorkspaceRequest) {
        ApiResponse<UserResponse> user = userClient.getUserByEmail(userWorkspaceRequest.getEmail());
        if (user==null){
            throw new NotFoundException("User email not found");
        }
        Optional<UserWorkspace> existUser = userWorkspaceRepository.findByUserIdAndWorkspaceId(user.getPayload().getUserId(), userWorkspaceRequest.getWorkspaceId());
        if (existUser.isPresent()) {
            throw new ConflictException("User email already join exists");
        }
        workspaceRepository.findById(userWorkspaceRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace id " + userWorkspaceRequest.getWorkspaceId() + " not found"));
        mailSenderService.sendMail(user.getPayload().getEmail(), userWorkspaceRequest.getWorkspaceId().toString(),false ); // Pass from email as string
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUserId(user.getPayload().getUserId());
        userWorkspace.setWorkspaceId(userWorkspaceRequest.getWorkspaceId());
        userWorkspace.setIsAdmin(false);
        userWorkspace.setIsAccept(false);
        userWorkspaceRepository.save(userWorkspace);
        return userWorkspace;
    }
    @Override
    public UserWorkspace acceptToJoinWorkspace(String email, UUID workspaceId, Boolean isAccept) {
        ApiResponse<UserResponse> user = userClient.getUserByEmail(email);
        if (user.getPayload().getEmail().isEmpty()) {
            throw new NotFoundException("User email "+email+" not found");
        }
        Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(user.getPayload().getUserId(), workspaceId);
        if (userWorkspace.isPresent()) {
            userWorkspace.get().setIsAccept(isAccept);
            userWorkspaceRepository.save(userWorkspace.get());
        }
        return userWorkspace.orElse(null);
    }

    @Transactional
    @Override
    public Void deleteCollaboratorFromWorkspace(RemoveUserRequest removeUserRequest) {
        userWorkspaceRepository.findByUserId(removeUserRequest.getUserId()).orElseThrow(() -> new NotFoundException("User id " + removeUserRequest.getWorkspaceId() + " not found"));
        workspaceRepository.findById(removeUserRequest.getWorkspaceId()).orElseThrow(() -> new NotFoundException("Workspace id " + removeUserRequest.getWorkspaceId() + " not found"));
        userWorkspaceRepository.deleteByUserIdAndWorkspaceId(removeUserRequest.getUserId(), removeUserRequest.getWorkspaceId());
        return null;
    }

}
