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

public class LoginFragment extends Fragment {

    UserViewModel userViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        TextInputEditText inputEmail = view.findViewById(R.id.emailInputText);
        TextInputEditText inputPassword = view.findViewById(R.id.passwordInputText);
        TextInputLayout emailTextField = view.findViewById(R.id.emailTextField);
        TextInputLayout passwordTextField = view.findViewById(R.id.passwordTextField);
        Button logInButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerTextButton);

        logInButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()) {
                userViewModel.getUserLiveData(email, password, null, true).observe(getViewLifecycleOwner(), result -> {
                    if(result.isSuccess()) {
                        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainActivity);
                        requireActivity().finish();
                    }
                    else {
                        Log.e("Login fragment", ((Result.Error) result).getErrorMessage());
                    }
                });
            }
            else {
                if(password.isEmpty()) {
                    passwordTextField.setError("Please insert a password");
                }
            }
        });


        registerButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registrationFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(userViewModel != null && userViewModel.getLoggedUser() != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainActivity);
            requireActivity().finish();
        }
    }
}