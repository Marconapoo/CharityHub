package it.sal.disco.unimib.charityhub.data.repositories.project;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectCallback;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;

public class ProjectRepository implements IProjectRepository, ProjectCallback {


    private final BaseProjectDataSource projectDataSource;
    private final MutableLiveData<Result> projectsLiveData;
    private final MutableLiveData<Result> themesLiveData;
    private final BaseProjectLocalDataSource projectLocalDataSource;

    public ProjectRepository(Application application) {
        projectDataSource = new ProjectDataSource();
        projectLocalDataSource = new ProjectLocalDataSource(application);
        projectLocalDataSource.setProjectCallback(this);
        projectDataSource.setProjectCallback(this);
        projectsLiveData = new MutableLiveData<>();
        themesLiveData = new MutableLiveData<>();
    }



    public MutableLiveData<Result> searchForProjects(String filter, Integer nextProjectId) {
        searchProjects(filter, nextProjectId);
        return  projectsLiveData;
    }

    public void searchProjects(String filter, Integer nextProjectId) {
        projectDataSource.searchForProjects(filter, nextProjectId);
    }


    public MutableLiveData<Result> getThemesLiveData() {
        projectDataSource.getThemes();
        return themesLiveData;
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

    @Override
    public void onThemesLoaded(ThemesApiResponse themes) {
        Result.ThemesResponseSuccess result = new Result.ThemesResponseSuccess(themes);
        themesLiveData.postValue(result);
    }

    @Override
    public void onFailureThemesLoaded(String error) {
        Result.Error result = new Result.Error(error);
        themesLiveData.postValue(result);
    }
}
