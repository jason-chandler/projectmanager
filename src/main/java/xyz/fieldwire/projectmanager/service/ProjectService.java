package xyz.fieldwire.projectmanager.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fieldwire.projectmanager.dto.ProjectDto;
import xyz.fieldwire.projectmanager.model.entity.ProjectEntity;
import xyz.fieldwire.projectmanager.model.repository.ProjectRepository;
import xyz.fieldwire.projectmanager.service.exception.ProjectNotFoundException;
import xyz.fieldwire.projectmanager.service.request.DeleteProjectRequest;
import xyz.fieldwire.projectmanager.service.request.GetProjectRequest;
import xyz.fieldwire.projectmanager.service.request.PatchProjectRequest;
import xyz.fieldwire.projectmanager.service.request.PostProjectRequest;
import xyz.fieldwire.projectmanager.service.response.DeleteProjectResponse;
import xyz.fieldwire.projectmanager.service.response.GetProjectResponse;
import xyz.fieldwire.projectmanager.service.response.PatchProjectResponse;
import xyz.fieldwire.projectmanager.service.response.PostProjectResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository projectRepository;

    public GetProjectResponse getById(GetProjectRequest request) throws ProjectNotFoundException {
        Long id = request.getId();
        ProjectEntity result = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        List<ProjectDto> results = List.of(ProjectDto.builder().entity(result).build());
        return GetProjectResponse.builder()
                .results(results)
                .build();
    }

    public GetProjectResponse getCollection(GetProjectRequest request) throws ProjectNotFoundException {
        Page<ProjectEntity> resultPage = projectRepository.findAll(PageRequest.of(request.getPageNumber(), request.getPageSize()));
        List<ProjectDto> results = resultPage.stream().map(result ->
                        ProjectDto.builder()
                                .entity(result)
                                .build())
                .collect(Collectors.toList());
        return GetProjectResponse.builder().message(results.isEmpty() ? "No projects have been created." : null).results(results).build();
    }

    @Transactional
    public PostProjectResponse postProject(PostProjectRequest request) {
        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project = projectRepository.save(project);
        return PostProjectResponse.builder().id(project.getId()).build();
    }

    @Transactional
    public PatchProjectResponse patchProject(PatchProjectRequest request) throws ProjectNotFoundException {
        ProjectEntity project = projectRepository.findById(request.getId()).orElseThrow(() -> new ProjectNotFoundException(request.getId()));
        String name = Objects.nonNull(request.getName()) ? request.getName() : project.getName();
        project.setName(name);
        project.setModifiedOn(Timestamp.from(Instant.now()));
        project = projectRepository.save(project);
        return PatchProjectResponse.builder().id(project.getId()).build();
    }

    @Transactional
    public DeleteProjectResponse deleteProject(DeleteProjectRequest request) {
        return DeleteProjectResponse.builder().build();
    }
}
