package com.example.remotemonitoring.views.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotemonitoring.R;
import com.example.remotemonitoring.core.entity.Device;
import com.example.remotemonitoring.databinding.CardviewsDeviceBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> implements View.OnClickListener{
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView uuid;

        DeviceViewHolder(CardviewsDeviceBinding binding) {
            super(binding.getRoot());
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.uuid = (TextView) itemView.findViewById(R.id.uuid);
        }
    }

    List<Device> devices;

    FragmentManager fragmentManager;

    DeviceListAdapter(List<Device> devices, FragmentManager fragmentManager){
        this.devices = Objects.requireNonNullElseGet(devices, ArrayList::new);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemCount(){
        return devices.size();
    }

    @Override
    public void onClick(View v) {
        Device device = (Device)v.getTag();

        fragmentManager.beginTransaction().replace(R.id.fragment_container, DeviceStatisticsFragment.newInstance(device.getUuid())).commit();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater inflater =  LayoutInflater.from(viewGroup.getContext());
        CardviewsDeviceBinding binding = CardviewsDeviceBinding.inflate(
                inflater,
                viewGroup,
                false
        );
        binding.getRoot().setOnClickListener(this);
        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i){
        deviceViewHolder.name.setText(devices.get(i).getName());
        deviceViewHolder.uuid.setText(devices.get(i).getUuid());
        deviceViewHolder.itemView.setTag(devices.get(i));

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }


}
