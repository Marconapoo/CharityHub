package it.sal.disco.unimib.charityhub.data.source.countries;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;

public abstract class BaseCountryDataSource {

    protected RestCallback restCallback;

    public void setRestCallback(RestCallback restCallback) {
        this.restCallback = restCallback;
    }

    public abstract void getCountries(String fields);

}
