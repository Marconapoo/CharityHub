package it.sal.disco.unimib.charityhub.data.repositories.project;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.BaseProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public class ProjectRepositoryTest {


    @Mock
    private BaseProjectDataSource projectDataSource;

    @Mock
    private BaseProjectLocalDataSource projectLocalDataSource;

    private ProjectRepository projectRepository;

    @Mock
    private MutableLiveData<Result> projectsLiveData;
    @Mock
    private MutableLiveData<Result> themesLiveData;
    @Mock
    private MutableLiveData<Result> imagesLiveData;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        projectRepository = new ProjectRepository(projectDataSource, projectLocalDataSource);
        //projectsLiveData = projectRepository.getDownloadedProjects();
        //themesLiveData = projectRepository.getThemesLiveData();
        //imagesLiveData = projectRepository.getImagesLiveData("projectId");
        projectRepository.setProjectsLiveData(projectsLiveData);
        projectRepository.setThemesLiveData(themesLiveData);
        projectRepository.setImagesLiveData(imagesLiveData);
    }

    @Test
    public void testSearchForProjects() {
        projectRepository.searchForProjects("filter", 1, true, "country");
        verify(projectLocalDataSource).getProjectsByCountry("country");
    }

    @Test
    public void testGetProjectsByTheme() {
        projectRepository.getProjectsByTheme("themeName", "country");
        verify(projectLocalDataSource).getProjectsByTheme("themeName", "country");
    }

    @Test
    public void testGetProjectsByCountry() {
        projectRepository.getProjectsByCountry("country");
        verify(projectLocalDataSource).getProjectsByCountry("country");
    }

    @Test
    public void testSearchProjects() {
        projectRepository.searchProjects("filter", 1);
        verify(projectDataSource).searchForProjects("filter", 1);
    }

    @Test
    public void testGetDownloadedProjects() {
        projectRepository.getDownloadedProjects();
        verify(projectLocalDataSource).getProjects();
    }

    @Test
    public void testGetThemesLiveData() {
        projectRepository.getThemesLiveData();
        verify(projectDataSource).getThemes();
    }

    @Test
    public void testGetImagesLiveData() {
        projectRepository.getImagesLiveData("projectId");
        verify(projectDataSource).getImages("projectId");
    }

    @Test
    public void testOnProjectsLoaded() {
        ProjectsApiResponse projectsApiResponse = mock(ProjectsApiResponse.class);
        ProjectsApiResponse.Search search = mock(ProjectsApiResponse.Search.class);
        ProjectsApiResponse.Response response = mock(ProjectsApiResponse.Response.class);
        ProjectsApiResponse.ProjectData projectData = mock(ProjectsApiResponse.ProjectData.class);
        List<Project> projectList = new ArrayList<>();
        Project project = mock(Project.class);

        projectList.add(project);  // Add a mock project to the list

        // Mock nested calls to avoid NullPointerException
        when(projectsApiResponse.getSearch()).thenReturn(search);
        when(search.getResponse()).thenReturn(response);
        when(response.getProjectData()).thenReturn(projectData);
        when(projectData.getProjectList()).thenReturn(projectList);
        when(project.getTitle()).thenReturn("Project Title");

        projectRepository.onProjectsLoaded(projectsApiResponse);

        verify(projectLocalDataSource).insertProjects(projectsApiResponse);
    }

    @Test
    public void testOnFailureFromRemote() {
        String error = "error message";
        projectRepository.onFailureFromRemote(error);

        ArgumentCaptor<Result.Error> captor = ArgumentCaptor.forClass(Result.Error.class);
        verify(projectsLiveData).postValue(captor.capture());
        assertEquals(error, captor.getValue().getErrorMessage());
    }

    @Test
    public void testOnThemesLoaded() {
        ThemesApiResponse themesApiResponse = mock(ThemesApiResponse.class);
        projectRepository.onThemesLoaded(themesApiResponse);

        ArgumentCaptor<Result.ThemesResponseSuccess> captor = ArgumentCaptor.forClass(Result.ThemesResponseSuccess.class);
        verify(themesLiveData).postValue(captor.capture());
        assertEquals(themesApiResponse, captor.getValue().getThemesApiResponse());
    }

    @Test
    public void testOnFailureThemesLoaded() {
        String error = "error message";
        projectRepository.onFailureThemesLoaded(error);

        ArgumentCaptor<Result.Error> captor = ArgumentCaptor.forClass(Result.Error.class);
        verify(themesLiveData).postValue(captor.capture());
        assertEquals(error, captor.getValue().getErrorMessage());
    }

    @Test
    public void testOnSuccessImagesLoaded() {
        ImagesApiResponse imagesApiResponse = mock(ImagesApiResponse.class);
        projectRepository.onSuccessImagesLoaded(imagesApiResponse);

        ArgumentCaptor<Result.ImagesResponseSuccess> captor = ArgumentCaptor.forClass(Result.ImagesResponseSuccess.class);
        verify(imagesLiveData).postValue(captor.capture());
        assertEquals(imagesApiResponse, captor.getValue().getImagesApiResponse());
    }

    @Test
    public void testOnFailureImagesLoaded() {
        String error = "error message";
        projectRepository.onFailureImagesLoaded(error);

        ArgumentCaptor<Result.Error> captor = ArgumentCaptor.forClass(Result.Error.class);
        verify(imagesLiveData).postValue(captor.capture());
        assertEquals(error, captor.getValue().getErrorMessage());
    }

    @Test
    public void testOnLocalSuccess() {
        ProjectsApiResponse projectsApiResponse = mock(ProjectsApiResponse.class);
        projectRepository.onLocalSuccess(projectsApiResponse);

        ArgumentCaptor<Result.ProjectResponseSuccess> captor = ArgumentCaptor.forClass(Result.ProjectResponseSuccess.class);
        verify(projectsLiveData).postValue(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(projectsApiResponse, captor.getValue().getProjectsApiResponse());
    }

    @Test
    public void testOnProjectsLocalSuccess() {
        List<Project> projectList = mock(List.class);
        projectRepository.onProjectsLocalSuccess(projectList);

        ArgumentCaptor<Result.ProjectResponseSuccess> captor = ArgumentCaptor.forClass(Result.ProjectResponseSuccess.class);
        verify(projectsLiveData).postValue(captor.capture());
        assertEquals(projectList, captor.getValue().getProjectList());
    }
}

