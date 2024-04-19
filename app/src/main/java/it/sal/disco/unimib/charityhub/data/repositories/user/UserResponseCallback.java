package it.sal.disco.unimib.charityhub.data.repositories.user;

import it.sal.disco.unimib.charityhub.model.User;

public interface UserResponseCallback {
    void onSuccessAuthentication(User user);
    void onFailureAuthentication(String error);
    void onSuccessLogout();
    void onSuccessUserSaved(User user);
    void onSuccessRegistration(User user);
    void onCountryGotSuccess(User user);
    void onSuccessAuthInfoChanged(String newFullName, String newEmail, String newCountry);
    void onSuccessInfoChanged(User user);

}
