package it.sal.disco.unimib.charityhub.data.repositories.user;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public interface IUserRepository {

    MutableLiveData<Result> getUserLiveData(String email, String password, String fullName, String country, boolean isRegistered);
    User getLoggedUser();
    MutableLiveData<Result> logOut();

    MutableLiveData<Result> changeUserCountry(User user);
}
