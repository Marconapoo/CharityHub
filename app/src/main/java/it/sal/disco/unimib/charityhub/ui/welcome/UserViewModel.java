package it.sal.disco.unimib.charityhub.ui.welcome;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.sal.disco.unimib.charityhub.data.repositories.countries.CountryRepository;
import it.sal.disco.unimib.charityhub.data.repositories.user.IUserRepository;
import it.sal.disco.unimib.charityhub.data.repositories.user.UserRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserViewModel extends ViewModel {

    private final IUserRepository userRepository;

    private MutableLiveData<Result> userLiveData;

    private final CountryRepository countryRepository;

    private MutableLiveData<Result> countriesLiveData;
    private MutableLiveData<String> userCountry;


    private boolean authenticationError;

    public UserViewModel(Context application) {
        userRepository = new UserRepository(application);
        countryRepository = new CountryRepository();
    }

    public MutableLiveData<Result> getUserLiveData(String email, String password, String fullName, String country, boolean isUserRegistered) {
        logUser(email, password, fullName, country, isUserRegistered);
        return userLiveData;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userLiveData == null) {
            userLiveData = userRepository.logOut();
        } else {
            userRepository.logOut();
        }
        return userLiveData;
    }

    public MutableLiveData<Result> getCountriesLiveData() {
        if(countriesLiveData == null) {
            countriesLiveData = countryRepository.getCountriesLiveData();
            return countriesLiveData;
        }
        return countriesLiveData;
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    public void logUser(String email, String password, String fullName, String country, boolean isUserRegistered) {
        userLiveData = userRepository.getUserLiveData(email, password, fullName, country, isUserRegistered);
    }

    public MutableLiveData<Result> changeUserCountry(User user) {
        changeUserInformation(user);
        return userLiveData;
    }

    public void changeUserInformation(User user) {
        userLiveData = userRepository.changeUserCountry(user);
    }
}
