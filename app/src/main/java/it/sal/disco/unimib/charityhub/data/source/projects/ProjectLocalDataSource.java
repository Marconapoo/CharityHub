package it.sal.disco.unimib.charityhub.data.source.projects;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

import it.sal.disco.unimib.charityhub.data.database.ProjectDAO;
import it.sal.disco.unimib.charityhub.data.database.ProjectsRoomDatabase;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class ProjectLocalDataSource extends BaseProjectLocalDataSource {

    private final SharedPreferencesUtil sharedPreferencesUtil;
    private final ProjectDAO projectDAO;

    public ProjectLocalDataSource(Context application) {
        projectDAO = ProjectsRoomDatabase.getDatabase(application).projectDAO();
        this.sharedPreferencesUtil = new SharedPreferencesUtil(application);
    }

    @Override
    public void insertProjects(ProjectsApiResponse projectsApiResponse) {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Project> projectList = projectsApiResponse.getSearch().getResponse().getProjectData().getProjectList();
            for(Project project : projectList) {
                Log.w("PROJECT LOCAL DATA SOURCE", project.getTitle());
            }
            projectDAO.insertProjects(projectList);
            projectCallback.onLocalSuccess(projectsApiResponse);
        });
    }

    @Override
    public void getProjects() {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Project> projectList = projectDAO.getAll();
            projectCallback.onProjectsLocalSuccess(projectList);
        });
    }

    @Override
    public void getProjectsByCountry(String country) {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Project> projectList = projectDAO.getProjectsByCountry(country);
            projectCallback.onProjectsLocalSuccess(projectList);
        });
    }

    @Override
    public void getProjectsByTheme(String theme, String country) {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Project> projectList = projectDAO.getProjectsByTheme(theme, country);
            projectCallback.onProjectsLocalSuccess(projectList);
        });
    }

    @Override
    public void deleteAll() {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            projectDAO.deleteAll();
            sharedPreferencesUtil.deleteAll(Constants.SHARED_PREFERENCES_FILE_NAME);
        });
    }
}
