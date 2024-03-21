package it.sal.disco.unimib.charityhub.model;

public abstract class Result {

    private Result() {}

    public boolean isSuccess() {
        return this instanceof UserResponseSuccess || this instanceof ProjectResponseSuccess;
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
