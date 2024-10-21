package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.DocumentClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "document-service",url = "http://localhost:8085",configuration = DocumentClientFallback.class,fallback = DocumentClientFallback.class )
@Primary
public interface DocumentClient {
    @DeleteMapping("/api/v1/documents/workspace/{workspaceId}")
    void deleteDocumentByWorkspaceId(@PathVariable UUID workspaceId);
}
