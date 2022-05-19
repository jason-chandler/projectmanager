package xyz.fieldwire.projectmanager.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long id;

    private String name;
    @CreationTimestamp
    private Timestamp createdOn;
    @UpdateTimestamp
    private Timestamp modifiedOn;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<FloorPlanEntity> floorPlans = new HashSet<>();

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
