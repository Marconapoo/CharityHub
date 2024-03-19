package it.sal.disco.unimib.charityhub.data.repositories;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.data.source.BaseUserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.UserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserRepository implements IUserRepository, UserResponseCallback {

    private final MutableLiveData<Result> userLiveData;
    private final BaseUserAuthenticationDataSource userAuthenticationDataSource;

    public UserRepository() {
        this.userAuthenticationDataSource = new UserAuthenticationDataSource();
        userAuthenticationDataSource.setUserResponseCallback(this);
        this.userLiveData = new MutableLiveData<>();
    }

    @Override
    public void onSuccessAuthentication(User user) {
        if(user != null) {

        }
    }

    @Override
    public void onFailureAuthentication(String error) {

    }

    @Override
    public void onSuccessLogout() {

    }
}
