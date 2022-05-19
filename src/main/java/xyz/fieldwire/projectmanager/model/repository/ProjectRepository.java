package xyz.fieldwire.projectmanager.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import xyz.fieldwire.projectmanager.model.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends PagingAndSortingRepository<ProjectEntity, Long> {
}
