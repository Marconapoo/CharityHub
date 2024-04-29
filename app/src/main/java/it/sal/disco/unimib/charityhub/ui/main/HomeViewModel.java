package it.sal.disco.unimib.charityhub.ui.main;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.countries.CountryRepository;
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

    public HomeViewModel(Application application) {
        projectRepository = new ProjectRepository(application);

    }

    public MutableLiveData<Result> searchForProjects(String filter, Integer nextProjectId) {
        projectsLiveData = projectRepository.searchForProjects(filter, nextProjectId);
        return projectsLiveData;
    }

    public void searchProjects(String filter, Integer nextProjectId) {
        projectRepository.searchProjects(filter, nextProjectId);
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
            return themesLiveData;
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
