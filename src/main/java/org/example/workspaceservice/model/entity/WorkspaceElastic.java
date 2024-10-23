package org.example.workspaceservice.model.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.UUID;

@Document(indexName = "workspace")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceElastic {
    @Id
    private UUID workspaceId;
    @Field(type = FieldType.Text)
    private String workspaceName;
    @Field(type = FieldType.Boolean)
    private Boolean isPrivate;
    @CreatedDate
    @Field(type = FieldType.Date)
    private String createdAt;
    @LastModifiedDate
    @Field(type = FieldType.Date)
    private String updatedAt;
}
