package it.sal.disco.unimib.charityhub.data.source;

import it.sal.disco.unimib.charityhub.data.repositories.UserResponseCallback;

public abstract class BaseUserAuthenticationDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void logIn(String email, String password);
    public abstract void signIn(String email, String password, String fullName);
    public abstract void logOut();

}
