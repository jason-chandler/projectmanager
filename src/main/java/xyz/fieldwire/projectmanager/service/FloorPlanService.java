package xyz.fieldwire.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    private void modifyFloorPlan(FloorPlanEntity floorPlan, Long projectId, String name) throws ProjectNotFoundException {
        if(projectId != null) {
            floorPlan.setProject(projectRepository
                    .findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException(projectId)));
        }
        if(name != null) {
            floorPlan.setName(name);
        }
    }
    @Transactional
    public PatchFloorPlanResponse patchFloorPlan(PatchFloorPlanRequest request) throws FileSystemNotFoundException, IOException, ProjectNotFoundException {
        Long id = request.getId();
        Long projectId = request.getProjectId();
        String name = request.getName();
        MultipartFile file = request.getFile();
        FloorPlanEntity floorPlan = floorPlanRepository.findById(id).orElseThrow(() -> new FloorPlanNotFoundException(id));
        modifyFloorPlan(floorPlan, projectId, name);
        if(file != null || name != null) {
            String path = floorPlan.getPath();
            InputStream inputStream = Objects.nonNull(file) ? file.getInputStream() : new ByteArrayInputStream(imageService.getImageIfExists(path));
            if(name != null) {
                deleteFloorPlansFromDisk(path);
            }
            writeFloorPlansToDisk(inputStream, id, floorPlan.getName(), Boolean.TRUE);
        }
        finalizeFloorPlan(floorPlan);
        return PatchFloorPlanResponse.builder().id(id).build();
    }

    @Transactional
    public Boolean cascadeDeleteFloorPlans(Set<FloorPlanEntity> floorPlans) throws IOException {
        for (FloorPlanEntity floorPlan : floorPlans) {
            deleteFloorPlansFromDisk(floorPlan.getPath());
            floorPlanRepository.delete(floorPlan);
        }
        return Boolean.TRUE;
    }

    @Transactional
    public DeleteFloorPlanResponse deleteFloorPlan(DeleteFloorPlanRequest request) throws IOException, FloorPlanNotFoundException {
        FloorPlanEntity floorPlan = floorPlanRepository.findById(request.getId()).orElseThrow(() -> new FloorPlanNotFoundException(request.getId()));
        deleteFloorPlansFromDisk(floorPlan.getPath());
        floorPlanRepository.delete(floorPlan);
        return DeleteFloorPlanResponse.builder().id(request.getId()).build();
    }

    private void deleteFloorPlansFromDisk(String path) throws IOException {
        imageService.deleteImageIfExists(path);
        imageService.deleteImageIfExists(path + THUMB_POSTFIX);
        imageService.deleteImageIfExists(path + LARGE_POSTFIX);
        imageService.deleteFolderIfExists(path);
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
