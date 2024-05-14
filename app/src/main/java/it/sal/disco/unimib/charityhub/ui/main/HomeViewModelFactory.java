package it.sal.disco.unimib.charityhub.ui.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.ui.welcome.UserViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectRepository projectRepository;

    public HomeViewModelFactory(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == HomeViewModel.class)
            return (T) new HomeViewModel(projectRepository);
        else if(modelClass == ProjectDetailsViewModel.class)
            return (T) new ProjectDetailsViewModel(projectRepository);
        throw new RuntimeException();
    }
}
