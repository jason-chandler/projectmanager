package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PatchProjectRequest extends CommonRequest {
    private final Long id;
    private final String name;

    @Builder
    private PatchProjectRequest(Integer pageNumber, Integer pageSize, Long id, String name) {
        super(pageNumber, pageSize);
        this.id = id;
        this.name = name;
    }
}
