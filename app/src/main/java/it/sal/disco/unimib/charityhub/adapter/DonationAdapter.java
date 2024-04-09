package it.sal.disco.unimib.charityhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.projects.DonationOptions;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private final List<DonationOptions.DonationOption> donationOptionList;

    public DonationAdapter(List<DonationOptions.DonationOption> donationOptionList) {
        this.donationOptionList = donationOptionList;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        String donationAmount = donationOptionList.get(position).getAmount() + "€";
        String donationDescription = donationOptionList.get(position).getDescription();

        holder.donationAmountTextView.setText(donationAmount);
        holder.donationDescriptionTextView.setText(donationDescription);
    }

    @Override
    public int getItemCount() {
        return donationOptionList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        private final TextView donationAmountTextView;

        private final TextView donationDescriptionTextView;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            donationAmountTextView = itemView.findViewById(R.id.donationAmount);
            donationDescriptionTextView = itemView.findViewById(R.id.donationDescription);
        }
    }
}
