package it.sal.disco.unimib.charityhub.ui.main;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.Theme;

public class HomeViewModelTest {

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
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        when(projectRepository.searchForProjects(anyString(), anyInt(), anyBoolean(), anyString())).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.searchForProjects("filter", 1, true, "country");

        assertEquals(expectedLiveData, actualLiveData);
        verify(projectRepository).searchForProjects("filter", 1, true, "country");
    }

    @Test
    public void testGetDownloadedProjects() {
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        when(projectRepository.getDownloadedProjects()).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.getDownloadedProjects();

        assertEquals(expectedLiveData, actualLiveData);
        verify(projectRepository).getDownloadedProjects();
    }

    @Test
    public void testGetThemesLiveData() {
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();
        when(projectRepository.getThemesLiveData()).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = homeViewModel.getThemesLiveData();

        assertEquals(expectedLiveData, actualLiveData);
        verify(projectRepository).getThemesLiveData();
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