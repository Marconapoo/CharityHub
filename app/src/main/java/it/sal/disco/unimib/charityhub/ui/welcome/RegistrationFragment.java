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

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.countries.Country;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.ui.main.HomeViewModelFactory;
import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class RegistrationFragment extends Fragment {

    UserViewModel userViewModel;
    List<Country> countries;

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
        userViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        TextInputEditText inputEmail = view.findViewById(R.id.emailInputText);
        TextInputEditText inputPassword = view.findViewById(R.id.passwordInputText);
        TextInputEditText inputFullName = view.findViewById(R.id.fullNameInputText);
        TextInputLayout emailTextField = view.findViewById(R.id.emailTextField);
        TextInputLayout passwordTextField = view.findViewById(R.id.passwordTextField);
        TextInputLayout fullNameTextField = view.findViewById(R.id.fullNameTextField);
        Button registerButton = view.findViewById(R.id.registerButton);
        Button logInTextButton = view.findViewById(R.id.loginTextButton);;

        countries = new ArrayList<>();

        MaterialAutoCompleteTextView countryPicker = view.findViewById(R.id.countryAutoCompleteTextView);


        userViewModel.getCountriesLiveData().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccess()) {
                List<Country> countriesApiResponse = ((Result.CountriesResponseSucccess) result).getCountriesResponse();
                countriesApiResponse.sort(Comparator.comparing(country -> country.getName().getCommonName()));
                countries.addAll(countriesApiResponse);
                String[] countryNames = new String[countries.size()];
                for(int i = 0; i < countries.size(); i++) {
                    countryNames[i] = countries.get(i).getName().getCommonName();
                }
                countryPicker.setSimpleItems(countryNames);
            }
            else {
                Log.e("Home fragment", ((Result.Error) result).getErrorMessage());
            }
        });


        registerButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String fullName = inputFullName.getText().toString();
            String countryCode = null;

            for(Country country : countries) {
                if(countryPicker.getText().toString().equals(country.getName().getCommonName())) {
                    countryCode = country.getCountryCode();
                    break;
                }
            }

            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity().getApplication());

            sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, countryCode);
            userViewModel.getUserLiveData(email, password, fullName,  countryCode, false).observe(getViewLifecycleOwner(), result -> {
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
            requireActivity().finish();
        }
    }
}