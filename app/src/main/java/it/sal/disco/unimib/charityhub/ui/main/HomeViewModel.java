package it.sal.disco.unimib.charityhub.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.countries.CountryRepository;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;

public class HomeViewModel extends ViewModel {

    private final ProjectRepository projectRepository;

    private final CountryRepository countryRepository;
    private MutableLiveData<Result> projectsLiveData;
    private  MutableLiveData<Result> themesLiveData;
    private MutableLiveData<Result> countriesLiveData;


    private boolean isLoading;
    private boolean isFirstLoading;
    private int currentResults;
    private int totalResults;

    public HomeViewModel() {
        projectRepository = new ProjectRepository();
        countryRepository = new CountryRepository();
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

    public void setFirstLoading(boolean firstLoading) {
        isFirstLoading = firstLoading;
    }


    public MutableLiveData<Result> getThemesLiveData() {
        themesLiveData = projectRepository.getThemesLiveData();
        return themesLiveData;
    }

    public MutableLiveData<Result> getCountriesLiveData() {
        if(countriesLiveData == null) {
            countriesLiveData = countryRepository.getCountriesLiveData();
            return countriesLiveData;
        }
        return countriesLiveData;
    }
}
