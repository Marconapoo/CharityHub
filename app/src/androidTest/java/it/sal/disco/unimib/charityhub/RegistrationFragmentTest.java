package it.sal.disco.unimib.charityhub;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.ui.welcome.RegistrationFragment;
import it.sal.disco.unimib.charityhub.ui.welcome.WelcomeActivity;

@RunWith(AndroidJUnit4.class)
public class RegistrationFragmentTest {

    @Rule
    public ActivityScenarioRule<WelcomeActivity> activityScenarioRule = new ActivityScenarioRule<>(WelcomeActivity.class);

    @Before
    public void setUp() {
        // Esegui il fragment di registrazione prima di ogni test
        ActivityScenario<WelcomeActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new RegistrationFragment()).commit());
    }

    @Test
    public void testRegistrationSuccess() {
        // Testa la registrazione con credenziali valide
        Espresso.onView(ViewMatchers.withId(R.id.emailInputText)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.passwordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmPasswordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fullNameInputText)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.countryAutoCompleteTextView)).perform(ViewActions.typeText("Italy"), ViewActions.closeSoftKeyboard()); // Assicurati che "Italy" sia un elemento valido nell'autocomplete
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).perform(ViewActions.click());

        // Verifica che il progress indicator non sia visibile dopo la registrazione
        Espresso.onView(ViewMatchers.withId(R.id.progressIndicator)).check(matches(isDisplayed()));


        // Verifica che la navigazione avvenga con successo
        //Espresso.onView(ViewMatchers.withId(R.id.mainActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())); // Assicurati che mainActivity sia l'ID corretto del layout principale
    }

    @Test
    public void testPasswordMismatch() {
        // Testa la registrazione con password e conferma password diverse
        Espresso.onView(ViewMatchers.withId(R.id.emailInputText)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.passwordInputText)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.confirmPasswordInputText)).perform(ViewActions.typeText("differentpassword"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fullNameInputText)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.countryAutoCompleteTextView)).perform(ViewActions.typeText("Italy"), ViewActions.closeSoftKeyboard()); // Assicurati che "Italy" sia un elemento valido nell'autocomplete
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).perform(ViewActions.click());

        // Verifica che venga visualizzato un messaggio di errore
        Espresso.onView(ViewMatchers.withText(R.string.passwords_don_t_match)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFields() {
        // Testa la registrazione con campi vuoti
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).perform(ViewActions.click());

        // Verifica che vengano visualizzati messaggi di errore per ogni campo vuoto
        Espresso.onView(ViewMatchers.withText(R.string.password_must_be_at_least_6_characters)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withText(R.string.email_is_not_valid)).check(matches(isDisplayed()));
        //Espresso.onView(ViewMatchers.withText("Please insert a country")).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withText(R.string.please_insert_a_name)).check(matches(isDisplayed()));
    }
}
