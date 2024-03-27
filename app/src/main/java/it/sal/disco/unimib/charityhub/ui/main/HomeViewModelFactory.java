package it.sal.disco.unimib.charityhub.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.sal.disco.unimib.charityhub.ui.welcome.UserViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public HomeViewModelFactory(Application application) {
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == HomeViewModel.class)
            return (T) new HomeViewModel(application);
        else if(modelClass == UserViewModel.class)
            return (T) new UserViewModel(application);
        throw new RuntimeException();
    }
}
