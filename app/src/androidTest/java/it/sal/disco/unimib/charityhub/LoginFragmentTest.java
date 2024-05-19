package it.sal.disco.unimib.charityhub;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static com.google.common.truth.Truth.assertThat;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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


    @Before
    public void setUp() {

        // Launch the LoginFragment
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new LoginFragment())
                    .commitNow();
        });
    }

    @After
    public void tearDown() {

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
        onView(withId(R.id.emailTextField)).check(matches(hasErrorText("Email or password are not correct")));
        onView(withId(R.id.passwordTextField)).check(matches(hasErrorText("Email or password are not correct")));
    }

    @Test
    public void testNavigateToRegistration() {

        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        // Launch RegistrationFragment in a container
        FragmentScenario<RegistrationFragment> registrationScenario = FragmentScenario.launchInContainer(RegistrationFragment.class);

        registrationScenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
        });

        // Click the register button
        onView(withId(R.id.registerTextButton)).perform(click());

        // Verify that the NavController navigates to the registration fragment
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.registrationFragment);

    }

}
