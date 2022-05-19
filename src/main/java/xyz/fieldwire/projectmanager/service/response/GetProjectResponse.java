package xyz.fieldwire.projectmanager.service.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.fieldwire.projectmanager.dto.ProjectDto;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GetProjectResponse extends CommonResponse {

    List<ProjectDto> results;

    @Builder
    private GetProjectResponse(Boolean success, String message, List<ProjectDto> results) {
        super(success, message);
        this.results = results;
    }
}
