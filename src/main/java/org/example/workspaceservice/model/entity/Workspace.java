package org.example.workspaceservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID workspaceId;
    @Column(nullable = false)
    private String workspaceName;
    @Column(nullable = false)
    private Boolean isPrivate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
