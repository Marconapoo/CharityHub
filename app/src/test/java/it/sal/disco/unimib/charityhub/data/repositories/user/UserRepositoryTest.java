package it.sal.disco.unimib.charityhub.data.repositories.user;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.BaseUserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.BaseUserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;

public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private BaseUserAuthenticationDataSource userAuthenticationDataSource;

    @Mock
    private BaseUserDataRemoteDataSource userDataRemoteDataSource;

    @Mock
    private BaseProjectLocalDataSource projectLocalDataSource;

    @Mock
    private MutableLiveData<Result> userLiveData;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository(userAuthenticationDataSource, userDataRemoteDataSource, projectLocalDataSource);
        userRepository.setUserLiveData(userLiveData); // Inject mocked LiveData
    }

    @Test
    public void testGetUserLiveData_RegisteredUser() {
        String email = "test@example.com";
        String password = "password";
        String fullName = "Test User";
        String country = "Country";
        boolean isRegistered = true;

        userRepository.getUserLiveData(email, password, fullName, country, isRegistered);

        verify(userAuthenticationDataSource).logIn(email, password);
    }

    @Test
    public void testGetUserLiveData_NewUser() {
        String email = "test@example.com";
        String password = "password";
        String fullName = "Test User";
        String country = "Country";
        boolean isRegistered = false;

        userRepository.getUserLiveData(email, password, fullName, country, isRegistered);

        verify(userAuthenticationDataSource).signIn(email, password, fullName, country);
    }

    @Test
    public void testGetLoggedUser() {
        User expectedUser = new User("test@example.com", "Test User", "Country");
        when(userAuthenticationDataSource.getLoggedUser()).thenReturn(expectedUser);

        User actualUser = userRepository.getLoggedUser();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testLogOut() {
        userRepository.logOut();

        verify(userAuthenticationDataSource).logOut();
    }

    @Test
    public void testChangeUserCountry() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.changeUserCountry(user);

        verify(userDataRemoteDataSource).changeUserCountry(user);
    }

    @Test
    public void testOnSuccessAuthentication() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.onSuccessAuthentication(user);

        verify(userDataRemoteDataSource).getUserCountry(user);
    }

    @Test
    public void testOnFailureAuthentication() {
        String error = "Authentication failed";

        userRepository.onFailureAuthentication(error);

        ArgumentCaptor<Result.Error> captor = ArgumentCaptor.forClass(Result.Error.class);
        verify(userLiveData).postValue(captor.capture());
        assertEquals(error, captor.getValue().getErrorMessage());
    }

    @Test
    public void testOnSuccessLogout() {
        userRepository.onSuccessLogout();

        verify(projectLocalDataSource).deleteAll();
        ArgumentCaptor<Result.UserResponseSuccess> captor = ArgumentCaptor.forClass(Result.UserResponseSuccess.class);
        verify(userLiveData).postValue(captor.capture());
        assertNull(captor.getValue().getUser());
    }

    @Test
    public void testOnSuccessUserSaved() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.onSuccessUserSaved(user);

        ArgumentCaptor<Result.UserResponseSuccess> captor = ArgumentCaptor.forClass(Result.UserResponseSuccess.class);
        verify(userLiveData).postValue(captor.capture());
        assertEquals(user, captor.getValue().getUser());
    }

    @Test
    public void testOnSuccessRegistration() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.onSuccessRegistration(user);

        verify(userDataRemoteDataSource).saveUserData(user);
    }

    @Test
    public void testOnCountryGotSuccess() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.onCountryGotSuccess(user);

        ArgumentCaptor<Result.UserResponseSuccess> captor = ArgumentCaptor.forClass(Result.UserResponseSuccess.class);
        verify(userLiveData).postValue(captor.capture());
        assertEquals(user, captor.getValue().getUser());
    }

    @Test
    public void testOnSuccessInfoChanged() {
        User user = new User("test@example.com", "Test User", "Country");

        userRepository.onSuccessInfoChanged(user);

        ArgumentCaptor<Result.UserResponseSuccess> captor = ArgumentCaptor.forClass(Result.UserResponseSuccess.class);
        verify(userLiveData).postValue(captor.capture());
        assertEquals(user, captor.getValue().getUser());
    }
}
