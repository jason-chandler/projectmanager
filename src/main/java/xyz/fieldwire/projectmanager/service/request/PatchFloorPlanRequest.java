package xyz.fieldwire.projectmanager.service.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PatchFloorPlanRequest extends CommonRequest {
    private final Long id;
    private final Long projectId;
    private final String name;
    private final MultipartFile file;

    @Builder
    private PatchFloorPlanRequest(Long id, Long projectId, String name, MultipartFile file) {
        this.id = id;
        this.projectId = projectId;
        this.file = file;
        this.name = name;
    }
}
