package it.sal.disco.unimib.charityhub.data.repositories;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.BaseUserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.BaseUserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.data.source.UserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.UserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserRepository implements IUserRepository, UserResponseCallback {

    private final MutableLiveData<Result> userLiveData;
    private final BaseUserAuthenticationDataSource userAuthenticationDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;

    public UserRepository() {
        this.userAuthenticationDataSource = new UserAuthenticationDataSource();
        this.userDataRemoteDataSource = new UserDataRemoteDataSource();
        userAuthenticationDataSource.setUserResponseCallback(this);
        this.userLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> getUserLiveData(String email, String password, String fullName, boolean isRegistered) {
        if(isRegistered)
            userAuthenticationDataSource.logIn(email, password);
        else
            userAuthenticationDataSource.signIn(email, password, fullName);
        return userLiveData;
    }

    @Override
    public void onSuccessAuthentication(User user) {
        if(user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureAuthentication(String error) {
        Result.Error result = new Result.Error(error);
        userLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userLiveData.postValue(result);
    }

    @Override
    public void onSuccessUserSaved(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userLiveData.postValue(result);
    }
}
