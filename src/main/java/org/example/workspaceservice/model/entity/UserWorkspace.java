package org.example.workspaceservice.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userRoleId;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private UUID workspaceId;
    private Boolean isAccept;
    private Boolean isAdmin;

}
