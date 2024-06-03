package it.sal.disco.unimib.charityhub;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.anything;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.ui.main.AccountFragment;
import it.sal.disco.unimib.charityhub.ui.main.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountFragmentTest {

    private Context context;
    private TestNavHostController navController;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuthSettings firebaseAuthSettings;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAppVerificationDisabledForTesting(true);

        firebaseAuth.signInWithEmailAndPassword("giuliogialli@gmail.com", "123456")
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        throw new AssertionError("Failed to sign in test user");
                    }
                });


        context = ApplicationProvider.getApplicationContext();
        navController = new TestNavHostController(
                context);

        FragmentScenario<AccountFragment> fragmentScenario = FragmentScenario.launchInContainer(AccountFragment.class, null, R.style.Theme_CharityHub);
        fragmentScenario.onFragment(accountFragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.main_nav_graph);

            navController.setCurrentDestination(R.id.accountFragment);
            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(accountFragment.requireView(), navController);
        });

    }

    @Test
    public void testChangeCountryButton() {
        onView(withId(R.id.editButton)).perform(click());
        onView(withId(R.id.countryEdit)).check(matches(isEnabled()));
        onView(withId(R.id.confirmButton)).check(matches(isDisplayed()));
        onView(withId(R.id.undoButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testUndoButton() {
        onView(withId(R.id.editButton)).perform(click());
        onView(withId(R.id.countryEdit)).perform(typeText("India"), closeSoftKeyboard());
        onData(anything())
                .atPosition(0)
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        onView(withId(R.id.undoButton)).perform(click());
        onView(withId(R.id.countryEdit)).check(matches(withText("Italy")));

    }
    @Test
    public void testChangeCountry() {
        onView(withId(R.id.editButton)).perform(click());
        onView(withId(R.id.countryEdit)).perform(ViewActions.typeText("India"), ViewActions.closeSoftKeyboard());
        onData(anything())
                .atPosition(0)
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        onView(withId(R.id.confirmButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.editButton)).check(matches(isDisplayed()));
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));
        onView(withId(R.id.countryEdit)).check(matches(isNotEnabled()));
        onView(withId(R.id.countryEdit)).check(matches(withText("India")));
    }

    @Test
    public void logOutButton() {
        onView(withId(R.id.logoutButton)).perform(click());
        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.welcomeActivity);
    }


    @After
    public void tearDown() {
        firebaseAuthSettings.setAppVerificationDisabledForTesting(false);
    }

}
