package it.sal.disco.unimib.charityhub.data.source.projects;

import android.app.Application;

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

    public ProjectLocalDataSource(Application application) {
        projectDAO = ProjectsRoomDatabase.getDatabase(application).projectDAO();
        this.sharedPreferencesUtil = new SharedPreferencesUtil(application);
    }


    public void insertProjects(ProjectsApiResponse projectsApiResponse) {
        ProjectsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Project> allProjects = projectDAO.getAll();
            List<Project> projectList = projectsApiResponse.getSearch().getResponse().getProjectData().getProjectList();

            if(projectList != null) {
                for(Project project : allProjects) {
                    if(projectList.contains(project)) {

                    }
                }
            }

        });
    }


    @Override
    public void deleteAll() {
        sharedPreferencesUtil.deleteAll(Constants.SHARED_PREFERENCES_FILE_NAME);
    }
}
