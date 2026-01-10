package com.roktim.blooddonation.ui.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.User;
import com.roktim.blooddonation.utils.DateUtils;
import com.roktim.blooddonation.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorSearchAdapter extends RecyclerView.Adapter<DonorSearchAdapter. DonorViewHolder> {

    private List<User> donors = new ArrayList<>();
    private final OnDonorClickListener listener;

    public interface OnDonorClickListener {
        void onCallClick(User user);
    }

    public DonorSearchAdapter(OnDonorClickListener listener) {
        this.listener = listener;
    }

    public void setDonors(List<User> donors) {
        this.donors = donors != null ? donors : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donor_search, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        holder.bind(donors. get(position));
    }

    @Override
    public int getItemCount() {
        return donors. size();
    }

    class DonorViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView ivProfile;
        private final TextView tvName, tvBloodGroup, tvPhone, tvAddress, tvLastDonation;
        private final ImageButton btnCall;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView. findViewById(R.id.tv_name);
            tvBloodGroup = itemView. findViewById(R.id.tv_blood_group);
            tvPhone = itemView. findViewById(R.id.tv_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvLastDonation = itemView.findViewById(R.id. tv_last_donation);
            btnCall = itemView.findViewById(R.id. btn_call);
        }

        public void bind(User donor) {
            tvName.setText(donor.getName());
            tvBloodGroup. setText(donor.getBloodGroup());
            tvPhone.setText(donor.getPhone());
            tvAddress.setText(donor. getAddress());

            if (donor. getLastDonation() != null && ! donor.getLastDonation().isEmpty()) {
                tvLastDonation.setText("Last Donation: " + DateUtils.formatForDisplay(donor.getLastDonation()));
            } else {
                tvLastDonation.setText("No donation record");
            }
            tvLastDonation. setVisibility(View.VISIBLE);

            if (donor.getProfileImage() != null && !donor.getProfileImage().isEmpty()) {
                try {
                    ivProfile.setImageBitmap(ImageUtils.base64ToBitmap(donor.getProfileImage()));
                } catch (Exception e) {
                    ivProfile.setImageResource(R.drawable. ic_profile);
                }
            } else {
                ivProfile.setImageResource(R.drawable.ic_profile);
            }

            btnCall.setOnClickListener(v -> {
                if (listener != null) listener.onCallClick(donor);
            });
        }
    }
}