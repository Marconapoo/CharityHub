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
    List<Project> projectList;
    ProjectAdapter projectAdapter;
    RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.projectsRV);
        homeViewModel.setFirstLoading(true);
        projectList = new ArrayList<>();
        projectAdapter = new ProjectAdapter(projectList, requireContext());
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        loadData(null);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                // Se l'utente è vicino alla fine della lista e non stiamo già caricando, carica più dati
                if (!homeViewModel.isLoading() && totalItemCount == lastVisibleItem + 1) {
                    Log.e("Home Fragment", String.valueOf(projectAdapter.getProject(lastVisibleItem).getId()));
                    // Carica più dati qui
                    homeViewModel.setLoading(true);
                    homeViewModel.getProjects("env", projectAdapter.getProject(lastVisibleItem).getId());
                }
            }
        });

    }

    public void loadData(Integer lastProjectId) {
        if(homeViewModel.isLoading())
            return;
        homeViewModel.setLoading(true);
        homeViewModel.getProjectsLiveData("env", lastProjectId).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                homeViewModel.setLoading(false);
                List<Project> fetchedProjects = ((Result.ProjectResponseSuccess) result).getProjectsApiResponse().getProjects().getProject();
                int startPosition = projectList.size();
                if(homeViewModel.isFirstLoading()) {
                    projectList.addAll(fetchedProjects);
                    homeViewModel.setFirstLoading(false);
                }
                else {
                    fetchedProjects.remove(fetchedProjects.get(0));
                    projectList.addAll(fetchedProjects);
                }
                Log.e("Home fragment", String.valueOf(projectList.size()));
                recyclerView.post(() -> projectAdapter.notifyItemRangeInserted(startPosition, projectList.size()));

            } else {
                Log.e("Home Fragment", ((Result.Error) result).getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}