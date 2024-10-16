package org.example.workspaceservice.Client;
import org.example.workspaceservice.Client.fallback.DocumentClientFallback;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.DocumentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "document-service",url = "http://localhost:8085",fallback = DocumentClientFallback.class)
@Primary
public interface DocumentClient {
    @GetMapping("/api/v1/documents")
    ApiResponse<List<DocumentResponse>> getAllDocument();

    @GetMapping("/api/v1/documents/workspace/{workspaceId}")
    ApiResponse<List<DocumentResponse>> getAllDocumentByWorkspaceId(@PathVariable UUID workspaceId);

    @DeleteMapping("/api/v1/documents/{documentId}")
    void deleteDocument(@PathVariable UUID documentId);
}
