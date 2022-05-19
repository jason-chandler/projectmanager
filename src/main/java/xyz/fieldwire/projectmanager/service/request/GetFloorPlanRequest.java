package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GetFloorPlanRequest extends CommonRequest {
    private final Long id;

    @Builder
    private GetFloorPlanRequest(Integer pageNumber, Integer pageSize, Long id) {
        super(pageNumber, pageSize);
        this.id = id;
    }
}
