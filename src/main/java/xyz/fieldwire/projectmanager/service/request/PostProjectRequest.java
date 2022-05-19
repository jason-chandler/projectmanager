package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PostProjectRequest extends CommonRequest {
    private final String name;

    @Builder
    private PostProjectRequest(String name) {
        this.name = name;
    }
}
