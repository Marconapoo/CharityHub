package it.sal.disco.unimib.charityhub.data.service;

import it.sal.disco.unimib.charityhub.model.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectApiService {

    @GET("services/search/projects")
    Call<ProjectsApiResponse> searchForProjects(
        @Query("api_key") String api_key,
        @Query("q") String query,
        @Query("filter") String filter,
        @Query("start") Integer projectId,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );

    @GET("projectservice/themes")
    Call<ThemesApiResponse> getThemes(
        @Query("api_key") String api_key,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );

    @GET("projectservice/projects/{projectid}/imagegallery")
    Call<ImagesApiResponse> getImages(
        @Path("projectid") String projectId,
        @Query("api_key") String api_key,
        @Header("Content-Type") String content_type,
        @Header("Accept") String accept
    );
}
