package it.sal.disco.unimib.charityhub.data.repositories.user;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.BaseUserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.BaseUserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.UserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.UserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserRepository implements IUserRepository, UserResponseCallback {

    private final MutableLiveData<Result> userLiveData;
    private final BaseUserAuthenticationDataSource userAuthenticationDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseProjectLocalDataSource projectLocalDataSource;


    public UserRepository(BaseUserAuthenticationDataSource userAuthenticationDataSource, BaseUserDataRemoteDataSource userDataRemoteDataSource, BaseProjectLocalDataSource localDataSource) {
        this.userAuthenticationDataSource = userAuthenticationDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.projectLocalDataSource = localDataSource;
        userAuthenticationDataSource.setUserResponseCallback(this);
        userDataRemoteDataSource.setUserResponseCallback(this);
        this.userLiveData = new MutableLiveData<>();
    }


    @Override
    public MutableLiveData<Result> getUserLiveData(String email, String password, String fullName, String country, boolean isRegistered) {
        if(isRegistered)
            userAuthenticationDataSource.logIn(email, password);
        else
            userAuthenticationDataSource.signIn(email, password, fullName, country);
        return userLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userAuthenticationDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logOut() {
        userAuthenticationDataSource.logOut();
        return userLiveData;
    }

    @Override
    public MutableLiveData<Result> changeUserCountry(User user) {
        userDataRemoteDataSource.changeUserCountry(user);
        return userLiveData;
    }

    @Override
    public void onSuccessAuthentication(User user) {
        userDataRemoteDataSource.getUserCountry(user);
    }

    @Override
    public void onFailureAuthentication(String error) {
        Result.Error result = new Result.Error(error);
        userLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        projectLocalDataSource.deleteAll();
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userLiveData.postValue(result);
    }

    @Override
    public void onSuccessUserSaved(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userLiveData.postValue(result);
    }

    @Override
    public void onSuccessRegistration(User user) {
        if(user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onCountryGotSuccess(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userLiveData.postValue(result);
    }


    @Override
    public void onSuccessInfoChanged(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userLiveData.postValue(result);
    }


}
