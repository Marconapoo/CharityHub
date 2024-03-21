package it.sal.disco.unimib.charityhub.data.service;

import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectApiService {

    @GET("themes/{themeId}/projects")
    Call<ProjectsApiResponse> getProjectsByTheme(
        @Path("themeId") String themeId,
        @Query("api_key") String api_key,
        @Query("nextProjectId") Integer projectId,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );

    @GET("all/projects/ids")
    Call<ProjectsApiResponse> getAllProjectsIds(
        @Query("api_key") String api_key,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );

    @GET("countries/{iso3166CountryCode}/projects")
    Call<ProjectsApiResponse> getAllProjectsByCountry(
        @Query("api_key") String api_key,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );
}
