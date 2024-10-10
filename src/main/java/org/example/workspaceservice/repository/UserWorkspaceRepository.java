package org.example.workspaceservice.repository;

import org.example.workspaceservice.model.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, UUID> {
    List<UserWorkspace> findByWorkspaceId(UUID workspaceId);
}
