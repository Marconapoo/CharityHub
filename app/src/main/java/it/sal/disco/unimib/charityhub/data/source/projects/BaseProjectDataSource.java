package it.sal.disco.unimib.charityhub.data.source.projects;

public abstract class BaseProjectDataSource {

    ProjectCallback projectCallback;

    public void setProjectCallback(ProjectCallback projectCallback) {
        this.projectCallback = projectCallback;
    }

    public abstract void searchForProjects(String filter, Integer nextProjectId);

    public abstract void getThemes();
}
