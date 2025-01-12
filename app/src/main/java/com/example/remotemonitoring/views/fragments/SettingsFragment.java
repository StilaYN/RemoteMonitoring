package com.example.remotemonitoring.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.remotemonitoring.MainActivity;
import com.example.remotemonitoring.R;
import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.databinding.FragmentSettingsBinding;
import com.example.remotemonitoring.webclients.model.DeviceConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements BackPressedListener {

    private static final String UUID = "uuid";

    private FragmentSettingsBinding binding;


    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.save.setOnClickListener(save());
        binding.back.setOnClickListener(back());
        MainActivity.changeFocusListener(this);
        return binding.getRoot();
    }
    public void updateUI(long interval, int minT, int maxT) {
        binding.interval.setText(String.valueOf(interval));
        binding.minT.setText(String.valueOf(minT));
        binding.maxT.setText(String.valueOf(maxT));
    }

    private View.OnClickListener save(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Long.parseLong(binding.interval.getText().toString().trim())<=0){
                    Toast toast = Toast.makeText(getContext(), "Время не может быть отрицательным", Toast.LENGTH_SHORT);
                    toast.show();
                }
                RemoteMonitoringApplication.INSTANCE
                        .getRemoteMonitoringWebClient()
                        .saveNewConfiguration(
                                requireArguments().getString(UUID),
                                new DeviceConfiguration(
                                    Long.parseLong(binding.interval.getText().toString().trim()),
                                    Integer.parseInt(binding.minT.getText().toString().trim()),
                                    Integer.parseInt(binding.maxT.getText().toString().trim())
                                )
                        ).enqueue(new Callback<DeviceConfiguration>() {
                            @Override
                            public void onResponse(Call<DeviceConfiguration> call, Response<DeviceConfiguration> response) {

                            }

                            @Override
                            public void onFailure(Call<DeviceConfiguration> call, Throwable throwable) {
                                if (isAdded()) {
                                    Toast toast = Toast.makeText(getContext(), "Ошибка сервера", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,DeviceStatisticsFragment.newInstance(requireArguments().getString(UUID)))
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

    private View.OnClickListener back(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("UUID", requireArguments().getString(UUID));
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,DeviceStatisticsFragment.newInstance(requireArguments().getString(UUID)))
                        .commit();
            }
        };
    }

    @Override
    public void OnBackPressed() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,DeviceStatisticsFragment.newInstance(requireArguments().getString(UUID)))
                .commit();
    }

    public static SettingsFragment newInstance(String uuid) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        RemoteMonitoringApplication.INSTANCE
                .getRemoteMonitoringWebClient()
                .getConfiguration(uuid).enqueue(new Callback<DeviceConfiguration>() {
                    @Override
                    public void onResponse(@NonNull Call<DeviceConfiguration> call, @NonNull Response<DeviceConfiguration> response) {
                        if (response.isSuccessful()){
                            assert response.body() != null;
                            long interval = response.body().updateInterval();
                            int minT = response.body().minCriticalTemperature();
                            int maxT = response.body().maxCriticalTemperature();
                            if (fragment.isAdded()) {
                                fragment.updateUI(interval, minT, maxT);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeviceConfiguration> call, Throwable throwable) {
                        if (fragment.isAdded()) {
                            Toast toast = Toast.makeText(fragment.getContext(), "Ошибка сервера", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
        args.putString(UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

}