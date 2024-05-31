package it.sal.disco.unimib.charityhub;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.ui.main.HomeFragment;
import it.sal.disco.unimib.charityhub.ui.main.MainActivity;
import it.sal.disco.unimib.charityhub.ui.welcome.LoginFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.ChipGroup;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }
    @Test
    public void testChipGroupIsDisplayed() {
        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewIsDisplayed() {
        onView(withId(R.id.projectsRV)).check(matches(isDisplayed()));
    }

    @Test
    public void noProjectsTest() {


    }
    @Test
    public void testClickOnRecyclerViewItem() throws InterruptedException {

        TestNavHostController navController = new TestNavHostController(
                context);

        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<HomeFragment> titleScenario = FragmentScenario.launchInContainer(HomeFragment.class, null, R.style.Theme_CharityHub);

        titleScenario.onFragment(homeFragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.main_nav_graph);

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(homeFragment.requireView(), navController);
        });
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Scroll to a specific position in the RecyclerView
        int position = 3; // Change this to the position of the card view you want to interact with
        onView(withId(R.id.projectsRV))
                .perform(RecyclerViewActions.scrollToPosition(position));

        // Perform a click on the button within the specific card view
        onView(withId(R.id.projectsRV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position,
                        MyViewAction.clickChildViewWithId(R.id.viewProjectButton)));

        assertThat(navController.getCurrentDestination().getId()).isEqualTo(R.id.projectDetailsFragment);

    }

    // Add more tests as needed...

    public static class MyViewAction {
        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return allOf(ViewMatchers.isAssignableFrom(android.view.View.class), ViewMatchers.isDisplayed());
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

