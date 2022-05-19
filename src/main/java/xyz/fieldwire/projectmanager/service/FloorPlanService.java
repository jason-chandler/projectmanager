package xyz.fieldwire.projectmanager.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fieldwire.projectmanager.dto.FloorPlanDto;
import xyz.fieldwire.projectmanager.model.entity.FloorPlanEntity;
import xyz.fieldwire.projectmanager.model.entity.ProjectEntity;
import xyz.fieldwire.projectmanager.model.repository.FloorPlanRepository;
import xyz.fieldwire.projectmanager.model.repository.ProjectRepository;
import xyz.fieldwire.projectmanager.service.exception.FloorPlanNotFoundException;
import xyz.fieldwire.projectmanager.service.exception.ProjectNotFoundException;
import xyz.fieldwire.projectmanager.service.request.DeleteFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.GetFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PatchFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.request.PostFloorPlanRequest;
import xyz.fieldwire.projectmanager.service.response.DeleteFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.GetFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PatchFloorPlanResponse;
import xyz.fieldwire.projectmanager.service.response.PostFloorPlanResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FloorPlanService {
    private final ImageService imageService;
    private final ProjectRepository projectRepository;
    private final FloorPlanRepository floorPlanRepository;
    private final static String THUMB_POSTFIX = "_thumb";
    private final static String LARGE_POSTFIX = "_large";
    private final static int THUMB_SIZE = 100;
    private final static int LARGE_SIZE = 2000;

    private byte[] getOriginal(String path) {
        return imageService.getImageIfExists(path);
    }

    private byte[] getThumb(String path) {
        return imageService.getImageIfExists(path + THUMB_POSTFIX);
    }

    private byte[] getLarge(String path) {
        return imageService.getImageIfExists(path + LARGE_POSTFIX);
    }

    public GetFloorPlanResponse getById(GetFloorPlanRequest request) {
        FloorPlanEntity result = floorPlanRepository.findById(request.getId()).orElseThrow(() -> new FloorPlanNotFoundException(request.getId()));
        String path = result.getPath();
        List<FloorPlanDto> results = List.of(FloorPlanDto.builder()
                .entity(result)
                .original(getOriginal(path))
                .thumb(getThumb(path))
                .large(getLarge(path))
                .build());
        return GetFloorPlanResponse.builder().results(results).build();
    }

    public GetFloorPlanResponse getCollection(GetFloorPlanRequest request) {
        Page<FloorPlanEntity> resultPage = floorPlanRepository.findAll(PageRequest.of(request.getPageNumber(), request.getPageSize()));

        // Don't populate images when returning collection
        List<FloorPlanDto> results = resultPage.stream()
                .map(result -> FloorPlanDto.builder()
                        .entity(result)
                        .build())
                .collect(Collectors.toList());
        return GetFloorPlanResponse.builder()
                .results(results)
                .message(results.isEmpty() ? "No floor plans have been created.)" : null)
                .build();
    }

    @Transactional
    public PostFloorPlanResponse postFloorPlan(PostFloorPlanRequest request) throws IOException, ProjectNotFoundException {
        ProjectEntity project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(request.getProjectId()));
        FloorPlanEntity floorPlan = initializeFloorPlan(request, project);
        Long id = floorPlan.getId();
        writeFloorPlansToDisk(request.getFile().getInputStream(), id, floorPlan.getName(), Boolean.FALSE);
        finalizeFloorPlan(floorPlan);
        return PostFloorPlanResponse.builder().id(id).build();
    }

    @Transactional
    public PatchFloorPlanResponse patchFloorPlan(PatchFloorPlanRequest request) {
        return PatchFloorPlanResponse.builder().build();
    }

    @Transactional
    public DeleteFloorPlanResponse deleteFloorPlan(DeleteFloorPlanRequest request) {
        return DeleteFloorPlanResponse.builder().build();
    }

    private void writeFloorPlansToDisk(InputStream fileInput, Long id, String name, Boolean overwrite) throws IOException {
        BufferedImage original = ImageIO.read(fileInput);
        BufferedImage thumb = imageService.resize(original, THUMB_SIZE, THUMB_SIZE);
        BufferedImage large = imageService.resize(original, LARGE_SIZE, LARGE_SIZE);
        imageService.writeImageToDisk(original, id, name, overwrite);
        imageService.writeImageToDisk(thumb, id, name + THUMB_POSTFIX, overwrite);
        imageService.writeImageToDisk(large, id, name + LARGE_POSTFIX, overwrite);
    }

    private String constructRelativePath(FloorPlanEntity floorPlan) {
        return floorPlan.getId() + File.separator + floorPlan.getName();
    }

    private FloorPlanEntity initializeFloorPlan(PostFloorPlanRequest request, ProjectEntity project) {
        FloorPlanEntity floorPlan = new FloorPlanEntity();
        floorPlan.setName(request.getName());
        floorPlan.setProject(project);
        return floorPlanRepository.save(floorPlan);
    }

    private FloorPlanEntity finalizeFloorPlan(FloorPlanEntity floorPlan) {
        floorPlan.setPath(constructRelativePath(floorPlan));
        return floorPlanRepository.save(floorPlan);
    }
}
