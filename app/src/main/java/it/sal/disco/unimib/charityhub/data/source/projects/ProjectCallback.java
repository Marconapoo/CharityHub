package it.sal.disco.unimib.charityhub.data.source.projects;

import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;

public interface ProjectCallback {

    void onProjectsLoaded(ProjectsApiResponse projects);

    void onFailureFromRemote(String error);

}
