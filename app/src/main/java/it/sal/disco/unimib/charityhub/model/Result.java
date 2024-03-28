package it.sal.disco.unimib.charityhub.model;

import java.util.List;

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

        private final ProjectsApiResponse projectsApiResponse;

        public ProjectResponseSuccess(ProjectsApiResponse projectsApiResponse) {
            this.projectsApiResponse = projectsApiResponse;
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
