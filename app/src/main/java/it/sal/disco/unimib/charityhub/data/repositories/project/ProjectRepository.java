package it.sal.disco.unimib.charityhub.data.repositories.project;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectCallback;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public class ProjectRepository implements IProjectRepository, ProjectCallback {


    private final BaseProjectDataSource projectDataSource;
    private final MutableLiveData<Result> projectsLiveData;
    private final MutableLiveData<Result> themesLiveData;
    private final MutableLiveData<Result> imagesLiveData;
    private final BaseProjectLocalDataSource projectLocalDataSource;


    public ProjectRepository(Application application) {
        projectDataSource = new ProjectDataSource();
        projectLocalDataSource = new ProjectLocalDataSource(application);
        projectLocalDataSource.setProjectCallback(this);
        projectDataSource.setProjectCallback(this);
        imagesLiveData = new MutableLiveData<>();
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
    public MutableLiveData<Result> getImagesLiveData(String projectId) {
        projectDataSource.getImages(projectId);
        return imagesLiveData;
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

    @Override
    public void onSuccessImagesLoaded(ImagesApiResponse body) {
        Result.ImagesResponseSuccess result = new Result.ImagesResponseSuccess(body);
        imagesLiveData.postValue(result);
    }

    @Override
    public void onFailureImagesLoaded(String noImagesWereFound) {
        Result.Error result = new Result.Error(noImagesWereFound);
        imagesLiveData.postValue(result);
    }
}
