package it.sal.disco.unimib.charityhub.ui.welcome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.sal.disco.unimib.charityhub.data.repositories.user.UserRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    UserRepository userRepository;


    UserViewModel userViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userViewModel = new UserViewModel(userRepository);
    }

    @Test
    public void testGetUserLiveData() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        String fullName = "John Doe";
        String country = "USA";
        boolean isUserRegistered = false;

        // Create expected user and result
        User expectedUser = new User(email, fullName, "", country); // Assuming User class has a constructor like this
        Result.UserResponseSuccess expectedResult = new Result.UserResponseSuccess(expectedUser);
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(expectedResult);

        // Mock the repository method
        when(userRepository.getUserLiveData(email, password, fullName, country, isUserRegistered))
                .thenReturn(expectedLiveData);

        // Act
        MutableLiveData<Result> actualLiveData = userViewModel.getUserLiveData(email, password, fullName, country, isUserRegistered);

        Observer<Result> observer = result -> {
            Result actualResult = actualLiveData.getValue();
            assertTrue(actualResult instanceof Result.UserResponseSuccess);

            Result.UserResponseSuccess actualUserResponse = (Result.UserResponseSuccess) actualResult;
            User actualUser = actualUserResponse.getUser();

            assertNotNull(actualUser);
            assertEquals(expectedUser.getEmail(), actualUser.getEmail());
            assertEquals(expectedUser.getName(), actualUser.getName());
            assertEquals(expectedUser.getCountryOfInterest(), actualUser.getCountryOfInterest());
        };
        // Assert

        actualLiveData.observeForever(observer);
        verify(userRepository).getUserLiveData(email, password, fullName, country, isUserRegistered);
        actualLiveData.removeObserver(observer);
    }

    @Test
    public void testLogout() {
        MutableLiveData<Result> expectedResult = new MutableLiveData<>();
        expectedResult.setValue(null);
        when(userRepository.logOut()).thenReturn(expectedResult);

        MutableLiveData<Result> actualLiveData = userViewModel.logout();
        Observer<Result> observer = result -> {
            assertNull(actualLiveData.getValue());
            assertEquals(expectedResult, actualLiveData);
        };

        actualLiveData.observeForever(observer);
        verify(userRepository).logOut();
        actualLiveData.removeObserver(observer);
    }


    @Test
    public void testChangeUserCountry() {
        User expectedUser = new User("John Doe", "john@email.com", "", "New Country");
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        Result.UserResponseSuccess expectedResult = new Result.UserResponseSuccess(expectedUser);

        expectedLiveData.setValue(expectedResult);

        when(userRepository.changeUserCountry(expectedUser)).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = userViewModel.changeUserCountry(expectedUser);

        Observer<Result> observer = new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                Result actualResult = actualLiveData.getValue();
                assertNotNull(actualResult);
                assertTrue(actualResult instanceof Result.UserResponseSuccess);
                Result.UserResponseSuccess actualResponse = ((Result.UserResponseSuccess) actualResult);

                assertEquals(expectedLiveData, actualLiveData);
                assertEquals(expectedUser, actualResponse.getUser());
            }
        };

        actualLiveData.observeForever(observer);

        verify(userRepository).changeUserCountry(expectedUser);

        actualLiveData.removeObserver(observer);
    }

    @Test
    public void testGetLoggedUser() {
        // Arrange
        User expectedUser = new User("test@example.com", "John Doe", "", "US");
        when(userRepository.getLoggedUser()).thenReturn(expectedUser);

        // Act
        User actualUser = userViewModel.getLoggedUser();

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository).getLoggedUser();
    }
}
