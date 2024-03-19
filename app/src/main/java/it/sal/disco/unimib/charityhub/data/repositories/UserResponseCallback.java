package it.sal.disco.unimib.charityhub.data.repositories;

import it.sal.disco.unimib.charityhub.model.User;

public interface UserResponseCallback {
    void onSuccessAuthentication(User user);
    void onFailureAuthentication(String error);
    void onSuccessLogout();
}
