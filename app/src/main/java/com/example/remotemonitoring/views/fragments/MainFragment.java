package com.example.remotemonitoring.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.remotemonitoring.R;
import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.core.Repository.DeviceRepository;
import com.example.remotemonitoring.databinding.FragmentMainBinding;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainFragment extends Fragment {

    private final DeviceRepository devices = RemoteMonitoringApplication.INSTANCE.getDeviceRepository();

    private FragmentMainBinding binding;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.devices.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.devices.setAdapter(new DeviceListAdapter(devices.getAllDevice()));
        binding.addButton.setOnClickListener(addNewDevice());
        return binding.getRoot();
    }

    public static MainFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener addNewDevice(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteMonitoringApplication.INSTANCE.getRouter().navigateTo(new FragmentScreen() {
                    @Override
                    public boolean getClearContainer() {
                        return false;
                    }

                    @NonNull
                    @Override
                    public Fragment createFragment(@NonNull FragmentFactory fragmentFactory) {
                        return DeviceAddFragment.newInstance();
                    }

                    @NonNull
                    @Override
                    public String getScreenKey() {
                        return "AddDeviceFragment";
                    }
                });
            }
        };
    }


}