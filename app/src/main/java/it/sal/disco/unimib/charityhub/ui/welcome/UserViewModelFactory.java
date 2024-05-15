package it.sal.disco.unimib.charityhub.ui.welcome;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.sal.disco.unimib.charityhub.data.repositories.countries.CountryRepository;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.data.repositories.user.UserRepository;
import it.sal.disco.unimib.charityhub.ui.main.HomeViewModel;
import it.sal.disco.unimib.charityhub.ui.main.ProjectDetailsViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    public UserViewModelFactory(UserRepository userRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == UserViewModel.class)
            return (T) new UserViewModel(userRepository, countryRepository);
        throw new RuntimeException();
    }
}
