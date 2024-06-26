package it.sal.disco.unimib.charityhub;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.anything;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.ui.welcome.RegistrationFragment;

@RunWith(AndroidJUnit4.class)
public class RegistrationFragmentTest {


    private Context context;
    private TestNavHostController navController;
    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        navController = new TestNavHostController(context);

        // Create a graphical FragmentScenario for the RegistrationFragment
        FragmentScenario<RegistrationFragment> registrationScenario = FragmentScenario.launchInContainer(RegistrationFragment.class, null, R.style.Theme_CharityHub);

        registrationScenario.onFragment(registrationFragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.welcome_nav_graph);

            navController.setCurrentDestination(R.id.registrationFragment);
            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(registrationFragment.requireView(), navController);
        });
    }

    @Test
    public void testRegistrationSuccess() {


        // Test registration with valid credentials
        onView(ViewMatchers.withId(R.id.emailInputText)).perform(ViewActions.typeText("newtest2@example.com"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.fullNameInputText)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());

        // Type in the country and select from the dropdown
        onView(ViewMatchers.withId(R.id.countryAutoCompleteTextView)).perform(ViewActions.typeText("Italy"), ViewActions.closeSoftKeyboard());

        onData(anything())
                .atPosition(0)
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // Continue with the rest of the form
        onView(ViewMatchers.withId(R.id.passwordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.confirmPasswordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());

        // Ensure "Italy" is selected properly in the autocomplete and then click the register button
        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());


        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.mainActivity);
    }


    @Test
    public void testPasswordMismatch() {
        // Testa la registrazione con password e conferma password diverse
        onView(ViewMatchers.withId(R.id.emailInputText)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.fullNameInputText)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.countryAutoCompleteTextView)).perform(ViewActions.typeText("Italy"), ViewActions.closeSoftKeyboard());
        onData(anything())
                .atPosition(0)
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        onView(ViewMatchers.withId(R.id.passwordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.confirmPasswordInputText)).perform(ViewActions.typeText("differentpassword"), ViewActions.closeSoftKeyboard());


        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());

        // Verifica che venga visualizzato un messaggio di errore
        onView(ViewMatchers.withText(R.string.passwords_don_t_match)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFields() {
        onView(withId(R.id.registerButton)).perform(scrollTo());
        // Testa la registrazione con campi vuoti
        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());

        // Verifica che vengano visualizzati messaggi di errore per ogni campo vuoto
        onView(ViewMatchers.withText(R.string.password_must_be_at_least_6_characters)).check(matches(isDisplayed()));
        onView(ViewMatchers.withText(R.string.email_is_not_valid)).check(matches(isDisplayed()));
        //Espresso.onView(ViewMatchers.withText("Please insert a country")).check(matches(isDisplayed()));
        onView(ViewMatchers.withText(R.string.please_insert_a_name)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToLoginFragment() {
        onView(withId(R.id.loginTextButton)).perform(scrollTo());

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.loginTextButton)).perform(ViewActions.click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.loginFragment);
    }
}
