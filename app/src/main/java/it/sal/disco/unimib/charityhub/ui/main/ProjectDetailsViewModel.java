package it.sal.disco.unimib.charityhub.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.project.IProjectRepository;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;

public class ProjectDetailsViewModel extends ViewModel {

    private final IProjectRepository projectRepository;
    private MutableLiveData<Result> imagesLiveData;

    public ProjectDetailsViewModel(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public MutableLiveData<Result> getImagesLiveData(String projectId) {
        imagesLiveData = projectRepository.getImagesLiveData(projectId);
        return imagesLiveData;
    }
}
