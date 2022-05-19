package xyz.fieldwire.projectmanager.service.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DeleteFloorPlanResponse extends CommonResponse {
    private final Long id;

    @Builder
    private DeleteFloorPlanResponse(Long id, Boolean success, String message) {
        super(success, message);
        this.id = id;
    }
}
