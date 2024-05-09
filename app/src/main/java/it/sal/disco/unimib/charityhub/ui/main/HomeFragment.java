package it.sal.disco.unimib.charityhub.ui.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.ProjectAdapter;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.model.projects.ProjectsApiResponse;
import it.sal.disco.unimib.charityhub.model.Result;
import it.sal.disco.unimib.charityhub.model.projects.Theme;
import it.sal.disco.unimib.charityhub.model.projects.ThemesApiResponse;
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
    CircularProgressIndicator circularProgressIndicator;
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

        homeViewModel.searchForProjects(Constants.COUNTRY_FILTER + country, null, false, country).observe(this, result -> {
                Log.w("Home fragment", "OBSERVER");
                if (result.isSuccess()) {
                    homeViewModel.setFirstLoading(false);
                    homeViewModel.setLoading(false);
                    homeViewModel.setNoMoreProjects(false);
                    ProjectsApiResponse projectResponseSuccess = ((Result.ProjectResponseSuccess) result).getProjectsApiResponse();
                    List<Project> fetchedProjects;
                    if(projectResponseSuccess != null)
                        fetchedProjects = projectResponseSuccess.getSearch().getResponse().getProjectData().getProjectList();
                    else {
                        fetchedProjects = ((Result.ProjectResponseSuccess) result).getProjectList();
                    }
                    Log.e("Home fragment", "Progetti trovati: " + String.valueOf(fetchedProjects.size()));
                    int startPosition = projectList.size();
                    for (Project project : fetchedProjects) {
                        if (!checkDuplicates(project)) {
                            projectList.add(project);
                        }
                    }
                    Log.e("Home fragment", "Numero progetti attuali " + projectList.size());
                    updateUi(startPosition);
                } else {
                    homeViewModel.setLoading(false);
                    homeViewModel.setNoMoreProjects(true);
                    if (circularProgressIndicator != null) {
                        circularProgressIndicator.setVisibility(View.GONE);
                    }
                    Log.e("Home Fragment", ((Result.Error) result).getErrorMessage());
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), ((Result.Error) result).getErrorMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });

        homeViewModel.getThemesLiveData().observe(this, result -> {
            if(result.isSuccess()) {
                ThemesApiResponse themesApiResponse = ((Result.ThemesResponseSuccess) result).getThemesApiResponse();
                List<Theme> themes = themesApiResponse.getThemeData().getThemes();
                for(Theme theme : themes) {
                    Log.w("Home Fragment", "new Theme(\"" + theme.getId() + "\", \"" +theme.getName() + "\"),");
                }
                loadedThemes.addAll(themes);
                showThemes();
            }
            else {
                List<Theme> themes = new ArrayList<>();
                Collections.addAll(themes, new Theme("animals", "Animal Welfare"),
                        new Theme("children", "Child Protection"),
                        new Theme("climate", "Climate Action"),
                        new Theme("democ", "Peace and Reconciliation"),
                        new Theme("disaster", "Disaster Response"),
                        new Theme("ecdev", "Economic Growth"),
                        new Theme("edu", "Education"),
                        new Theme("env", "Ecosystem Restoration"),
                        new Theme("gender", "Gender Equality"),
                        new Theme("health", "Physical Health"),
                        new Theme("human", "Ending Human Trafficking"),
                        new Theme("rights", "Justice and Human Rights"),
                        new Theme("sport", "Sport"),
                        new Theme("tech", "Digital Literacy"),
                        new Theme("hunger", "Food Security"),
                        new Theme("art", "Arts and Culture"),
                        new Theme("lgbtq", "LGBTQIA+ Equality"),
                        new Theme("covid-19", "COVID-19"),
                        new Theme("water", "Clean Water"),
                        new Theme("disability", "Disability Rights"),
                        new Theme("endabuse", "Ending Abuse"),
                        new Theme("mentalhealth", "Mental Health"),
                        new Theme("justice", "Racial Justice"),
                        new Theme("refugee", "Refugee Rights"),
                        new Theme("reproductive", "Reproductive Health"),
                        new Theme("housing", "Safe Housing"),
                        new Theme("agriculture", "Sustainable Agriculture"),
                        new Theme("wildlife", "Wildlife Conservation"));
                loadedThemes.addAll(themes);
                showThemes();
                Log.e("Home fragment", ((Result.Error) result).getErrorMessage());
            }
        });
    }

    private void showThemes() {
        for(Theme theme : loadedThemes) {
            Chip chip = new Chip(requireContext());
            chip.setId(ViewCompat.generateViewId());
            chip.setText(theme.getName());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, com.google.android.material.R.style.Widget_Material3_Chip_Filter);
            chip.setChipDrawable(chipDrawable);
            if(homeViewModel.getCurrentTheme() != null && homeViewModel.getCurrentTheme().equals(theme)) {
                Log.w("HomeFragment", "TEMA ATTUALE : " + homeViewModel.getCurrentTheme().getName());
                currentTheme = theme;
                chip.setChecked(true);
            }
            chipGroup.addView(chip);
        }

    }

    private void loadProjectsByTheme(Theme theme) {
        currentSet = 0;
        int projectListSize = projectList.size();
        projectList.clear();
        projectAdapter.notifyItemRangeRemoved(0, projectListSize);
        homeViewModel.setLoading(true);
        if(circularProgressIndicator != null)
            circularProgressIndicator.setVisibility(View.VISIBLE);
        //homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country + "," + Constants.THEME_FILTER + theme.getId(), currentSet, false);
        homeViewModel.getProjectsByTheme(theme.getName(), country);
        currentTheme = theme;
    }

    private void loadProjectsWithoutTheme() {
        currentSet = 0;
        int projectListSize = projectList.size();
        projectList.clear();
        homeViewModel.setLoading(true);
        projectAdapter.notifyItemRangeRemoved(0, projectListSize);
        currentTheme = null;
        if(circularProgressIndicator != null)
            circularProgressIndicator.setVisibility(View.VISIBLE);
        //homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country, currentSet, false);
        homeViewModel.getProjectsByCountry(country);
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

        chipGroup = view.findViewById(R.id.chipGroup);
        circularProgressIndicator = view.findViewById(R.id.progressIndicator);
        String newCountry = sharedPreferencesUtil.readStringData(Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST);
        recyclerView = view.findViewById(R.id.projectsRV);
        projectAdapter = new ProjectAdapter(projectList, requireContext());
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(false);
        currentSet = 0;
        homeViewModel.setNoMoreProjects(false);
        if(newCountry != null && !newCountry.equals(country)) {
            circularProgressIndicator.setVisibility(View.VISIBLE);
            projectList.clear();
            country = newCountry;
            Log.w("HOME FRAGMENT", "NUOVA NAZIONE " + newCountry);
            //homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country, null, false);
            homeViewModel.getProjectsByCountry(country);
        }

        if(projectList.isEmpty() && homeViewModel.isFirstLoading()) {
            circularProgressIndicator.setVisibility(View.VISIBLE);
            homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country, null, true);
        }

        // Controlla se è stato selezionato un tema e carica i progetti corrispondenti

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if(checkedId == View.NO_ID && !homeViewModel.isLoading()) {
                loadProjectsWithoutTheme();
            }
            else if (chip != null) {
                Theme selectedTheme = getThemeByName(chip.getText().toString());
                if (selectedTheme != null && !homeViewModel.isLoading()) {
                    loadProjectsByTheme(selectedTheme);
                } else if (!homeViewModel.isLoading()){
                    Log.w("Home Fragment", "Carico senza temi");
                    loadProjectsWithoutTheme();
                }
            }
        });


        if(loadedThemes.size() > 0) {
            showThemes();
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = 0;
                int lastVisibleItem = 0;
                if(layoutManager != null) {
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                }

                Log.d("HOME FRAGMENT", lastVisibleItem + " , " + totalItemCount + homeViewModel.isLoading() + homeViewModel.isNoMoreProjects() + isConnected());
                if(totalItemCount > 1 && dy > 0) {
                    if (isConnected() && !homeViewModel.isLoading() && totalItemCount == lastVisibleItem + 1 && !homeViewModel.isNoMoreProjects()) {
                        homeViewModel.setLoading(true);
                        circularProgressIndicator.setVisibility(View.VISIBLE);
                        if (currentTheme != null) {
                            Log.w("Home Fragment", "CARICO CON TEMA: " + currentTheme.getName());
                            currentSet = (int) (10 * Math.ceil((double)totalItemCount/10) + 1);
                            homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country + "," + Constants.THEME_FILTER + currentTheme.getId(), currentSet, true);
                        }
                        else {
                            Log.w("Home Fragment", "CARICO SENZA TEMA " + totalItemCount);
                            currentSet = (int) (10 * Math.ceil((double)totalItemCount/10) + 1);
                            Log.w("HOME FRAMGNET", "CURRENT SET " + currentSet);
                            homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country, currentSet, true);
                        }
                    }
                } else if(totalItemCount == 0 || (totalItemCount == 1)) {
                    if(isConnected() && !homeViewModel.isLoading()) {
                        if (currentTheme != null) {
                            circularProgressIndicator.setVisibility(View.VISIBLE);
                            Log.w("Home Fragment", "CARICO CON TEMA: " + currentTheme.getName());
                            currentSet = (int) (10 * Math.ceil((double) totalItemCount / 10) + 1);
                            homeViewModel.searchProjects(Constants.COUNTRY_FILTER + country + "," + Constants.THEME_FILTER + currentTheme.getId(), currentSet, true);
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
        try {
            if(circularProgressIndicator != null)
                circularProgressIndicator.setVisibility(View.GONE);
            projectAdapter.notifyItemRangeInserted(startPosition, projectList.size());
        } catch (Exception e) {
            Log.e("HOME FRAGMENT", "Errore in updateUI: " + e.getMessage());
            if(circularProgressIndicator != null)
                projectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(currentTheme != null) {
            homeViewModel.setCurrentTheme(currentTheme);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeViewModel.setLoading(false);
    }

    public Theme getThemeByName(String themeName) {
        for(Theme theme : loadedThemes) {
            if(themeName.equals(theme.getName())) {
                return theme;
            }
        }
        return null;
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}