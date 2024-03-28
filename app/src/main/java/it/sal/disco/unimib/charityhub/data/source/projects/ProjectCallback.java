package it.sal.disco.unimib.charityhub.data.source.projects;

import it.sal.disco.unimib.charityhub.model.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;

public interface ProjectCallback {

    void onProjectsLoaded(ProjectsApiResponse projects);

    void onFailureFromRemote(String error);

    void onThemesLoaded(ThemesApiResponse themes);

    void onFailureThemesLoaded(String error);

    void onSuccessImagesLoaded(ImagesApiResponse body);

    void onFailureImagesLoaded(String noImagesWereFound);
}
