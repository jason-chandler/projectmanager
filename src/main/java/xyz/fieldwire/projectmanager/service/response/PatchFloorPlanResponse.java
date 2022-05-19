package xyz.fieldwire.projectmanager.service.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PatchFloorPlanResponse extends CommonResponse {
    private final Long id;

    @Builder
    private PatchFloorPlanResponse(Long id, Boolean success, String message) {
        super(success, message);
        this.id = id;
    }
}
