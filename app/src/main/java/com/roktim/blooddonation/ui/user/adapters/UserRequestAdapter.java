package com.roktim.blooddonation.ui.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Request;
import com.roktim.blooddonation.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.RequestViewHolder> {

    private List<Request> requests = new ArrayList<>();

    public void setRequests(List<Request> requests) {
        this.requests = requests != null ? requests : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView. ViewHolder {
        private final TextView tvPatientName, tvBloodGroup, tvLocation, tvDate, tvStatus;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvBloodGroup = itemView. findViewById(R.id.tv_blood_group);
            tvLocation = itemView. findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R. id.tv_status);
        }

        public void bind(Request request) {
            tvPatientName.setText(request. getPatientName());
            tvBloodGroup.setText(request.getBloodGroup());
            tvLocation.setText(request. getLocation());
            tvDate.setText(DateUtils.formatForDisplay(request.getDate()));

            if (request. isPending()) {
                tvStatus.setText("Pending");
                tvStatus.setBackgroundResource(R.drawable. bg_status_pending);
            } else {
                tvStatus. setText("Fulfilled");
                tvStatus.setBackgroundResource(R.drawable. bg_status_fulfilled);
            }
        }
    }
}