package it.sal.disco.unimib.charityhub.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.adapter.DonationAdapter;
import it.sal.disco.unimib.charityhub.model.projects.DonationOptions;
import it.sal.disco.unimib.charityhub.model.projects.Project;

public class DonationFragment extends Fragment {


    Project project;
    RecyclerView recyclerView;
    DonationAdapter donationAdapter;
    List<DonationOptions.DonationOption> donationOptionList;
    MaterialDivider divider;


    public DonationFragment() {
        // Required empty public constructor
    }

    public static DonationFragment newInstance(String param1, String param2) {
        DonationFragment fragment = new DonationFragment();
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
        return inflater.inflate(R.layout.fragment_donation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        project = DonationFragmentArgs.fromBundle(requireArguments()).getProject();

        donationOptionList = new ArrayList<>();
        donationOptionList.addAll(project.getDonationOptions().getDonationOptionList());

        recyclerView = view.findViewById(R.id.donationRecyclerView);
        donationAdapter = new DonationAdapter(donationOptionList);
        recyclerView.setAdapter(donationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);
        TextView projectTitleTextView = view.findViewById(R.id.projectTitleDonation);
        LinearProgressIndicator linearProgressIndicator = view.findViewById(R.id.moneyLinearProgressIndicator);
        TextView numberOfDonationTextView = view.findViewById(R.id.numberOfDonations);
        TextView donationRemainingTextView = view.findViewById(R.id.moneyRemaining);
        TextView currentMoneyRaisedTextView = view.findViewById(R.id.currentMoneyAmount);
        TextView totalMoneyNeededTextView = view.findViewById(R.id.totalMoneyAmount);
        String raisedMoney = "€" + project.getFunding();
        String totalMoneyNeeded = "raised of " + project.getGoal();
        currentMoneyRaisedTextView.setText(raisedMoney);
        totalMoneyNeededTextView.setText(totalMoneyNeeded);
        String donationsNumber = project.getNumberOfDonations() + " donations";
        String moneyRemaining = project.getRemainingFunding() + " to go";
        numberOfDonationTextView.setText(donationsNumber);
        donationRemainingTextView.setText(moneyRemaining);
        linearProgressIndicator.setProgress((int) (project.getFunding()*100/project.getGoal()));
        projectTitleTextView.setText(project.getTitle());


    }
}