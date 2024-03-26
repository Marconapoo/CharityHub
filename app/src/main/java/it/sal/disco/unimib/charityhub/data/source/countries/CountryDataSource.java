package it.sal.disco.unimib.charityhub.data.source.countries;

import android.util.Log;

import java.util.List;

import it.sal.disco.unimib.charityhub.data.service.RestApiService;
import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;
import it.sal.disco.unimib.charityhub.model.Country;
import it.sal.disco.unimib.charityhub.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryDataSource extends BaseCountryDataSource {

    private final RestApiService restApiService;

    public CountryDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApiService = retrofit.create(RestApiService.class);
    }

    @Override
    public void getCountries(String fields) {
        Call<List<Country>> countriesApiResponseCall = restApiService.getCountries(fields);

        countriesApiResponseCall.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    Log.e("Country Data source", String.valueOf(response.body().size()));
                    restCallback.onCountriesLoaded(response.body());
                }
                else {
                    restCallback.onFailure("ERRORE");
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                restCallback.onFailure(t.getLocalizedMessage());
            }
        });
    }
}
