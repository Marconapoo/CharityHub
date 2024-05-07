package it.sal.disco.unimib.charityhub.model.countries;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("name")
    private CountryName name;

    private String countryName;

    @SerializedName("cca2")
    private String countryCode;

    public Country(String countryCode, String name) {
        this.countryCode = countryCode;
        this.countryName = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public CountryName getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static class CountryName {
        @SerializedName("common")
        private String commonName;

        public String getCommonName() {
            return commonName;
        }
    }
}

