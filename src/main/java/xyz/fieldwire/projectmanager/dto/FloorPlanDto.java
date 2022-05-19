package xyz.fieldwire.projectmanager.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import xyz.fieldwire.projectmanager.model.entity.FloorPlanEntity;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FloorPlanDto extends CommonDto {
    private final Long projectId;
    private Timestamp createdOn;
    private Timestamp modifiedOn;

    @Builder
    private FloorPlanDto(FloorPlanEntity entity) {
        super(entity.getId(), entity.getName());
        BeanUtils.copyProperties(entity, this);
        this.projectId = entity.getProject().getId();
    }

}
