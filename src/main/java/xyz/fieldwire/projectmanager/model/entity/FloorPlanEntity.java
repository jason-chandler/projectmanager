package xyz.fieldwire.projectmanager.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "floor_plan")
@Data
public class FloorPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private long id;

    private String name;
    private String path;
    @CreationTimestamp
    private Timestamp createdOn;
    @UpdateTimestamp
    private Timestamp modifiedOn;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
