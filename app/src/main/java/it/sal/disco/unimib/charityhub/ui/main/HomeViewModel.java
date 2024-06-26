package it.sal.disco.unimib.charityhub.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.Theme;

public class HomeViewModel extends ViewModel {

    private final ProjectRepository projectRepository;


    private MutableLiveData<Result> projectsLiveData;
    private  MutableLiveData<Result> themesLiveData;



    private boolean isLoading;
    private Theme currentTheme;
    private boolean isFirstLoading;
    private boolean noMoreProjects;
    private int currentResults;
    private int totalResults;

    public HomeViewModel(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;

    }



    public MutableLiveData<Result> searchForProjects(String filter, Integer nextProjectId, boolean isConnected, String country) {
        Log.e("HOME VIEW MODEL", "HOME CI SONO");
        projectsLiveData = projectRepository.searchForProjects(filter, nextProjectId, isConnected, country);
        return projectsLiveData;
    }

    public MutableLiveData<Result> getDownloadedProjects() {
        projectsLiveData = projectRepository.getDownloadedProjects();
        return projectsLiveData;
    }
    public void searchProjects(String filter, Integer nextProjectId, boolean remoteLoading) {
        projectRepository.searchProjects(filter, nextProjectId);
    }

    public void getProjectsByTheme(String themeName, String country) {
        projectRepository.getProjectsByTheme(themeName, country);
    }

    public void getProjectsByCountry(String country) {
        projectRepository.getProjectsByCountry(country);
    }
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isFirstLoading() {
        return isFirstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        isFirstLoading = firstLoading;
    }


    public MutableLiveData<Result> getThemesLiveData() {
        if(themesLiveData == null) {
            themesLiveData = projectRepository.getThemesLiveData();
        }
        return themesLiveData;
    }

    public void setCurrentTheme(Theme currentTheme) {
        this.currentTheme = currentTheme;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setNoMoreProjects(boolean noMoreProjects) {
        this.noMoreProjects = noMoreProjects;
    }

    public boolean isNoMoreProjects() {
        return noMoreProjects;
    }
}
