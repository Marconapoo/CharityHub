package it.sal.disco.unimib.charityhub.data.source.projects;

import it.sal.disco.unimib.charityhub.data.service.ProjectApiService;
import it.sal.disco.unimib.charityhub.model.ImagesApiResponse;
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
                .baseUrl(Constants.GLOBALGIVING_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.projectApiService = retrofit.create(ProjectApiService.class);
    }

    @Override
    public void searchForProjects(String filter, Integer nextProjectId) {
        Call<ProjectsApiResponse> projectsApiResponseCall = projectApiService.searchForProjects(Constants.API_KEY, "*", filter, nextProjectId,"application/json", "application/json");
        projectsApiResponseCall.enqueue(new Callback<ProjectsApiResponse>() {
            @Override
            public void onResponse(Call<ProjectsApiResponse> call, Response<ProjectsApiResponse> response) {
                if(response.body() != null && response.isSuccessful()) {
                    if(response.body().getSearch().getResponse().getNumberFound() > 0 && response.body().getSearch().getResponse().getProjectData() != null)
                        projectCallback.onProjectsLoaded(response.body());
                    else
                        projectCallback.onFailureFromRemote("Nessun progetto trovato");
                }
                else {
                    projectCallback.onFailureFromRemote("Nessun progetto trovato");
                }
            }

            @Override
            public void onFailure(Call<ProjectsApiResponse> call, Throwable t) {
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

    @Override
    public void getImages(String projectId) {
        Call<ImagesApiResponse> imagesApiResponseCall = projectApiService.getImages(projectId, Constants.API_KEY, "application/json", "application/json");

        imagesApiResponseCall.enqueue(new Callback<ImagesApiResponse>() {
            @Override
            public void onResponse(Call<ImagesApiResponse> call, Response<ImagesApiResponse> response) {
                if(response.body() != null && response.isSuccessful()) {
                    projectCallback.onSuccessImagesLoaded(response.body());
                }
                else {
                    projectCallback.onFailureImagesLoaded("No images were found");
                }
            }

            @Override
            public void onFailure(Call<ImagesApiResponse> call, Throwable t) {
                    projectCallback.onFailureImagesLoaded(t.getLocalizedMessage());
            }
        });
    }
}
