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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

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
import it.sal.disco.unimib.charityhub.model.CountriesApiResponse;
import it.sal.disco.unimib.charityhub.model.Country;
import it.sal.disco.unimib.charityhub.model.Project;
import it.sal.disco.unimib.charityhub.model.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.Theme;
import it.sal.disco.unimib.charityhub.model.ThemesApiResponse;
import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class HomeFragment extends Fragment {


    HomeViewModel homeViewModel;
    List<Project> projectList;
    ProjectAdapter projectAdapter;
    RecyclerView recyclerView;
    Theme currentTheme;
    SharedPreferencesUtil sharedPreferencesUtil;
    String country;
    List<Theme> loadedThemes;
    ChipGroup chipGroup;

    int currentSet;


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
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(requireActivity().getApplication())).get(HomeViewModel.class);
        homeViewModel.setFirstLoading(true);
        projectList = new ArrayList<>();
        loadedThemes = new ArrayList<>();
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());

        country = sharedPreferencesUtil.readStringData(
                Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST);


        homeViewModel.searchForProjects("country:"+country, null).observe(this, result -> {
            Log.w("Home fragment", "OBSERVER");
            if(result.isSuccess()) {
                homeViewModel.setLoading(false);

                ProjectsApiResponse projectResponseSuccess = ((Result.ProjectResponseSuccess) result).getProjectsApiResponse();

                List<Project> fetchedProjects = projectResponseSuccess.getSearch().getResponse().getProjectData().getProjectList();
                Log.e("Home fragment", String.valueOf(fetchedProjects.size()));
                int startPosition = projectList.size();
                for (Project project : fetchedProjects) {
                    if (!checkDuplicates(project)) {
                        projectList.add(project);
                    }
                }
                updateUi(startPosition);
            } else {
                Log.e("Home Fragment", ((Result.Error) result).getErrorMessage());
                Snackbar.make(requireActivity().findViewById(android.R.id.content), ((Result.Error) result).getErrorMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        homeViewModel.getThemesLiveData().observe(this, result -> {
            if(result.isSuccess()) {
                ThemesApiResponse themesApiResponse = ((Result.ThemesResponseSuccess) result).getThemesApiResponse();
                List<Theme> themes = themesApiResponse.getThemeData().getThemes();
                Log.e("HOME FRAGMENT", "OBSERVER THEMES" + themes.size());
                loadedThemes.addAll(themes);
                showThemes();
            }
            else {
                Log.e("Home fragment", ((Result.Error) result).getErrorMessage());
            }
        });
    }

    private void showThemes() {
        Log.e("Home fragment", "themes: " + loadedThemes.size());
        for(Theme theme : loadedThemes) {
            Chip chip = new Chip(requireContext());
            chip.setId(ViewCompat.generateViewId());
            chip.setText(theme.getName());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    currentSet = 0;
                    int projectListSize = projectList.size();
                    projectList.clear();
                    // Notifica all'adapter che i dati sono cambiati
                    projectAdapter.notifyItemRangeRemoved(0, projectListSize);
                    homeViewModel.searchProjects("country:" + country+ ",theme:" +theme.getId(), currentSet);
                    currentTheme = theme;
                }
                else {
                    currentSet = 0;
                    int projectListSize = projectList.size();
                    projectList.clear();
                    projectAdapter.notifyItemRangeRemoved(0, projectListSize);
                    homeViewModel.searchProjects("country:" + country, currentSet);
                }
            });
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, com.google.android.material.R.style.Widget_Material3_Chip_Filter);
            chip.setChipDrawable(chipDrawable);
            chipGroup.addView(chip);
        }
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

        currentSet = 0;

        chipGroup = view.findViewById(R.id.chipGroup);


        if(loadedThemes.size() > 0) {
            showThemes();
        }
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
                        if (currentTheme != null) {
                            Log.w("Home fragment", String.valueOf(projectAdapter.getProject(lastVisibleItem).getId()) + " " + currentSet);
                            //homeViewModel.getProjects(currentTheme.getId(), projectAdapter.getProject(lastVisibleItem).getId());
                            currentSet += 10;
                            homeViewModel.searchProjects("country:" + country + ",theme:" + currentTheme.getId(), currentSet);
                        }
                        else {

                            Log.w("Home fragment", String.valueOf(projectAdapter.getProject(lastVisibleItem).getId()));
                            currentSet += 10;
                            homeViewModel.searchProjects("country:" + country, currentSet);
                        }
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