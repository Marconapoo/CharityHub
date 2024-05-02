package it.sal.disco.unimib.charityhub.data.source.projects;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public interface ProjectCallback {

    void onProjectsLoaded(ProjectsApiResponse projects);

    void onFailureFromRemote(String error);

    void onThemesLoaded(ThemesApiResponse themes);

    void onFailureThemesLoaded(String error);

    void onSuccessImagesLoaded(ImagesApiResponse body);

    void onFailureImagesLoaded(String noImagesWereFound);
    void onLocalSuccess(ProjectsApiResponse projectsApiResponse);
    void onProjectsLocalSuccess(List<Project> projectList);
}
