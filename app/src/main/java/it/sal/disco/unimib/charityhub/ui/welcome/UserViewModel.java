package it.sal.disco.unimib.charityhub.ui.welcome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.IUserRepository;
import it.sal.disco.unimib.charityhub.data.repositories.UserRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserViewModel extends ViewModel {

    private final IUserRepository userRepository;

    private MutableLiveData<Result> userLiveData;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<Result> getUserLiveData(String email, String password, String fullName, boolean isUserRegistered) {
        userLiveData = userRepository.getUserLiveData(email, password, fullName, isUserRegistered);
        return userLiveData;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

}
