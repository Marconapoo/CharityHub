package it.sal.disco.unimib.charityhub.ui.welcome;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.Result;

public class RegistrationFragment extends Fragment {

    UserViewModel userViewModel;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        TextInputEditText inputEmail = view.findViewById(R.id.emailInputText);
        TextInputEditText inputPassword = view.findViewById(R.id.passwordInputText);
        TextInputEditText inputFullName = view.findViewById(R.id.fullNameInputText);
        TextInputLayout emailTextField = view.findViewById(R.id.emailTextField);
        TextInputLayout passwordTextField = view.findViewById(R.id.passwordTextField);
        TextInputLayout fullNameTextField = view.findViewById(R.id.fullNameTextField);
        Button registerButton = view.findViewById(R.id.registerButton);
        Button logInTextButton = view.findViewById(R.id.loginTextButton);

        registerButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String fullName = inputFullName.getText().toString();

            userViewModel.getUserLiveData(email, password, fullName, false).observe(getViewLifecycleOwner(), result -> {
                if(result.isSuccess()) {
                    Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_mainActivity);
                    requireActivity().finish();
                }
                else {
                    Log.e("Registration Fragment", ((Result.Error) result).getErrorMessage());
                }
            });
        });

        logInTextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_loginFragment);
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(userViewModel != null && userViewModel.getLoggedUser() != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainActivity);
        }
    }
}