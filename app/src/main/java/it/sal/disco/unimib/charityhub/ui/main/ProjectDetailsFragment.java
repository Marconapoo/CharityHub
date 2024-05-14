package it.sal.disco.unimib.charityhub.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.ImageAdapter;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectDataSource;
import it.sal.disco.unimib.charityhub.data.source.projects.ProjectLocalDataSource;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.Image;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.utils.Constants;


public class ProjectDetailsFragment extends Fragment {

    Project project;
    List<Image> imagesArrays;

    ImageAdapter imageAdapter;
    RecyclerView recyclerView;
    Button donateButton;
    ProjectDetailsViewModel projectDetailsViewModel;


    public ProjectDetailsFragment() {
        // Required empty public constructor
    }

    public static ProjectDetailsFragment newInstance(String param1, String param2) {
        ProjectDetailsFragment fragment = new ProjectDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectDataSource projectDataSource = new ProjectDataSource();
        ProjectLocalDataSource projectLocalDataSource = new ProjectLocalDataSource(requireActivity().getApplicationContext());
        ProjectRepository projectRepository = new ProjectRepository(projectDataSource, projectLocalDataSource);
        projectDetailsViewModel = new ViewModelProvider(this, new HomeViewModelFactory(projectRepository)).get(ProjectDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //((MainActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        project = ProjectDetailsFragmentArgs.fromBundle(getArguments()).getProject();
        recyclerView = view.findViewById(R.id.images_recycler_view);
        donateButton = view.findViewById(R.id.donateButton);
        imagesArrays = new ArrayList<>();
        imageAdapter = new ImageAdapter(imagesArrays, requireContext(), project);
        recyclerView.setLayoutManager(new CarouselLayoutManager(new FullScreenCarouselStrategy()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);
        SnapHelper snapHelper = new CarouselSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        Log.w("Details fragment", project.getDonationOptions().getDonationOptionList().get(0).getDescription());
        projectDetailsViewModel.getImagesLiveData(String.valueOf(project.getId())).observe(getViewLifecycleOwner(), result -> {
            Log.e("Details fragment", "OBSERVER");
            if(result.isSuccess()) {
                imagesArrays.clear();
                ImagesApiResponse imagesApiResponse = ((Result.ImagesResponseSuccess) result).getImagesApiResponse();

                for(ImagesApiResponse.ImagesArray images : imagesApiResponse.getImagesObject().getImagesArrays()) {
                    for(Image image : images.getImageLinks()) {
                        if(image.getSize().equals(Constants.IMAGE_ORIGINAL_SIZE_FIELD))
                            imagesArrays.add(image);
                        if(imagesArrays.size() == 4)
                            break;
                    }
                }
                while(imagesArrays.size() < 4) {
                    imagesArrays.add(new Image());
                }

                imageAdapter.notifyItemRangeInserted(0, imagesArrays.size());
            }
            else {
                Log.e("Details Fragment", ((Result.Error) result).getErrorMessage());
                while(imagesArrays.size() < 4) {
                    imagesArrays.add(new Image());
                }

                imageAdapter.notifyItemRangeInserted(0, imagesArrays.size());
            }
        });

        donateButton.setOnClickListener(v -> {
            if(project != null) {
                ProjectDetailsFragmentDirections.ActionProjectDetailsFragmentToDonationFragment action = ProjectDetailsFragmentDirections.actionProjectDetailsFragmentToDonationFragment(project);
                Navigation.findNavController(v).navigate(action);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //((MainActivity) requireActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /*
    public void setUpUI() {
        summmary.setText(project.getSummary());
        challenge.setText(project.getChallenge());
        solution.setText(project.getSolution());
        longTermImpact.setText(project.getLongTermImpact());
    }*/
}