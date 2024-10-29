package org.example.workspaceservice.repository;
import org.example.workspaceservice.model.entity.WorkspaceElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.UUID;

public interface WorkspaceElasticRepository extends ElasticsearchRepository<WorkspaceElastic, UUID> {
}
