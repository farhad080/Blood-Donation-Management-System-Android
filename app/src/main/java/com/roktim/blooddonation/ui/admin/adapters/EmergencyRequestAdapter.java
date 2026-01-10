package com.roktim.blooddonation.ui.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class EmergencyRequestAdapter extends RecyclerView. Adapter<EmergencyRequestAdapter.RequestViewHolder> {

    private List<Request> requests = new ArrayList<>();
    private final OnRequestClickListener listener;

    public interface OnRequestClickListener {
        void onMarkFulfilled(Request request);
        void onFindDonors(Request request);
    }

    public EmergencyRequestAdapter(OnRequestClickListener listener) {
        this.listener = listener;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests != null ?  requests : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPatientName, tvBloodGroup, tvLocation, tvMessage, tvDate, tvStatus;
        private final MaterialButton btnFindDonors, btnMarkFulfilled;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvBloodGroup = itemView.findViewById(R.id. tv_blood_group);
            tvLocation = itemView.findViewById(R.id. tv_location);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDate = itemView. findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnFindDonors = itemView. findViewById(R.id.btn_find_donors);
            btnMarkFulfilled = itemView.findViewById(R.id.btn_mark_fulfilled);
        }

        public void bind(Request request) {
            tvPatientName. setText(request.getPatientName());
            tvBloodGroup.setText(request.getBloodGroup());
            tvLocation.setText(request.getLocation());
            tvDate.setText(DateUtils.formatForDisplay(request.getDate()));

            if (request.getMessage() != null && !request.getMessage().isEmpty()) {
                tvMessage.setText(request.getMessage());
                tvMessage.setVisibility(View.VISIBLE);
            } else {
                tvMessage.setVisibility(View.GONE);
            }

            if (request.isPending()) {
                tvStatus.setText("Pending");
                tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                btnMarkFulfilled.setVisibility(View.VISIBLE);
                btnFindDonors.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setText("Fulfilled");
                tvStatus.setBackgroundResource(R.drawable.bg_status_fulfilled);
                btnMarkFulfilled.setVisibility(View.GONE);
                btnFindDonors.setVisibility(View.GONE);
            }

            btnFindDonors.setOnClickListener(v -> {
                if (listener != null) listener.onFindDonors(request);
            });

            btnMarkFulfilled.setOnClickListener(v -> {
                if (listener != null) listener.onMarkFulfilled(request);
            });
        }
    }
}