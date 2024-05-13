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

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    CircularProgressIndicator circularProgressIndicator;
    String[] countryNames;
    String currentCountry;
    String newCountry;
    String currentCountryName;
    List<Country> countries;
    boolean firstCountryChange;
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
        userViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory(requireActivity().getApplicationContext())).get(UserViewModel.class);
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
        circularProgressIndicator = view.findViewById(R.id.progressIndicator);
        TextInputEditText fullName = view.findViewById(R.id.fullNameEditText);
        TextInputEditText email = view.findViewById(R.id.emailEditText);
        TextInputLayout fullNameText = view.findViewById(R.id.fullName);
        TextInputLayout emailText = view.findViewById(R.id.email);
        TextInputLayout countryText = view.findViewById(R.id.countryPicker);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button undoButton = view.findViewById(R.id.undoButton);
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        currentCountry = sharedPreferencesUtil.readStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST);
        firstCountryChange = true;
        countries = new ArrayList<>();
        getCountries();
        setCountries(countryNames);
        //Log.e("Account fragment", userViewModel.getLoggedUser().getCountryOfInterest());

        user = userViewModel.getLoggedUser();

        fullName.setText(user.getName());
        email.setText(user.getEmail());

        logOutButton.setOnClickListener(v -> userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
            Navigation.findNavController(v).navigate(R.id.action_accountFragment_to_welcomeActivity);
            requireActivity().finish();
        }));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryText.setEnabled(true);
                countryPicker.setText("");
                logOutButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.VISIBLE);
                undoButton.setVisibility(View.VISIBLE);
            }
        });

        confirmButton.setOnClickListener(v -> {
            newCountry = null;
            circularProgressIndicator.setVisibility(View.VISIBLE);
            if (countryPicker.getText() != null) {
                for (Country country : countries) {
                    if (country.getCountryName().equals(countryPicker.getText().toString()) && !currentCountry.equals(country.getCountryCode())) {
                        newCountry = country.getCountryCode();
                        break;
                    }
                }
            }

            if (newCountry != null) {
                if(firstCountryChange) {
                    userViewModel.changeUserCountry(new User(user.getName(), user.getEmail(), user.getUid(), newCountry)).observe(getViewLifecycleOwner(), result -> {
                        if (result.isSuccess()) {
                            firstCountryChange = false;
                            User user = ((Result.UserResponseSuccess) result).getUser();
                            sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, newCountry);
                            currentCountry = newCountry;
                            countryText.setEnabled(false);
                            circularProgressIndicator.setVisibility(View.GONE);
                            logOutButton.setVisibility(View.VISIBLE);
                            editButton.setVisibility(View.VISIBLE);
                            confirmButton.setVisibility(View.GONE);
                            undoButton.setVisibility(View.GONE);
                            Log.e("Account fragment", user.toString());
                        } else {
                            Log.e("Account fragment", ((Result.Error) result).getErrorMessage());
                        }
                    });
                }
                else {
                    userViewModel.changeUserInformation(new User(user.getName(), user.getEmail(), user.getUid(), newCountry));
                }
            }
        });

        undoButton.setOnClickListener(v -> {
            if(currentCountryName != null)
                countryPicker.setText(currentCountryName);
            countryText.setEnabled(false);
            logOutButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.GONE);
            undoButton.setVisibility(View.GONE);
        });

    }

    public void setCountries(String[] countryNames) {
        currentCountryName = null;
        for(Country country : countries) {
            if(country.getCountryCode().equals(currentCountry)) {
                currentCountryName = country.getCountryName();
                break;
            }
        }
        if(countryPicker != null) {
            Log.w("Account fragment", "" + countryNames.length);
            countryPicker.setSimpleItems(countryNames);
            countryPicker.setText(currentCountryName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firstCountryChange = true;
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
    }
}