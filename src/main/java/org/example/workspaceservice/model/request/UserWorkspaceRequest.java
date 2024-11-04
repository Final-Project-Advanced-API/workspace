package org.example.workspaceservice.model.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkspaceRequest {
	@Email
	@NotBlank
	@NotNull
	@NotEmpty
	@Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Email must follow the format: user@domain.com")
	private String email;
	private UUID workspaceId;
}
