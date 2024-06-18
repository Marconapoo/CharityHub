package it.sal.disco.unimib.charityhub.ui.welcome;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.data.repositories.user.UserRepository;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.UserAuthenticationDataSource;
import it.sal.disco.unimib.charityhub.data.source.user.UserDataRemoteDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.countries.Country;
import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class RegistrationFragment extends Fragment {

    UserViewModel userViewModel;
    List<Country> countries;
    String[] countryNames;
    MaterialAutoCompleteTextView countryPicker;

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
        UserAuthenticationDataSource userAuthenticationDataSource = new UserAuthenticationDataSource();
        UserDataRemoteDataSource userDataRemoteDataSource = new UserDataRemoteDataSource();
        ProjectLocalDataSource projectLocalDataSource = new ProjectLocalDataSource(requireActivity().getApplicationContext());
        UserRepository userRepository = new UserRepository(userAuthenticationDataSource, userDataRemoteDataSource, projectLocalDataSource);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            View view = inflater.inflate(R.layout.fragment_registration, container, false);
            return view;
        } catch (Exception e) {
            Log.e("REGISTRATION FRAGMENT", e.getMessage());
            throw e;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        TextInputEditText inputEmail = view.findViewById(R.id.emailInputText);
        TextInputEditText inputPassword = view.findViewById(R.id.passwordInputText);
        TextInputEditText inputFullName = view.findViewById(R.id.fullNameInputText);
        TextInputLayout emailTextField = view.findViewById(R.id.emailTextField);
        TextInputLayout passwordTextField = view.findViewById(R.id.passwordTextField);
        TextInputLayout confirmPasswordTextField = view.findViewById(R.id.confirmPasswordTextField);
        TextInputEditText inputConfirmPassword = view.findViewById(R.id.confirmPasswordInputText);
        TextInputLayout fullNameTextField = view.findViewById(R.id.fullNameTextField);
        Button registerButton = view.findViewById(R.id.registerButton);
        Button logInTextButton = view.findViewById(R.id.loginTextButton);;
        CircularProgressIndicator circularProgressIndicator = view.findViewById(R.id.progressIndicator);

        countries = new ArrayList<>();

        countryPicker = view.findViewById(R.id.countryAutoCompleteTextView);


        getCountries();

        registerButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String fullName = inputFullName.getText().toString();
            String countryCode = countryPicker.getText().toString();

            if(checkEmail(email) && checkPassword(password) && !countryCode.isEmpty() && !fullName.isEmpty()) {
                if(password.equals(inputConfirmPassword.getText().toString())) {
                    circularProgressIndicator.setVisibility(View.VISIBLE);
                    for (Country country : countries) {
                        if (countryPicker.getText().toString().equals(country.getCountryName())) {
                            countryCode = country.getCountryCode();
                            break;
                        }
                    }


                    SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity().getApplication());

                    sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, countryCode);
                    if (!userViewModel.isAuthenticationError()) {
                        userViewModel.getUserLiveData(email, password, fullName, countryCode, false).observe(getViewLifecycleOwner(), result -> {
                            circularProgressIndicator.setVisibility(View.GONE);
                            if (result.isSuccess()) {
                                userViewModel.setAuthenticationError(false);
                                Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_mainActivity);
                                requireActivity().finish();
                            } else {
                                Result.Error error = (Result.Error) result;
                                userViewModel.setAuthenticationError(true);
                            }
                        });
                    } else {
                        userViewModel.logUser(email, password, fullName, countryCode, false);
                    }
                }
                else {
                    confirmPasswordTextField.setError(getString(R.string.passwords_don_t_match));
                }
            }
            else {
                if(!checkPassword(password)) {
                    passwordTextField.setError(getString(R.string.password_must_be_at_least_6_characters));
                }
                if(!checkEmail(email)) {
                    emailTextField.setError(getString(R.string.email_is_not_valid));
                }
                if(countryCode.isEmpty()) {
                    countryPicker.setError(getString(R.string.please_insert_a_country));
                }
                if(fullName.isEmpty()) {
                    fullNameTextField.setError(getString(R.string.please_insert_a_name));
                }
            }
        });
        logInTextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_loginFragment);
        });

    }

    public void getCountries() {
        Collections.addAll(countries, new Country("AD", "Andorra"),
                new Country("AE", "United Arab Emirates"),
                new Country("AF", "Afghanistan"),
                new Country("AG", "Antigua and Barbuda"),
                new Country("AI", "Anguilla"),
                new Country("AL", "Albania"),
                new Country("AM", "Armenia"),
                new Country("AN", "Netherlands Antilles"),
                new Country("AO", "Angola"),
                new Country("AR", "Argentina"),
                new Country("AS", "American Samoa"),
                new Country("AT", "Austria"),
                new Country("AU", "Australia"),
                new Country("AW", "Aruba"),
                new Country("AZ", "Azerbaijan"),
                new Country("BA", "Bosnia and Herzegovina"),
                new Country("BB", "Barbados"),
                new Country("BD", "Bangladesh"),
                new Country("BE", "Belgium"),
                new Country("BF", "Burkina Faso"),
                new Country("BG", "Bulgaria"),
                new Country("BH", "Bahrain"),
                new Country("BI", "Burundi"),
                new Country("BJ", "Benin"),
                new Country("BM", "Bermuda"),
                new Country("BN", "Brunei Darussalam"),
                new Country("BO", "Bolivia"),
                new Country("BR", "Brazil"),
                new Country("BS", "Bahamas"),
                new Country("BT", "Bhutan"),
                new Country("BW", "Botswana"),
                new Country("BY", "Belarus"),
                new Country("BZ", "Belize"),
                new Country("CA", "Canada"),
                new Country("CD", "Democratic Republic of the Congo"),
                new Country("CF", "Central African Republic"),
                new Country("CG", "Congo"),
                new Country("CH", "Switzerland"),
                new Country("CI", "Côte d'Ivoire"),
                new Country("CL", "Chile"),
                new Country("CM", "Cameroon"),
                new Country("CN", "China"),
                new Country("CO", "Colombia"),
                new Country("CR", "Costa Rica"),
                new Country("CS", "Serbia and Montenegro"),
                new Country("CU", "Cuba"),
                new Country("CV", "Cape Verde"),
                new Country("CY", "Cyprus"),
                new Country("CZ", "Czech Republic"),
                new Country("DE", "Germany"),
                new Country("DJ", "Djibouti"),
                new Country("DK", "Denmark"),
                new Country("DM", "Dominica"),
                new Country("DO", "Dominican Republic"),
                new Country("DZ", "Algeria"),
                new Country("EC", "Ecuador"),
                new Country("EE", "Estonia"),
                new Country("EG", "Egypt"),
                new Country("EH", "Western Sahara"),
                new Country("ER", "Eritrea"),
                new Country("ES", "Spain"),
                new Country("ET", "Ethiopia"),
                new Country("FI", "Finland"),
                new Country("FJ", "Fiji"),
                new Country("FM", "Federated States of Micronesia"),
                new Country("FR", "France"),
                new Country("GA", "Gabon"),
                new Country("GB", "United Kingdom"),
                new Country("GD", "Grenada"),
                new Country("GE", "Georgia"),
                new Country("GF", "French Guiana"),
                new Country("GH", "Ghana"),
                new Country("GI", "Gibraltar"),
                new Country("GL", "Greenland"),
                new Country("GM", "Gambia"),
                new Country("GN", "Guinea"),
                new Country("GP", "Guadeloupe"),
                new Country("GQ", "Equatorial Guinea"),
                new Country("GR", "Greece"),
                new Country("GT", "Guatemala"),
                new Country("GU", "Guam"),
                new Country("GW", "Guinea-Bissau"),
                new Country("GY", "Guyana"),
                new Country("HK", "Hong Kong"),
                new Country("HN", "Honduras"),
                new Country("HR", "Croatia"),
                new Country("HT", "Haiti"),
                new Country("HU", "Hungary"),
                new Country("ID", "Indonesia"),
                new Country("IE", "Ireland"),
                new Country("IL", "Israel"),
                new Country("IN", "India"),
                new Country("IQ", "Iraq"),
                new Country("IR", "Iran"),
                new Country("IS", "Iceland"),
                new Country("IT", "Italy"),
                new Country("JM", "Jamaica"),
                new Country("JO", "Jordan"),
                new Country("JP", "Japan"),
                new Country("KE", "Kenya"),
                new Country("KG", "Kyrgyzstan"),
                new Country("KH", "Cambodia"),
                new Country("KI", "Kiribati"),
                new Country("KM", "Comoros"),
                new Country("KN", "Saint Kitts and Nevis"),
                new Country("KP", "North Korea"),
                new Country("KR", "South Korea"),
                new Country("KW", "Kuwait"),
                new Country("KY", "Cayman Islands"),
                new Country("KZ", "Kazakhstan"),
                new Country("LA", "Laos"),
                new Country("LB", "Lebanon"),
                new Country("LC", "Saint Lucia"),
                new Country("LI", "Liechtenstein"),
                new Country("LK", "Sri Lanka"),
                new Country("LR", "Liberia"),
                new Country("LS", "Lesotho"),
                new Country("LT", "Lithuania"),
                new Country("LU", "Luxembourg"),
                new Country("LV", "Latvia"),
                new Country("LY", "Libya"),
                new Country("MA", "Morocco"),
                new Country("MC", "Monaco"),
                new Country("MD", "Moldova"),
                new Country("MG", "Madagascar"),
                new Country("MH", "Marshall Islands"),
                new Country("MK", "North Macedonia"),
                new Country("ML", "Mali"),
                new Country("MM", "Myanmar"),
                new Country("MN", "Mongolia"),
                new Country("MO", "Macau"),
                new Country("MP", "Northern Mariana Islands"),
                new Country("MQ", "Martinique"),
                new Country("MR", "Mauritania"),
                new Country("MS", "Montserrat"),
                new Country("MT", "Malta"),
                new Country("MU", "Mauritius"),
                new Country("MV", "Maldives"),
                new Country("MW", "Malawi"),
                new Country("MX", "Mexico"),
                new Country("MY", "Malaysia"),
                new Country("MZ", "Mozambique"),
                new Country("NA", "Namibia"),
                new Country("NC", "New Caledonia"),
                new Country("NE", "Niger"),
                new Country("NG", "Nigeria"),
                new Country("NI", "Nicaragua"),
                new Country("NL", "Netherlands"),
                new Country("NO", "Norway"),
                new Country("NP", "Nepal"),
                new Country("NR", "Nauru"),
                new Country("NZ", "New Zealand"),
                new Country("OM", "Oman"),
                new Country("PA", "Panama"),
                new Country("PE", "Peru"),
                new Country("PF", "French Polynesia"),
                new Country("PG", "Papua New Guinea"),
                new Country("PH", "Philippines"),
                new Country("PK", "Pakistan"),
                new Country("PL", "Poland"),
                new Country("PN", "Pitcairn Islands"),
                new Country("PR", "Puerto Rico"),
                new Country("PS", "Palestinian territories"),
                new Country("PT", "Portugal"),
                new Country("PW", "Palau"),
                new Country("PY", "Paraguay"),
                new Country("QA", "Qatar"),
                new Country("RO", "Romania"),
                new Country("RU", "Russia"),
                new Country("RW", "Rwanda"),
                new Country("SA", "Saudi Arabia"),
                new Country("SB", "Solomon Islands"),
                new Country("SC", "Seychelles"),
                new Country("SD", "Sudan"),
                new Country("SE", "Sweden"),
                new Country("SG", "Singapore"),
                new Country("SI", "Slovenia"),
                new Country("SK", "Slovakia"),
                new Country("SL", "Sierra Leone"),
                new Country("SM", "San Marino"),
                new Country("SN", "Senegal"),
                new Country("SO", "Somalia"),
                new Country("SR", "Suriname"),
                new Country("ST", "São Tomé and Príncipe"),
                new Country("SV", "El Salvador"),
                new Country("SY", "Syria"),
                new Country("SZ", "Eswatini"),
                new Country("TC", "Turks and Caicos Islands"),
                new Country("TD", "Chad"),
                new Country("TG", "Togo"),
                new Country("TH", "Thailand"),
                new Country("TJ", "Tajikistan"),
                new Country("TL", "East Timor"),
                new Country("TM", "Turkmenistan"),
                new Country("TN", "Tunisia"),
                new Country("TO", "Tonga"),
                new Country("TR", "Turkey"),
                new Country("TT", "Trinidad and Tobago"),
                new Country("TV", "Tuvalu"),
                new Country("TW", "Taiwan"),
                new Country("TZ", "Tanzania"),
                new Country("UA", "Ukraine"),
                new Country("UG", "Uganda"),
                new Country("US", "United States"),
                new Country("UY", "Uruguay"),
                new Country("UZ", "Uzbekistan"),
                new Country("VC", "Saint Vincent and the Grenadines"),
                new Country("VE", "Venezuela"),
                new Country("VG", "British Virgin Islands"),
                new Country("VI", "United States Virgin Islands"),
                new Country("VN", "Vietnam"),
                new Country("VU", "Vanuatu"),
                new Country("WS", "Samoa"),
                new Country("YE", "Yemen"),
                new Country("ZA", "South Africa"),
                new Country("ZM", "Zambia"),
                new Country("ZW", "Zimbabwe")
        );
        countryNames = new String[countries.size()];
        for(int i = 0; i < countries.size(); i++) {
            countryNames[i] = countries.get(i).getCountryName();
        }
        countryPicker.setSimpleItems(countryNames);
    }
    @Override
    public void onStart() {
        super.onStart();
        if(userViewModel != null && userViewModel.getLoggedUser() != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_mainActivity);
            requireActivity().finish();
        }
    }

    public boolean checkEmail(String email) {
        if(email.isEmpty())
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean checkPassword(String password) {
        if(password.isEmpty())
            return false;
        return password.length() >= 6;
    }
}