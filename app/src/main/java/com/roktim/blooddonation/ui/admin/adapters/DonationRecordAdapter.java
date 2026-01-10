package com.roktim.blooddonation.ui.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.RoktimApplication;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DonationRecordAdapter extends RecyclerView. Adapter<DonationRecordAdapter.DonationViewHolder> {

    private List<Donation> donations = new ArrayList<>();

    public void setDonations(List<Donation> donations) {
        this.donations = donations != null ? donations :  new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donation donation = donations.get(position);
        holder.bind(donation);
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    static class DonationViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDonorName, tvBloodGroup, tvUnits, tvDate;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonorName = itemView.findViewById(R.id.tv_donor_name);
            tvBloodGroup = itemView. findViewById(R.id.tv_blood_group);
            tvUnits = itemView.findViewById(R. id.tv_units);
            tvDate = itemView.findViewById(R.id. tv_date);
        }

        public void bind(Donation donation) {
            tvBloodGroup.setText(donation.getBloodGroup());
            tvUnits.setText(donation.getUnits() + " Units");
            tvDate.setText(DateUtils.formatForDisplay(donation.getDate()));

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = RoktimApplication. getInstance()
                        .getDatabase()
                        . userDao()
                        .getUserByIdSync(donation.getUserId());

                if (user != null) {
                    itemView.post(() -> tvDonorName. setText(user.getName()));
                } else {
                    itemView.post(() -> tvDonorName.setText("Unknown Donor"));
                }
            });
        }
    }
}