package it.sal.disco.unimib.charityhub.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.ui.welcome.UserViewModel;

public class AccountFragment extends Fragment {

    UserViewModel userViewModel;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button logOutButton = view.findViewById(R.id.logoutButton);

        Log.e("Account fragment", userViewModel.getLoggedUser().getName());

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                    Navigation.findNavController(v).navigate(R.id.action_accountFragment_to_welcomeActivity);
                    requireActivity().finish();
                });
            }
        });
    }
}