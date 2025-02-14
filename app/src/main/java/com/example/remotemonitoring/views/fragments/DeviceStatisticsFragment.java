package com.example.remotemonitoring.views.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.remotemonitoring.MainActivity;
import com.example.remotemonitoring.R;
import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.core.Repository.DeviceRepository;
import com.example.remotemonitoring.databinding.FragmentDeviceStatisticsBinding;
import com.example.remotemonitoring.webclients.model.Temperature;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceStatisticsFragment extends Fragment implements BackPressedListener{
    private FragmentDeviceStatisticsBinding binding;

    private static final DeviceRepository devicesRepository = RemoteMonitoringApplication.INSTANCE.getDeviceRepository();

    public DeviceStatisticsFragment() {
        super(R.layout.fragment_device_statistics);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.changeFocusListener(this);
        binding = FragmentDeviceStatisticsBinding.inflate(inflater, container, false);

        binding.name.setText(getNameValue());
        binding.uuid.setText(getUuidValue());

        binding.settings.setOnClickListener(openSettings());
        binding.back.setOnClickListener(back());
        binding.delete.setOnClickListener(delete());

        return binding.getRoot();
    }

    private String getNameValue() {
        return requireArguments().getString("name");
    }

    private String getUuidValue() {
        return requireArguments().getString("uuid");
    }

    @SuppressLint("DefaultLocale")
    public void updateUI(int temperature, OffsetDateTime dateTime, boolean isCritical) {
        binding.temperature.setText(String.valueOf(temperature));
        binding.date.setText(
                String.format("%d:%d:%d %d-%d-%d",
                        dateTime.getHour(),
                        dateTime.getMinute(),
                        dateTime.getSecond(),
                        dateTime.getDayOfMonth(),
                        dateTime.getMonth().getValue(),
                        dateTime.getYear()
                )
        );
        binding.temperature.setTextColor((isCritical) ? Color.RED : Color.WHITE);
    }

    public void updateChart(List<Temperature> temperatures, int offset){
        List<Entry> entries = new ArrayList<>();
        for (Temperature temperature:temperatures) {
            entries.add(new Entry(
                    temperature.timestamp().plusHours(offset).toEpochSecond(),
                    temperature.temperature())
            );
        }
        LineDataSet dataSet = new LineDataSet(entries, "Температура");
        dataSet.setColor(0xff6750f4);
        LineData lineData = new LineData(dataSet);
        binding.lineChart.setData(lineData);

        // Настройка графика
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getXAxis().setDrawLabels(false);
        //binding.lineChart.setVisibleXRangeMaximum(7);
        //binding.lineChart.moveViewToX(entries.size() - 1);
        binding.lineChart.animateXY(2000, 2000);
    }

    private View.OnClickListener openSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment.newInstance(requireArguments().getString("uuid")))
                        .commit();
            }
        };
    }

    private View.OnClickListener back() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance()).commit();
            }
        };
    }

    @Override
    public void OnBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance()).commit();
    }

    private View.OnClickListener delete() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteMonitoringApplication.INSTANCE.getDeviceRepository().deleteDevice(requireArguments().getString("uuid"));
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance()).commit();
            }
        };
    }

    public static DeviceStatisticsFragment newInstance() {
        DeviceStatisticsFragment fragment = new DeviceStatisticsFragment();
        return fragment;
    }

    public static DeviceStatisticsFragment newInstance(String uuid) {
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        args.putString("name", devicesRepository.getDeviceByUuid(uuid).getName());
        DeviceStatisticsFragment fragment = new DeviceStatisticsFragment();
        fragment.setArguments(args);
        RemoteMonitoringApplication.INSTANCE
                .getRemoteMonitoringWebClient()
                .getLastTemperature(uuid)
                .enqueue(new Callback<Temperature>() {
                    @Override
                    public void onResponse(@NonNull Call<Temperature> call, @NonNull Response<Temperature> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Log.d("GetTemperature", response.body().toString());
                            int temperature = response.body().temperature();
                            OffsetDateTime dateTime = response.body().timestamp();
                            boolean isCritical = response.body().isCritical();
                            ZoneOffset offset = OffsetDateTime.now().getOffset();
                            int offsetHours = offset.getTotalSeconds() / 3600;

                            if (fragment.isAdded()) {
                                fragment.updateUI(
                                        temperature,
                                        dateTime.plusHours(offsetHours),
                                        isCritical
                                );

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Temperature> call, Throwable throwable) {
                        if (fragment.isAdded()) {
                            Toast toast = Toast.makeText(fragment.getContext(), "Ошибка сервера", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
        RemoteMonitoringApplication.INSTANCE
                .getRemoteMonitoringWebClient()
                .getAllTemperature(uuid)
                .enqueue(new Callback<List<Temperature>>() {
            @Override
            public void onResponse(@NonNull Call<List<Temperature>> call, @NonNull Response<List<Temperature>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("GetTemperature", response.body().toString());
                    List<Temperature> temperatures = response.body();
                    ZoneOffset offset = OffsetDateTime.now().getOffset();
                    int offsetHours = offset.getTotalSeconds() / 3600;

                    if (fragment.isAdded()) {
                        fragment.updateChart(
                                temperatures,
                                offsetHours
                        );

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Temperature>> call, Throwable throwable) {
                if(fragment.isAdded()) {
                    Toast toast = Toast.makeText(fragment.getContext(), "Ошибка сервера", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return fragment;
    }

}