package xyz.fieldwire.projectmanager.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import xyz.fieldwire.projectmanager.model.entity.FloorPlanEntity;

@Repository
public interface FloorPlanRepository extends PagingAndSortingRepository<FloorPlanEntity, Long> {
}
