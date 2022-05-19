package xyz.fieldwire.projectmanager.service.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project " + projectId + " not found.");
    }
}
