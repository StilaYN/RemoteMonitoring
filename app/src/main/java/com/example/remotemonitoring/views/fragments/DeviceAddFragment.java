package com.example.remotemonitoring.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.remotemonitoring.R;
import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.core.Repository.DeviceRepository;
import com.example.remotemonitoring.core.entity.Device;
import com.example.remotemonitoring.databinding.FragmentAddDeviceBinding;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceAddFragment extends Fragment {

    private final DeviceRepository devices = RemoteMonitoringApplication.INSTANCE.getDeviceRepository();

    private FragmentAddDeviceBinding binding;

    public DeviceAddFragment() {
        super(R.layout.fragment_add_device);
    }

    public static DeviceAddFragment newInstance() {
        return new DeviceAddFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDeviceBinding.inflate(inflater, container, false);
        binding.save.setOnClickListener(save());
        binding.back.setOnClickListener(back());
        return binding.getRoot();
    }

    private View.OnClickListener back(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteMonitoringApplication.INSTANCE.getRouter().replaceScreen(new FragmentScreen() {
                    @Override
                    public boolean getClearContainer() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public Fragment createFragment(@NonNull FragmentFactory fragmentFactory) {
                        return MainFragment.newInstance();
                    }

                    @NonNull
                    @Override
                    public String getScreenKey() {
                        return "MainFragment";
                    }
                });
            }
        };
    }

    private View.OnClickListener save(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devices.addNewDevice(new Device(
                        binding.name.getText().toString().trim(),
                        binding.uuid.getText().toString().trim())
                );
                RemoteMonitoringApplication.INSTANCE.getRouter().replaceScreen(new FragmentScreen() {
                    @Override
                    public boolean getClearContainer() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public Fragment createFragment(@NonNull FragmentFactory fragmentFactory) {
                        return MainFragment.newInstance();
                    }

                    @NonNull
                    @Override
                    public String getScreenKey() {
                        return "MainFragment";
                    }
                });
            }
        };
    }
}