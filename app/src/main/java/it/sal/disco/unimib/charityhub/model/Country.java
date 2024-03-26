package it.sal.disco.unimib.charityhub.model;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("name")
    private CountryName name;

    @SerializedName("cca2")
    private String countryCode;

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

