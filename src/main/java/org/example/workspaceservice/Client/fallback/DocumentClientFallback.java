package org.example.workspaceservice.Client.fallback;

import org.example.workspaceservice.Client.DocumentClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DocumentClientFallback implements DocumentClient {
	@Override
	public void deleteDocumentByWorkspaceId(String authorization, UUID workspaceId) {
	}
}
