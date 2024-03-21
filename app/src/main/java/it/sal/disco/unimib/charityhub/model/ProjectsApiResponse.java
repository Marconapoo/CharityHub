package it.sal.disco.unimib.charityhub.model;

import java.util.List;

public class ProjectsApiResponse {


    private ProjectData projects;

    public ProjectData getProjects() {
        return projects;
    }

    public static class ProjectData {
        private List<Project> project;

        public List<Project> getProject() {
            return project;
        }
    }


}
