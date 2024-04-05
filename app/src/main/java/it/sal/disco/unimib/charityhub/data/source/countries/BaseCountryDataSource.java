package it.sal.disco.unimib.charityhub.data.source.countries;

public abstract class BaseCountryDataSource {

    protected RestCallback restCallback;

    public void setRestCallback(RestCallback restCallback) {
        this.restCallback = restCallback;
    }

    public abstract void getCountries(String fields);

}
