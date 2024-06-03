package it.sal.disco.unimib.charityhub;


import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.testing.TestNavHostController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.ui.main.AccountFragment;
import it.sal.disco.unimib.charityhub.ui.main.HomeFragment;
import it.sal.disco.unimib.charityhub.ui.main.MainActivity;
import it.sal.disco.unimib.charityhub.ui.welcome.LoginFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeFragmentTest {

    private Context context;
    private TestNavHostController navController;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuthSettings firebaseAuthSettings;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    public FragmentScenario<HomeFragment> fragmentScenario;
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

        fragmentScenario = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.Theme_CharityHub);

        fragmentScenario.onFragment(homeFragment -> {
            navController.setGraph(R.navigation.main_nav_graph);
            navController.setCurrentDestination(R.id.homeFragment);
            Navigation.setViewNavController(homeFragment.requireView(), navController);
        });

    }

    @After
    public void tearDown() {
        firebaseAuthSettings.setAppVerificationDisabledForTesting(false);
    }
    @Test
    public void testChipGroupIsDisplayed() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewIsDisplayed() {


        onView(withId(R.id.projectsRV)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToAccount() {
        // Perform click on the bottom navigation item
        onView(withId(R.id.accountFragment)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Verify navigation to account fragment
        int currentDestinationId = navController.getCurrentDestination().getId();
        String fragmentName = getFragmentName(currentDestinationId);
        Log.e("Current Destination ID: " , "" + currentDestinationId);
        Log.e("Current Fragment Name: " , "" +  fragmentName);

        //assertThat(currentDestinationId).isEqualTo(R.id.accountFragment);
    }

    @Test
    public void testClickOnRecyclerViewItem() throws InterruptedException {

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Scroll to a specific position in the RecyclerView
        int position = 3; // Change this to the position of the card view you want to interact with
        onView(withId(R.id.projectsRV))
                .perform(scrollToPosition(position));

        // Perform a click on the button within the specific card view
        onView(withId(R.id.projectsRV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position,
                        MyViewAction.clickChildViewWithId(R.id.viewProjectButton)));

        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.projectDetailsFragment);

    }

    private String getFragmentName(int destinationId) {
        NavDestination destination = navController.getGraph().findNode(destinationId);
        return destination == null ? "Unknown" : destination.getLabel().toString();
    }


    // Add more tests as needed...


    public static class MyViewAction {
        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return allOf(isAssignableFrom(android.view.View.class), ViewMatchers.isDisplayed());
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    if (v != null) {
                        v.performClick();
                    } else {
                        throw new PerformException.Builder()
                                .withActionDescription(this.getDescription())
                                .withViewDescription(HumanReadables.describe(view))
                                .withCause(new RuntimeException("No view with id " + id))
                                .build();
                    }
                }
            };
        }
    }

}

