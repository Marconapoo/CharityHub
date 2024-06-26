package it.sal.disco.unimib.charityhub;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.ui.welcome.LoginFragment;
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
        context.setTheme(R.style.Theme_CharityHub);
        // Launch the LoginFragment on the main thread
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

        TestNavHostController navController = new TestNavHostController(
                context);

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<LoginFragment> titleScenario = FragmentScenario.launchInContainer(LoginFragment.class, null, R.style.Theme_CharityHub);

        titleScenario.onFragment(loginFragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.welcome_nav_graph);

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(loginFragment.requireView(), navController);
        });

        // Enter valid email and password
        onView(withId(R.id.emailInputText)).perform(ViewActions.typeText("luigiverdi@tiscali.it"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInputText)).perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());
        // Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Check that the CircularProgressIndicator is displayed
        //onView(withId(R.id.progressIndicator)).check(matches(isDisplayed()));

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.mainActivity);
    }

    @Test
    public void testLoginFailure() {
        // Enter invalid email and password
        onView(withId(R.id.emailInputText)).perform(ViewActions.typeText("invalid@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInputText)).perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Check that the error message is displayed
        onView(ViewMatchers.withText(R.string.email_or_password_are_not_correct)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToRegistrationFragment() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                context);

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<LoginFragment> titleScenario = FragmentScenario.launchInContainer(LoginFragment.class, null, R.style.Theme_CharityHub);

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


}
