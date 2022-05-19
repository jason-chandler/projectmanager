package xyz.fieldwire.projectmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.fieldwire.projectmanager.service.ProjectService;
import xyz.fieldwire.projectmanager.service.request.DeleteProjectRequest;
import xyz.fieldwire.projectmanager.service.request.GetProjectRequest;
import xyz.fieldwire.projectmanager.service.request.PatchProjectRequest;
import xyz.fieldwire.projectmanager.service.request.PostProjectRequest;
import xyz.fieldwire.projectmanager.service.response.DeleteProjectResponse;
import xyz.fieldwire.projectmanager.service.response.GetProjectResponse;
import xyz.fieldwire.projectmanager.service.response.PatchProjectResponse;
import xyz.fieldwire.projectmanager.service.response.PostProjectResponse;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public GetProjectResponse getProjectCollection(@RequestParam(required = false) final Long id, @RequestParam(required = false) @PositiveOrZero final Integer pageNumber,
                                         @RequestParam(required = false) @Positive final Integer pageSize) {
        return projectService.getCollection(GetProjectRequest.builder().id(id).pageNumber(pageNumber).pageSize(pageSize).build());
    }

    @GetMapping("/{id}")
    public GetProjectResponse getProjectById(@PathVariable final Long id) {
        return projectService.getById(GetProjectRequest.builder().id(id).build());
    }

    @PostMapping
    public PostProjectResponse postProject(@RequestParam final String name) {
        return projectService.postProject(PostProjectRequest.builder().name(name).build());
    }

    @PatchMapping("/{id}")
    public PatchProjectResponse patchProject(@PathVariable final Long id, @RequestParam final String name) {
        return projectService.patchProject(PatchProjectRequest.builder().id(id).name(name).build());
    }

    @DeleteMapping("/{id}")
    public DeleteProjectResponse deleteProject(@PathVariable final Long id) throws IOException {
        return projectService.deleteProject(DeleteProjectRequest.builder().id(id).build());
    }
}
