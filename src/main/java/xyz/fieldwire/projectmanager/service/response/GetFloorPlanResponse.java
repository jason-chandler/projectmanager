package xyz.fieldwire.projectmanager.service.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.fieldwire.projectmanager.dto.FloorPlanDto;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GetFloorPlanResponse extends CommonResponse {
    List<FloorPlanDto> results;

    @Builder
    private GetFloorPlanResponse(Boolean success, String message, List<FloorPlanDto> results) {
        super(success, message);
        this.results = results;
    }
}
