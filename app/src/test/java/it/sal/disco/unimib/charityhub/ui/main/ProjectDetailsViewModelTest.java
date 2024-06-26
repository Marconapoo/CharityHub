package it.sal.disco.unimib.charityhub.ui.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;

public class ProjectDetailsViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private ProjectRepository projectRepository;
    private ProjectDetailsViewModel projectDetailsViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        projectDetailsViewModel = new ProjectDetailsViewModel(projectRepository);
    }

    @Test
    public void getImagesLiveData() {
        MutableLiveData<Result> expectedLiveData = new MutableLiveData<>();

        Result.ImagesResponseSuccess expectedResult = new Result.ImagesResponseSuccess(new ImagesApiResponse());

        expectedLiveData.setValue(expectedResult);

        when(projectRepository.getImagesLiveData(anyString())).thenReturn(expectedLiveData);

        MutableLiveData<Result> actualLiveData = projectDetailsViewModel.getImagesLiveData(anyString());

        Observer<Result> observer = result -> {
          assertNotNull(result);
          assertTrue(result instanceof Result.ImagesResponseSuccess);
          Result.ImagesResponseSuccess responseSuccess = (Result.ImagesResponseSuccess) result;
          assertEquals(expectedResult, responseSuccess);
          assertEquals(expectedLiveData, actualLiveData);
        };
        actualLiveData.observeForever(observer);

        verify(projectRepository).getImagesLiveData(anyString());
        actualLiveData.removeObserver(observer);

    }

}