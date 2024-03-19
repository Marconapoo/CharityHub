package it.sal.disco.unimib.charityhub.data.source;

import it.sal.disco.unimib.charityhub.data.repositories.UserResponseCallback;
import it.sal.disco.unimib.charityhub.model.User;

public abstract class BaseUserDataRemoteDataSource {

    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
}
