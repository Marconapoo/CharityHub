package it.sal.disco.unimib.charityhub.ui.main;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.versioning.AndroidVersions;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.Theme;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;

public class HomeViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private ProjectRepository projectRepository;
    private HomeViewModel homeViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        homeViewModel = new HomeViewModel(projectRepository);
    }


    @Test
    public void testSearchForProjects() {

        List<Project> expectedProjectList = new ArrayList<>();
        expectedProjectList.add(new Project());
        expectedProjectList.add(new Project());
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        Result.ProjectResponseSuccess expectedResult = new Result.ProjectResponseSuccess(expectedProjectList);
        expectedLiveData.setValue(expectedResult);
        when(projectRepository.searchForProjects(anyString(), anyInt(), anyBoolean(), anyString())).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.searchForProjects("filter", 1, true, "country");

        Observer<Result> observer = result -> {
            Result actualResult = actualLiveData.getValue();
            assertTrue(actualResult instanceof Result.ProjectResponseSuccess);
            Result.ProjectResponseSuccess projectResponse = (Result.ProjectResponseSuccess) actualResult;

            assertEquals(expectedProjectList, projectResponse.getProjectList());
            assertEquals(expectedLiveData, actualLiveData);
        };

        actualLiveData.observeForever(observer);

        verify(projectRepository).searchForProjects("filter", 1, true, "country");

        actualLiveData.removeObserver(observer);
    }

    @Test
    public void testGetDownloadedProjects() {


        List<Project> expectedProjectList = new ArrayList<>();
        expectedProjectList.add(new Project());
        expectedProjectList.add(new Project());
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        Result.ProjectResponseSuccess expectedResult = new Result.ProjectResponseSuccess(expectedProjectList);
        expectedLiveData.setValue(expectedResult);
        when(projectRepository.getDownloadedProjects()).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.getDownloadedProjects();

        Observer<Result> observer = result -> {
            Result actualResult = actualLiveData.getValue();
            assertTrue(actualResult instanceof Result.ProjectResponseSuccess);
            Result.ProjectResponseSuccess projectResponse = (Result.ProjectResponseSuccess) actualResult;

            assertEquals(expectedProjectList, projectResponse.getProjectList());
            assertEquals(expectedLiveData, actualLiveData);
        };

        actualLiveData.observeForever(observer);


        verify(projectRepository).getDownloadedProjects();

        actualLiveData.removeObserver(observer);
    }

    @Test
    public void testGetThemesLiveData() {
        List<Theme> expectedThemes = new ArrayList<>();
        expectedThemes.add(new Theme("TS", "Test 1"));
        expectedThemes.add(new Theme("TS2", "Test 2"));
        ThemesApiResponse expectedThemesApiResponse = new ThemesApiResponse(expectedThemes);
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        Result.ThemesResponseSuccess expectedResult = new Result.ThemesResponseSuccess(expectedThemesApiResponse);
        expectedLiveData.setValue(expectedResult);

        when(projectRepository.getThemesLiveData()).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.getThemesLiveData();

        // Add an observer to trigger the LiveData update
        Observer<Result> observer = result -> {
            assertNotNull(result);
            assertTrue(result instanceof Result.ThemesResponseSuccess);
            Result.ThemesResponseSuccess responseSuccess = (Result.ThemesResponseSuccess) result;

            assertEquals(expectedResult, responseSuccess);
            assertEquals(expectedResult.getThemesApiResponse().getThemeData(), responseSuccess.getThemesApiResponse().getThemeData());
            assertEquals(expectedLiveData, actualLiveData);
        };
        actualLiveData.observeForever(observer);

        verify(projectRepository).getThemesLiveData();

        actualLiveData.removeObserver(observer);
    }

    @Test
    public void testSetAndGetCurrentTheme() {
        Theme theme = new Theme("", "");
        homeViewModel.setCurrentTheme(theme);
        assertEquals(theme, homeViewModel.getCurrentTheme());
    }

    @Test
    public void testSetAndIsLoading() {
        homeViewModel.setLoading(true);
        assertTrue(homeViewModel.isLoading());
    }

    @Test
    public void testSetAndIsFirstLoading() {
        homeViewModel.setFirstLoading(true);
        assertTrue(homeViewModel.isFirstLoading());
    }

    @Test
    public void testSetAndIsNoMoreProjects() {
        homeViewModel.setNoMoreProjects(true);
        assertTrue(homeViewModel.isNoMoreProjects());
    }

    @Test
    public void testSearchProjects() {
        homeViewModel.searchProjects("filter", 1, true);
        verify(projectRepository).searchProjects("filter", 1);
    }

    @Test
    public void testGetProjectsByTheme() {
        homeViewModel.getProjectsByTheme("themeName", "country");
        verify(projectRepository).getProjectsByTheme("themeName", "country");
    }

    @Test
    public void testGetProjectsByCountry() {
        homeViewModel.getProjectsByCountry("country");
        verify(projectRepository).getProjectsByCountry("country");
    }

}