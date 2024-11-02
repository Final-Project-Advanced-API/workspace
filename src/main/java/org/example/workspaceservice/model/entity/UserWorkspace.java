package org.example.workspaceservice.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Boolean isAdmin;
    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonBackReference
    private Workspace workspace;
}
