package org.example.workspaceservice.repository;
import org.example.workspaceservice.model.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
