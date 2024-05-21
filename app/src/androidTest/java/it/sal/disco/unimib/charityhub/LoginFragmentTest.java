package it.sal.disco.unimib.charityhub;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.ui.welcome.LoginFragment;
import it.sal.disco.unimib.charityhub.ui.welcome.RegistrationFragment;
import it.sal.disco.unimib.charityhub.ui.welcome.WelcomeActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginFragmentTest {

    @Rule
    public ActivityScenarioRule<WelcomeActivity> activityScenarioRule = new ActivityScenarioRule<>(WelcomeActivity.class);

    public Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.setTheme(androidx.appcompat.R.style.Theme_AppCompat);
        // Launch the LoginFragment on the main thread
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new LoginFragment())
                    .commitNow();
        });
    }

    @After
    public void tearDown() {
        // Clean up after each test
    }

    @Test
    public void testWrongEmailFormat() {
        onView(withId(R.id.emailInputText)).perform(ViewActions.typeText("this_is_not_a_correct_email"), closeSoftKeyboard());
        onView(withId(R.id.passwordInputText)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.loginButton)).perform(click());

    }
    @Test
    public void testLoginSuccess() {
        // Enter valid email and password
        onView(withId(R.id.emailInputText)).perform(ViewActions.typeText("luigiverdi@tiscali.it"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInputText)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Check that the CircularProgressIndicator is displayed
        onView(withId(R.id.progressIndicator)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        // Enter invalid email and password
        onView(withId(R.id.emailInputText)).perform(ViewActions.typeText("invalid@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInputText)).perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Check that the error message is displayed
        onView(withId(R.id.emailTextField)).check(matches(withError("Email or password are not correct")));
        onView(withId(R.id.passwordTextField)).check(matches(withError("Email or password are not correct")));
    }

    @Test
    public void navigateToRegistrationFragment() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                context);

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<LoginFragment> titleScenario = FragmentScenario.launchInContainer(LoginFragment.class, null, androidx.appcompat.R.style.Theme_AppCompat_Light);

        titleScenario.onFragment(loginFragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.welcome_nav_graph);

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(loginFragment.requireView(), navController);
        });

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.registerTextButton)).perform(ViewActions.click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.registrationFragment);
    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof EditText) {
                    return ((EditText)item).getError().toString().equals(expected);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message" + expected + ", find it!");
            }
        };
    }

}
