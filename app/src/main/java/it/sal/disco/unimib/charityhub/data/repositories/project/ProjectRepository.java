package it.sal.disco.unimib.charityhub.data.repositories.project;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectCallback;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectDataSource;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;

public class ProjectRepository implements IProjectRepository, ProjectCallback {


    private final BaseProjectDataSource projectDataSource;
    private final MutableLiveData<Result> projectsLiveData;

    public ProjectRepository() {
        projectDataSource = new ProjectDataSource();
        projectDataSource.setProjectCallback(this);
        projectsLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<Result> getProjectsLiveData(String themeId, Integer nextProjectId) {
        projectDataSource.getProjectsByTheme(themeId, nextProjectId);
        return projectsLiveData;
    }

    @Override
    public void onProjectsLoaded(ProjectsApiResponse projects) {
        Result.ProjectResponseSuccess result = new Result.ProjectResponseSuccess(projects);
        projectsLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemote(String error) {
        Result.Error result = new Result.Error(error);
        projectsLiveData.postValue(result);
    }
}
