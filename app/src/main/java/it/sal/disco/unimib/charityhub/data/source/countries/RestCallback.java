package it.sal.disco.unimib.charityhub.data.source.countries;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;
import it.sal.disco.unimib.charityhub.model.Country;

public interface RestCallback {

    void onCountriesLoaded(List<Country> countriesApiResponse);
    void onFailure(String error);
}
