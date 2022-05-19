package xyz.fieldwire.projectmanager.service.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project " + projectId + " not found.");
    }

    public ProjectNotFoundException(String name) {
        super("Project with name containing " + name + " not found.");
    }

    public ProjectNotFoundException() {
        super("No projects found.");
    }
}
