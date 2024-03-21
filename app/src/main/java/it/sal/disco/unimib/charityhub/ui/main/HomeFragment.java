package it.sal.disco.unimib.charityhub.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.ProjectAdapter;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Project;
import it.sal.disco.unimib.charityhub.model.Result;

public class HomeFragment extends Fragment {


    HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.projectsRV);
        homeViewModel.setFirstLoading(true);
        List<Project> projectList = new ArrayList<>();
        ProjectAdapter projectAdapter = new ProjectAdapter(projectList, requireContext());
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);
        homeViewModel.getProjectsLiveData("env", null).observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccess()) {

                List<Project> fetchedProjects =  ((Result.ProjectResponseSuccess) result).getProjectsApiResponse().getProjects().getProject();
                int startPosition = projectList.size();
                projectList.addAll(fetchedProjects);

                projectAdapter.notifyItemRangeInserted(startPosition, fetchedProjects.size());
            }
            else {
                Log.e("Home Fragment", ((Result.Error) result).getErrorMessage());
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}