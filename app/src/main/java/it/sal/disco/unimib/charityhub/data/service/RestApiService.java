package it.sal.disco.unimib.charityhub.data.service;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;
import it.sal.disco.unimib.charityhub.model.Country;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("all")
    Call<List<Country>> getCountries(
        @Query("fields") String field
    );
}
