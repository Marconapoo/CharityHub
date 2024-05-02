package it.sal.disco.unimib.charityhub.data.source.projects;

import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;

public abstract class BaseProjectLocalDataSource {

    protected ProjectCallback projectCallback;

    public void setProjectCallback(ProjectCallback projectCallback) {
        this.projectCallback = projectCallback;
    }

    public abstract void insertProjects(ProjectsApiResponse projectsApiResponse);

    public abstract void getProjectsByCountry(String country);

    public abstract void getProjectsByTheme(String theme, String country);

    public abstract void deleteAll();

    public void getProjects() {
    }
}
