package it.sal.disco.unimib.charityhub.data.repositories.project;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectCallback;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public class ProjectRepository implements IProjectRepository, ProjectCallback {


    private  BaseProjectDataSource projectDataSource;
    private  MutableLiveData<Result> projectsLiveData;
    private  MutableLiveData<Result> themesLiveData;
    private  MutableLiveData<Result> imagesLiveData;
    private  BaseProjectLocalDataSource projectLocalDataSource;


    public ProjectRepository(BaseProjectDataSource projectDataSource, BaseProjectLocalDataSource localDataSource) {
        this.projectDataSource = projectDataSource;
        this.projectLocalDataSource = localDataSource;
        projectLocalDataSource.setProjectCallback(this);
        projectDataSource.setProjectCallback(this);
        imagesLiveData = new MutableLiveData<>();
        projectsLiveData = new MutableLiveData<>();
        themesLiveData = new MutableLiveData<>();
    }

    public void setProjectLocalDataSource(ProjectLocalDataSource projectLocalDataSource) {
        this.projectLocalDataSource = projectLocalDataSource;
    }

    public void setProjectDataSource(ProjectDataSource projectDataSource) {
        this.projectDataSource = projectDataSource;
    }

    public MutableLiveData<Result> searchForProjects(String filter, Integer nextProjectId, boolean isConnected, String country) {
        projectLocalDataSource.getProjectsByCountry(country);
        return projectsLiveData;
    }

    public void getProjectsByTheme(String themeName, String country) {
        projectLocalDataSource.getProjectsByTheme(themeName, country);
    }

    public void getProjectsByCountry(String country) {
        projectLocalDataSource.getProjectsByCountry(country);
    }

    public void searchProjects(String filter, Integer nextProjectId) {
        Log.e("PROJECT REPOSITORY", "REPO CI SONO");
        projectDataSource.searchForProjects(filter, nextProjectId);
    }

    public MutableLiveData<Result> getDownloadedProjects() {
        projectLocalDataSource.getProjects();
        return projectsLiveData;
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
        //Result.ProjectResponseSuccess result = new Result.ProjectResponseSuccess(projects);
        //projectsLiveData.postValue(result);
        projectLocalDataSource.insertProjects(projects);
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

    @Override
    public void onLocalSuccess(ProjectsApiResponse projectsApiResponse) {
        Result.ProjectResponseSuccess result = new Result.ProjectResponseSuccess(projectsApiResponse);
        projectsLiveData.postValue(result);
    }

    @Override
    public void onProjectsLocalSuccess(List<Project> projectList) {
        Result.ProjectResponseSuccess result = new Result.ProjectResponseSuccess(projectList);
        projectsLiveData.postValue(result);
    }

    public void setProjectsLiveData(MutableLiveData<Result> projectsLiveData) {
        this.projectsLiveData = projectsLiveData;
    }

    public void setThemesLiveData(MutableLiveData<Result> themesLiveData) {
        this.themesLiveData = themesLiveData;
    }

    public void setImagesLiveData(MutableLiveData<Result> imagesLiveData) {
        this.imagesLiveData = imagesLiveData;
    }

}
