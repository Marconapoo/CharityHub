package it.sal.disco.unimib.charityhub.data.repositories.countries;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.sal.disco.unimib.charityhub.data.source.countries.BaseCountryDataSource;
import it.sal.disco.unimib.charityhub.data.source.countries.CountryDataSource;
import it.sal.disco.unimib.charityhub.data.source.countries.RestCallback;
import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;
import it.sal.disco.unimib.charityhub.model.Country;
import it.sal.disco.unimib.charityhub.model.Result;

public class CountryRepository implements ICountryRepository, RestCallback {

    private final BaseCountryDataSource countryDataSource;
    private final MutableLiveData<Result> countriesLiveData;


    public CountryRepository() {
        countryDataSource = new CountryDataSource();
        countriesLiveData = new MutableLiveData<>();
        countryDataSource.setRestCallback(this);
    }

    public MutableLiveData<Result> getCountriesLiveData() {
        countryDataSource.getCountries("name,cca2");
        return countriesLiveData;
    }

    @Override
    public void onCountriesLoaded(List<Country> countriesApiResponse) {
        Result.CountriesResponseSucccess result = new Result.CountriesResponseSucccess(countriesApiResponse);
        countriesLiveData.postValue(result);
    }

    @Override
    public void onFailure(String error) {
        Result.Error result = new Result.Error(error);
        countriesLiveData.postValue(result);
    }

}
