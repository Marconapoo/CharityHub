package it.sal.disco.unimib.charityhub;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@LargeTest
public class LoginFragmentTest {


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("it.sal.disco.unimib.charityhub", appContext.getPackageName());
    }


}