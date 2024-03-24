package it.sal.disco.unimib.charityhub.data.source.projects;

import android.util.Log;

import it.sal.disco.unimib.charityhub.data.service.ProjectApiService;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;
import it.sal.disco.unimib.charityhub.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectDataSource extends BaseProjectDataSource {

    private final ProjectApiService projectApiService;

    public ProjectDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.projectApiService = retrofit.create(ProjectApiService.class);
    }

    @Override
    public void getProjectsByTheme(String themeID, Integer nextProjectId) {
        Call<ProjectsApiResponse> projectsApiResponseCall = projectApiService.getProjectsByTheme(themeID, Constants.API_KEY, nextProjectId, "application/json", "application/json");

        projectsApiResponseCall.enqueue(new Callback<ProjectsApiResponse>() {
            @Override
            public void onResponse(Call<ProjectsApiResponse> call, Response<ProjectsApiResponse> response) {
                if(response.body() != null && response.isSuccessful()) {

                    projectCallback.onProjectsLoaded(response.body());
                }
                else {
                    Log.e("Project Data Source", "Dentro on response");
                    projectCallback.onFailureFromRemote(response.message());
                }
            }

            @Override
            public void onFailure(Call<ProjectsApiResponse> call, Throwable t) {
                Log.e("Project Data Source", "Dentro on failure");
                projectCallback.onFailureFromRemote(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getThemes() {
        Call<ThemesApiResponse> themesApiResponseCall = projectApiService.getThemes(Constants.API_KEY, "application/json", "application/json");

        themesApiResponseCall.enqueue(new Callback<ThemesApiResponse>() {
            @Override
            public void onResponse(Call<ThemesApiResponse> call, Response<ThemesApiResponse> response) {
                if(response.body() != null && response.isSuccessful()) {
                    projectCallback.onThemesLoaded(response.body());
                }
                else {
                    projectCallback.onFailureThemesLoaded(response.message());
                }
            }

            @Override
            public void onFailure(Call<ThemesApiResponse> call, Throwable t) {
                projectCallback.onFailureThemesLoaded(t.getLocalizedMessage());
            }
        });
    }
}
