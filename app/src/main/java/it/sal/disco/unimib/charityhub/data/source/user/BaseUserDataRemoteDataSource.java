package it.sal.disco.unimib.charityhub.data.source.user;

import it.sal.disco.unimib.charityhub.data.repositories.user.UserResponseCallback;
import it.sal.disco.unimib.charityhub.model.User;

public abstract class BaseUserDataRemoteDataSource {

    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);

    public abstract void getUserCountry(User user);

    public abstract void changeUserInformation(String email, String fullName, String country);
}
