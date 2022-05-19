package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PostFloorPlanRequest extends CommonRequest {
    private final Long projectId;
    private String name;
    private MultipartFile file;

    @Builder
    private PostFloorPlanRequest(Long projectId, String name, MultipartFile file) {
        this.projectId = projectId;
        this.file = file;
        if (Objects.nonNull(name)) {
            this.name = name;
        } else if (Objects.nonNull(file)) {
            this.name = this.file.getOriginalFilename();
        }
    }
}
