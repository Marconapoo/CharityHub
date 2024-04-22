package it.sal.disco.unimib.charityhub.data.source.user;

import it.sal.disco.unimib.charityhub.data.repositories.user.UserResponseCallback;
import it.sal.disco.unimib.charityhub.model.User;

public abstract class BaseUserAuthenticationDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void logIn(String email, String password);
    public abstract void signIn(String email, String password, String fullName, String country);
    public abstract void logOut();
    public abstract User getLoggedUser();

}
