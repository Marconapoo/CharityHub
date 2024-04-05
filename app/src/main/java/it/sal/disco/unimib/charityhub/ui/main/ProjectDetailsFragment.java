package it.sal.disco.unimib.charityhub.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.ImageAdapter;
import it.sal.disco.unimib.charityhub.model.projects.Image;
import it.sal.disco.unimib.charityhub.model.projects.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.Result;


public class ProjectDetailsFragment extends Fragment {

    TextView summmary;
    TextView challenge;
    TextView solution;
    TextView longTermImpact;
    Project project;
    List<Image> imagesArrays;

    ImageAdapter imageAdapter;
    RecyclerView recyclerView;

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
        projectDetailsViewModel = new ViewModelProvider(this, new HomeViewModelFactory(requireActivity().getApplication())).get(ProjectDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //summmary = view.findViewById(R.id.summary);
        //challenge = view.findViewById(R.id.challenge);
        //solution = view.findViewById(R.id.solution);
        //longTermImpact = view.findViewById(R.id.longTermImpact);
        project = ProjectDetailsFragmentArgs.fromBundle(getArguments()).getProject();
        recyclerView = view.findViewById(R.id.images_recycler_view);

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
                        if(image.getSize().equals("orginal"))
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
            }
        });

        //setUpUI();
    }


    /*
    public void setUpUI() {
        summmary.setText(project.getSummary());
        challenge.setText(project.getChallenge());
        solution.setText(project.getSolution());
        longTermImpact.setText(project.getLongTermImpact());
    }*/
}