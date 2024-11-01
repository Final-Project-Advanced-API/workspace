package org.example.workspaceservice.model.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
    @Column(nullable = false,length = 255)
    private String workspaceName;
    @Column(nullable = false)
    private Boolean isPrivate;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
