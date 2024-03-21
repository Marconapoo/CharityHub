package it.sal.disco.unimib.charityhub.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;

public class HomeViewModel extends ViewModel {

    private final ProjectRepository projectRepository;
    private MutableLiveData<Result> projectsLiveData;
    private boolean isLoading;
    private boolean isFirstLoading;
    private int currentResults;
    private int totalResults;

    public HomeViewModel() {
        projectRepository = new ProjectRepository();
    }

    public MutableLiveData<Result> getProjectsLiveData(String themeId, Integer nextProjectId) {
        getProjects(themeId, nextProjectId);
        return projectsLiveData;
    }

    public void getProjects(String themeId, Integer nextProjectId) {
        projectsLiveData = projectRepository.getProjectsLiveData(themeId, nextProjectId);
    }

    public boolean isFirstLoading() {
        return isFirstLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setFirstLoading(boolean firstLoading) {
        isFirstLoading = firstLoading;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
