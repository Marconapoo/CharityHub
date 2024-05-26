package it.sal.disco.unimib.charityhub.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

import it.sal.disco.unimib.charityhub.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController;
        if(navHostFragment != null) {
            navController = navHostFragment.getNavController();
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);


            navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
                if(navDestination.getId() == R.id.projectDetailsFragment || navDestination.getId() == R.id.donationFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                    /*
                    bottomNavigationView.getMenu().getItem(0).setVisible(false);
                    bottomNavigationView.getMenu().getItem(1).setVisible(false);
                    bottomNavigationView.getMenu().getItem(2).setVisible(true);*/
                }
                else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    /*
                    bottomNavigationView.getMenu().getItem(0).setVisible(true);
                    bottomNavigationView.getMenu().getItem(1).setVisible(true);
                    bottomNavigationView.getMenu().getItem(2).setVisible(false);*/
                }
            });
        }
    }
}