package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.DocumentClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.UUID;
@FeignClient(name = "document-service",url = "${document-service.url}",fallback = DocumentClientFallback.class)
@Primary
public interface DocumentClient {
    @DeleteMapping("/api/v1/documents/workspace/{workspaceId}")
    void deleteDocumentByWorkspaceId(@RequestHeader("Authorization") String authorization, @PathVariable UUID workspaceId);
}
