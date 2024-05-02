package it.sal.disco.unimib.charityhub.model;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.countries.Country;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public abstract class Result {

    private Result() {}

    public boolean isSuccess() {
        return
                this instanceof UserResponseSuccess || this instanceof ProjectResponseSuccess || this instanceof ThemesResponseSuccess || this instanceof CountriesResponseSucccess
                || this instanceof ImagesResponseSuccess;
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getUser() {
            return user;
        }
    }

    public static final class ProjectResponseSuccess extends Result {

        private  ProjectsApiResponse projectsApiResponse;
        private  List<Project> projectList;

        public ProjectResponseSuccess(ProjectsApiResponse projectsApiResponse) {
            this.projectsApiResponse = projectsApiResponse;
        }

        public ProjectResponseSuccess(List<Project> projectList) {
            this.projectList = projectList;
        }

        public List<Project> getProjectList() {
            return projectList;
        }

        public ProjectsApiResponse getProjectsApiResponse() {
            return projectsApiResponse;
        }
    }


    public static final class ThemesResponseSuccess extends Result {
        private final ThemesApiResponse themesApiResponse;

        public ThemesResponseSuccess(ThemesApiResponse themesApiResponse) {
            this.themesApiResponse = themesApiResponse;
        }

        public ThemesApiResponse getThemesApiResponse() {
            return themesApiResponse;
        }
    }

    public static final class CountriesResponseSucccess extends Result {
        private final List<Country> countriesResponse;

        public CountriesResponseSucccess(List<Country> countriesApiResponse) {
            this.countriesResponse = countriesApiResponse;
        }
        public List<Country> getCountriesResponse() {
            return countriesResponse;
        }
    }

    public static final class ImagesResponseSuccess extends Result {
        private final ImagesApiResponse imagesApiResponse;

        public ImagesResponseSuccess(ImagesApiResponse imagesApiResponse) {
            this.imagesApiResponse = imagesApiResponse;
        }

        public ImagesApiResponse getImagesApiResponse() {
            return imagesApiResponse;
        }
    }
    public static final class Error extends Result {
        private final String errorMessage;

        public Error(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

}
