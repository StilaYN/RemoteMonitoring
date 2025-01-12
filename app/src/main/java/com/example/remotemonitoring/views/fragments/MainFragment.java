package com.example.remotemonitoring.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.remotemonitoring.MainActivity;
import com.example.remotemonitoring.R;
import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.core.Repository.DeviceRepository;
import com.example.remotemonitoring.databinding.FragmentMainBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainFragment extends Fragment implements BackPressedListener {

    private final DeviceRepository devices = RemoteMonitoringApplication.INSTANCE.getDeviceRepository();

    private FragmentMainBinding binding;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.changeFocusListener(this);
        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.devices.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.devices.setAdapter(new DeviceListAdapter(devices.getAllDevice(), getFragmentManager()));
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
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, DeviceAddFragment.newInstance())
                        .commit();
            }
        };
    }

    @Override
    public void OnBackPressed() {
        MainActivity.changeFocusListener(null);
        Toast toast = Toast.makeText(this.getContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT);
        toast.show();
    }
}