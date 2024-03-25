package it.sal.disco.unimib.charityhub.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectsApiResponse {



    private Search search;

    public Search getSearch() {
        return search;
    }


    public static class Search {
        private Response response;

        public Response getResponse() {
            return response;
        }
    }

    public static class Response {
        @SerializedName("projects")
        private ProjectData projectData;

        private int numberFound;

        public int getNumberFound() {
            return numberFound;
        }

        public ProjectData getProjectData() {
            return projectData;
        }
    }

    public static class ProjectData {
        @SerializedName("project")
        private List<Project> projectList;

        public List<Project> getProjectList() {
            return projectList;
        }
    }


}
