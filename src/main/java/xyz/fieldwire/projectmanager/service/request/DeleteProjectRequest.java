package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DeleteProjectRequest extends CommonRequest {
    private final Long id;

    @Builder
    private DeleteProjectRequest(Long id) {
        this.id = id;
    }
}
