package xyz.fieldwire.projectmanager.service.exception;

public class FloorPlanNotFoundException extends RuntimeException {
    public FloorPlanNotFoundException(Long floorPlanId) {
        super("Floor plan " + floorPlanId + " not found.");
    }
}
