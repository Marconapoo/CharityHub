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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.User;
import it.sal.disco.unimib.charityhub.model.countries.Country;
import it.sal.disco.unimib.charityhub.ui.welcome.UserViewModel;
import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class AccountFragment extends Fragment {

    UserViewModel userViewModel;
    User user;
    SharedPreferencesUtil sharedPreferencesUtil;
    List<Country> countryList;
    MaterialAutoCompleteTextView countryPicker;
    String[] countryNames;
    String currentCountry;
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
        countryList = new ArrayList<>();
        userViewModel.getCountriesLiveData().observe(this, result -> {
            if(result.isSuccess()) {
                List<Country> countriesApiResponse = ((Result.CountriesResponseSucccess) result).getCountriesResponse();
                countriesApiResponse.sort(Comparator.comparing(country -> country.getName().getCommonName()));
                countryList.addAll(countriesApiResponse);
                countryNames = new String[countryList.size()];
                for(int i = 0; i < countryList.size(); i++) {
                    countryNames[i] = countryList.get(i).getName().getCommonName();
                }
                setCountries(countryNames);
            }
            else {
                Log.e("Account fragment", ((Result.Error) result).getErrorMessage());
            }
        });
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
        Button editButton = view.findViewById(R.id.editButton);
        countryPicker = view.findViewById(R.id.countryEdit);
        TextInputEditText fullName = view.findViewById(R.id.fullNameEditText);
        TextInputEditText email = view.findViewById(R.id.emailEditText);
        TextInputLayout fullNameText = view.findViewById(R.id.fullName);
        TextInputLayout emailText = view.findViewById(R.id.email);
        TextInputLayout countryText = view.findViewById(R.id.countryPicker);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button undoButton = view.findViewById(R.id.undoButton);

        //Log.e("Account fragment", userViewModel.getLoggedUser().getCountryOfInterest());
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        user = userViewModel.getLoggedUser();
        currentCountry = sharedPreferencesUtil.readStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST);

        fullName.setText(user.getName());
        email.setText(user.getEmail());

        logOutButton.setOnClickListener(v -> userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
            Navigation.findNavController(v).navigate(R.id.action_accountFragment_to_welcomeActivity);
            requireActivity().finish();
        }));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameText.setEnabled(true);
                emailText.setEnabled(true);
                countryText.setEnabled(true);
                countryPicker.setText("");
                logOutButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.VISIBLE);
                undoButton.setVisibility(View.VISIBLE);
            }
        });

        confirmButton.setOnClickListener(v -> {
            String newFullName, newEmail, newCountry;
            if(fullName.getText() != null && !fullName.getText().toString().equals(user.getName())) {
                newFullName = fullName.getText().toString();
            }
            if(email.getText() != null && !email.getText().toString().equals(user.getEmail())) {
                newEmail = email.getText().toString();
            }
            if(countryPicker.getText() != null) {
                for(Country country : countryList) {
                    if(country.getName().getCommonName().equals(countryPicker.getText().toString()) && !currentCountry.equals(country.getCountryCode())) {
                        newCountry = country.getCountryCode();
                        break;
                    }
                }
            }
        });

        undoButton.setOnClickListener(v -> {
            fullName.setText(user.getName());
            email.setText(user.getEmail());
            countryPicker.setText(currentCountry);
            fullNameText.setEnabled(false);
            emailText.setEnabled(false);
            countryText.setEnabled(false);
            logOutButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.GONE);
            undoButton.setVisibility(View.GONE);
        });

    }

    public void setCountries(String[] countryNames) {
        for(Country country : countryList) {
            if(country.getCountryCode().equals(currentCountry)) {
                currentCountry = country.getName().getCommonName();
                break;
            }
        }
        if(countryPicker != null) {
            Log.w("Account fragment", "" + countryNames.length);
            countryPicker.setSimpleItems(countryNames);
            countryPicker.setText(currentCountry);
        }
    }

}