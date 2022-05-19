package xyz.fieldwire.projectmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.fieldwire.projectmanager.service.FloorPlanService;
import xyz.fieldwire.projectmanager.service.request.DeleteFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.GetFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PatchFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PostFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.response.DeleteFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.GetFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PatchFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PostFloorPlanResponse;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;

@RestController
@RequestMapping("/floor-plans")
@RequiredArgsConstructor
public class FloorPlanController {
    private final FloorPlanService floorPlanService;

    @GetMapping
    public GetFloorPlanResponse getFloorPlanCollection(@RequestParam(required = false) @PositiveOrZero final Integer pageNumber,
                                             @RequestParam(required = false) @Positive final Integer pageSize) {
        return floorPlanService.getCollection(GetFloorPlanRequest.builder().pageNumber(pageNumber).pageSize(pageSize).build());
    }

    @GetMapping("/{id}")
    public GetFloorPlanResponse getFloorPlanById(@PathVariable final Long id) {
        return floorPlanService.getById(GetFloorPlanRequest.builder().id(id).build());
    }

    @GetMapping(value = "/{id}/images/original", produces = "image/png")
    public byte[] getFloorPlanImageById(@PathVariable final Long id) {
        return floorPlanService.getById(GetFloorPlanRequest.builder().id(id).build()).getResults().get(0).getOriginal();
    }

    @GetMapping(value = "/{id}/images/thumb", produces = "image/png")
    public byte[] getFloorPlanThumbById(@PathVariable final Long id) {
        return floorPlanService.getById(GetFloorPlanRequest.builder().id(id).build()).getResults().get(0).getThumb();
    }

    @GetMapping(value = "/{id}/images/large", produces = "image/png")
    public byte[] getFloorPlanLargeById(@PathVariable final Long id) {
        return floorPlanService.getById(GetFloorPlanRequest.builder().id(id).build()).getResults().get(0).getLarge();
    }

    @PostMapping
    public PostFloorPlanResponse postFloorPlan(@RequestParam @NotNull final Long projectId, @RequestPart @NotNull final MultipartFile file) throws IOException {
        return floorPlanService.postFloorPlan(PostFloorPlanRequest.builder().projectId(projectId).file(file).build());
    }

    @PatchMapping("/{id}")
    public PatchFloorPlanResponse patchFloorPlan(@PathVariable final Long id, @RequestParam(required = false) final String name, @RequestParam(required = false) final Long projectId, @RequestPart(required = false) final MultipartFile file) throws IOException {
        return floorPlanService.patchFloorPlan(PatchFloorPlanRequest.builder().id(id).name(name).projectId(projectId).file(file).build());
    }

    @DeleteMapping("/{id}")
    public DeleteFloorPlanResponse deleteFloorPlan(@PathVariable final Long id) throws IOException {
        return floorPlanService.deleteFloorPlan(DeleteFloorPlanRequest.builder().id(id).build());
    }
}
