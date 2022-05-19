package xyz.fieldwire.projectmanager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fieldwire.projectmanager.dto.FloorPlanDto;
import xyz.fieldwire.projectmanager.model.entity.FloorPlanEntity;
import xyz.fieldwire.projectmanager.model.repository.FloorPlanRepository;
import xyz.fieldwire.projectmanager.service.request.DeleteFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.GetFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PatchFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PostFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.response.DeleteFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.GetFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PatchFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PostFloorPlanResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FloorPlanService {
    private FloorPlanRepository floorPlanRepository;
    public GetFloorPlanResponse getFloorPlan(GetFloorPlanRequest request) {
        Page<FloorPlanEntity> resultPage = floorPlanRepository.findAll(PageRequest.of(request.getPageNumber(), request.getPageSize()));

        List<FloorPlanDto> results = resultPage.stream()
                .map(result -> FloorPlanDto.builder()
                        .entity(result)
                        .build())
                .collect(Collectors.toList());
        return GetFloorPlanResponse.builder()
                .results(results)
                .build();
    }

    @Transactional
    public PostFloorPlanResponse postFloorPlan(PostFloorPlanRequest request) {
        return PostFloorPlanResponse.builder().build();
    }

    @Transactional
    public PatchFloorPlanResponse patchFloorPlan(PatchFloorPlanRequest request) {
        return PatchFloorPlanResponse.builder().build();
    }

    @Transactional
    public DeleteFloorPlanResponse deleteFloorPlan(DeleteFloorPlanRequest request) {
        return DeleteFloorPlanResponse.builder().build();
    }
}
