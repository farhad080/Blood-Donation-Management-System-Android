package com.roktim.blooddonation.ui.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Donation;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDonationAdapter extends RecyclerView.Adapter<UserDonationAdapter.DonationViewHolder> {

    private List<Donation> donations = new ArrayList<>();

    public void setDonations(List<Donation> donations) {
        this.donations = donations != null ? donations : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater. from(parent.getContext()).inflate(R.layout.item_user_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        holder.bind(donations.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    static class DonationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSerialNo, tvBloodGroup, tvUnits, tvDate;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNo = itemView.findViewById(R.id.tv_serial_no);
            tvBloodGroup = itemView. findViewById(R.id.tv_blood_group);
            tvUnits = itemView.findViewById(R. id.tv_units);
            tvDate = itemView.findViewById(R.id. tv_date);
        }

        public void bind(Donation donation, int serialNo) {
            tvSerialNo. setText("#" + serialNo);
            tvBloodGroup.setText(donation.getBloodGroup());
            tvUnits.setText(donation.getUnits() + " Units");
            tvDate.setText(DateUtils.formatForDisplay(donation.getDate()));
        }
    }
}