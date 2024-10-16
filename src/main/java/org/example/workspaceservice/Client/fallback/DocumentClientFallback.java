package org.example.workspaceservice.Client.fallback;

import org.example.workspaceservice.Client.DocumentClient;
import org.example.workspaceservice.model.response.ApiResponse;
import org.example.workspaceservice.model.response.DocumentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class DocumentClientFallback implements DocumentClient {

    @Override
    public ApiResponse<List<DocumentResponse>> getAllDocument() {
        return ApiResponse.<List<DocumentResponse>>builder()
                .message("Failed to fetch documents")
                .payload(Collections.emptyList())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @Override
    public ApiResponse<List<DocumentResponse>> getAllDocumentByWorkspaceId(UUID workspaceId) {
        return ApiResponse.<List<DocumentResponse>>builder()
                .message("Failed to fetch documents for workspace")
                .payload(Collections.emptyList())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @Override
    public void deleteDocument(UUID documentId) {
        throw new RuntimeException("Failed to delete document");
    }
}
