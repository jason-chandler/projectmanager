package xyz.fieldwire.projectmanager.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import xyz.fieldwire.projectmanager.model.entity.FloorPlanEntity;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FloorPlanDto extends CommonDto {
    private final Long projectId;
    private final byte[] original;
    private final byte[] thumb;
    private final byte[] large;

    @Builder
    private FloorPlanDto(FloorPlanEntity entity, byte[] original, byte[] thumb, byte[] large) {
        super(entity.getId(), entity.getName());
        BeanUtils.copyProperties(entity, this);
        this.projectId = entity.getProject().getId();
        this.original = original;
        this.thumb = thumb;
        this.large = large;
    }

}
