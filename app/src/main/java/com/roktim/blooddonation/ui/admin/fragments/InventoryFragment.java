package com.roktim.blooddonation.ui.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roktim.blooddonation.R;
import com.roktim.blooddonation.data.database.entity.Inventory;
import com.roktim.blooddonation.ui.admin.AdminDashboardActivity;
import com.roktim.blooddonation.ui.admin.AdminViewModel;
import com.roktim.blooddonation.ui.admin.adapters.InventoryAdapter;
import com.roktim.blooddonation.ui.dialogs.EditInventoryDialog;

public class InventoryFragment extends Fragment implements InventoryAdapter. OnInventoryClickListener {

    private AdminViewModel viewModel;
    private RecyclerView rvInventory;
    private TextView tvTotalUnits;
    private InventoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        observeData();

        return view;
    }

    private void initViews(View view) {
        rvInventory = view.findViewById(R.id.rv_inventory);
        tvTotalUnits = view.findViewById(R.id.tv_total_units);
    }

    private void initViewModel() {
        if (getActivity() instanceof AdminDashboardActivity) {
            viewModel = ((AdminDashboardActivity) getActivity()).getViewModel();
        }
    }

    private void setupRecyclerView() {
        adapter = new InventoryAdapter(this);
        rvInventory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvInventory.setAdapter(adapter);
    }

    private void observeData() {
        if (viewModel == null) return;

        viewModel.getAllInventory().observe(getViewLifecycleOwner(), inventoryList -> {
            if (inventoryList != null) {
                adapter. setInventoryList(inventoryList);
            }
        });

        viewModel.getTotalBloodUnits().observe(getViewLifecycleOwner(), total -> {
            tvTotalUnits. setText(total != null ? String.valueOf(total) : "0");
        });
    }

    @Override
    public void onInventoryClick(Inventory inventory) {
        EditInventoryDialog dialog = new EditInventoryDialog(inventory,
                (bloodGroup, units, isAdd) -> {
                    if (isAdd) {
                        viewModel. addBloodUnits(bloodGroup, units);
                    } else {
                        viewModel.reduceBloodUnits(bloodGroup, units);
                    }
                });
        dialog.show(getChildFragmentManager(), "EditInventoryDialog");
    }

    @Override
    public void onAddUnits(Inventory inventory) {
        EditInventoryDialog dialog = new EditInventoryDialog(inventory, true,
                (bloodGroup, units, isAdd) -> {
                    viewModel.addBloodUnits(bloodGroup, units);
                });
        dialog.show(getChildFragmentManager(), "AddUnitsDialog");
    }

    @Override
    public void onReduceUnits(Inventory inventory) {
        EditInventoryDialog dialog = new EditInventoryDialog(inventory, false,
                (bloodGroup, units, isAdd) -> {
                    viewModel. reduceBloodUnits(bloodGroup, units);
                });
        dialog.show(getChildFragmentManager(), "ReduceUnitsDialog");
    }
}