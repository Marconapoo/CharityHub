package it.sal.disco.unimib.charityhub.ui.welcome;

import static it.sal.disco.unimib.charityhub.utils.Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST;
import static it.sal.disco.unimib.charityhub.utils.Constants.SHARED_PREFERENCES_FILE_NAME;

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

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;
import it.sal.disco.unimib.charityhub.ui.main.HomeViewModelFactory;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

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
        userViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
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


        TextInputEditText inputEmail = view.findViewById(R.id.emailInputText);
        TextInputEditText inputPassword = view.findViewById(R.id.passwordInputText);
        TextInputLayout emailTextField = view.findViewById(R.id.emailTextField);
        TextInputLayout passwordTextField = view.findViewById(R.id.passwordTextField);
        Button logInButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerTextButton);
        CircularProgressIndicator circularProgressIndicator = view.findViewById(R.id.progressIndicator);
        userViewModel.setAuthenticationError(false);

        logInButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()) {
                circularProgressIndicator.setVisibility(View.VISIBLE);
                if(!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserLiveData(email, password, null, null, true).observe(getViewLifecycleOwner(), result -> {
                        circularProgressIndicator.setVisibility(View.GONE);
                        if (result.isSuccess()) {
                            userViewModel.setAuthenticationError(false);
                            User user = ((Result.UserResponseSuccess) result).getUser();
                            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
                            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, SHARED_PREFERENCES_COUNTRY_OF_INTEREST, user.getCountryOfInterest());
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainActivity);
                            requireActivity().finish();
                        } else {
                            emailTextField.setError("Email or password are not correct");
                            passwordTextField.setError("Email or password are not correct");
                            userViewModel.setAuthenticationError(true);
                        }
                    });
                }
                else {
                    Log.w("Login Fragment", "TEST");
                    userViewModel.logUser(email, password, null, null, true);
                }
            }
            else {
                if(password.isEmpty()) {
                    passwordTextField.setError("Please insert a password");
                }
                if(email.isEmpty()) {
                    emailTextField.setError(("Please insert an email"));
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