package org.example.workspaceservice.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userRoleId;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private UUID workspaceId;
    private Boolean isAdmin;

}
