package it.sal.disco.unimib.charityhub;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import it.sal.disco.unimib.charityhub.ui.main.MainActivity;
import it.sal.disco.unimib.charityhub.ui.welcome.LoginFragment;
import it.sal.disco.unimib.charityhub.ui.welcome.WelcomeActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginFragmentTest {


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("it.sal.disco.unimib.charityhub", appContext.getPackageName());
    }
}