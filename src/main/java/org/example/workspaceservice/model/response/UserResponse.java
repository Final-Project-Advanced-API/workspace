package org.example.workspaceservice.model.response;
import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private String userId;
    private String username;
    private String fullName;
    private String gender;
    private String dob;
    private String email;
    private String profile;
    private String bio;
    private Boolean isAdmin;
    private String createdDate;
    private String updatedDate;
}
