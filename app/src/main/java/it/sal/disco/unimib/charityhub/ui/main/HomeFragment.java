package it.sal.disco.unimib.charityhub.ui.main;

import android.net.ipsec.ike.ChildSaProposal;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.ProjectAdapter;
import it.sal.disco.unimib.charityhub.data.repositories.project.ProjectRepository;
import it.sal.disco.unimib.charityhub.model.Project;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.Theme;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;

public class HomeFragment extends Fragment {


    HomeViewModel homeViewModel;
    List<Project> projectList;
    ProjectAdapter projectAdapter;
    RecyclerView recyclerView;
    Theme currentTheme;

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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.setFirstLoading(true);
        projectList = new ArrayList<>();

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
        recyclerView = view.findViewById(R.id.projectsRV);
        projectAdapter = new ProjectAdapter(projectList, requireContext());
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(false);
        //recyclerView.setItemAnimator(null);
        homeViewModel.setLoading(true);

        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);

        homeViewModel.getThemesLiveData().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccess()) {
                ThemesApiResponse themesApiResponse = ((Result.ThemesResponseSuccess) result).getThemesApiResponse();
                List<Theme> themes = themesApiResponse.getThemeData().getThemes();
                for(Theme theme : themes) {
                    Log.w("Home fragment", theme.getName());
                    Chip chip = new Chip(requireContext());
                    chip.setId(ViewCompat.generateViewId());
                    chip.setText(theme.getName());
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Pulisci la lista dei progetti attualmente visualizzati
                            int projectListSize = projectList.size();
                            projectList.clear();
                            // Notifica all'adapter che i dati sono cambiati
                            projectAdapter.notifyItemRangeRemoved(0, projectListSize);
                            homeViewModel.getProjects(theme.getId(), null);
                            currentTheme = theme;
                        }
                    });
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, com.google.android.material.R.style.Widget_Material3_Chip_Filter);
                    chip.setChipDrawable(chipDrawable);
                    chipGroup.addView(chip);
                }
            }
            else {
                Log.e("Home fragment", ((Result.Error) result).getErrorMessage());
            }
        });
        homeViewModel.getProjectsLiveData("env", null).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                homeViewModel.setLoading(false);
                List<Project> fetchedProjects = ((Result.ProjectResponseSuccess) result).getProjectsApiResponse().getProjects().getProject();
                int startPosition = projectList.size();
                for(Project project : fetchedProjects) {
                    if(!checkDuplicates(project)) {
                        projectList.add(project);
                    }
                }
                updateUi(startPosition);
            } else {
                Log.e("Home Fragment", ((Result.Error) result).getErrorMessage());
                Snackbar.make(requireActivity().findViewById(android.R.id.content), ((Result.Error) result).getErrorMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if(totalItemCount > 0) {
                    // Se l'utente è vicino alla fine della lista e non stiamo già caricando, carica più dati
                    if (!homeViewModel.isLoading() && totalItemCount == lastVisibleItem + 1) {
                        //Log.e("Home Fragment", String.valueOf(projectAdapter.getProject(lastVisibleItem).getId()));
                        // Carica più dati qui
                        homeViewModel.setLoading(true);
                        if (currentTheme != null)
                            homeViewModel.getProjects(currentTheme.getId(), projectAdapter.getProject(lastVisibleItem).getId());
                        else
                            homeViewModel.getProjects("env", projectAdapter.getProject(lastVisibleItem).getId());
                    }
                }
            }
        });

    }

    public boolean checkDuplicates(Project projects) {
        for(Project p : projectList) {
            if(p.getId() == projects.getId()) {
                return true;
            }
        }
        return false;
    }

    public void updateUi(int startPosition) {
        recyclerView.post(() -> projectAdapter.notifyItemRangeInserted(startPosition, projectList.size()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
;        homeViewModel.setLoading(false);
    }

}