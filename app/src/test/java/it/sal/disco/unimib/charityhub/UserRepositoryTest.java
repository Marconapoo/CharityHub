package it.sal.disco.unimib.charityhub;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import it.sal.disco.unimib.charityhub.data.repositories.user.UserRepository;
import it.sal.disco.unimib.charityhub.model.Result;

@RunWith(JUnit4.class)
public class UserRepositoryTest {


    private Context context = ApplicationProvider.getApplicationContext();

    private UserRepository userRepository;

    @Before
    public void before() {
        FirebaseApp.initializeApp(context);
        FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
        userRepository = new UserRepository(context);
    }


    @Test
    public void testLogOut() {
        MutableLiveData<Result> expectedResult = new MutableLiveData<Result>();

        when(userRepository.logOut()).thenReturn(expectedResult);

        assertNull(expectedResult);

    }

}
