package xyz.fieldwire.projectmanager.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import xyz.fieldwire.projectmanager.model.entity.ProjectEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ProjectDto extends CommonDto {
    @Builder.Default
    Set<FloorPlanDto> floorPlans;

    private ProjectDto(Long id, String name, Set<FloorPlanDto> floorPlans) {
        super(id, name);
        this.floorPlans = floorPlans;
    }

    @Builder
    private ProjectDto(ProjectEntity entity) {
        super(entity.getId(), entity.getName());
        BeanUtils.copyProperties(entity, this);
        this.floorPlans = entity.getFloorPlans()
                .stream().map(planEntity ->
                        FloorPlanDto.builder()
                                .entity(planEntity)
                                .build())
                .collect(Collectors.toSet());
    }
}
