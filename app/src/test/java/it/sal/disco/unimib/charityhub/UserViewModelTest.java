package it.sal.disco.unimib.charityhub;

import org.junit.Test;

import static org.junit.Assert.*;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;
import it.sal.disco.unimib.charityhub.ui.welcome.UserViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UserViewModelTest {

    private final UserViewModel viewModel = new UserViewModel(new Application());

    @Test
    public void testGetUserLiveData() {
        // Given
        String email = "test@example.com";
        String password = "password";
        String fullName = "John Doe";
        String country = "USA";
        boolean isUserRegistered = true;
        MutableLiveData<Result> expectedResult = new MutableLiveData<>(new Result.UserResponseSuccess(new User(email, fullName, country)));

        // When
        MutableLiveData<Result> resultLiveData = viewModel.getUserLiveData(email, password, fullName, country, isUserRegistered);

        // Then
        assertEquals(expectedResult.getValue(), resultLiveData.getValue());
    }
}