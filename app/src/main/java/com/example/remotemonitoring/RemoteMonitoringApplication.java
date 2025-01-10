package com.example.remotemonitoring;

import android.app.Application;

import com.example.remotemonitoring.configuration.AppConfig;
import com.example.remotemonitoring.configuration.CustomExceptionHandler;
import com.example.remotemonitoring.configuration.FileNames;
import com.example.remotemonitoring.configuration.OffsetDateTimeAdapter;
import com.example.remotemonitoring.core.Repository.DeviceFileRepository;
import com.example.remotemonitoring.core.Repository.DeviceRepository;
import com.example.remotemonitoring.core.service.FileService;
import com.example.remotemonitoring.webclients.RemoteMonitoringWebClient;
import com.github.terrakok.cicerone.Cicerone;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Router;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.OffsetDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteMonitoringApplication extends Application {
    public static RemoteMonitoringApplication INSTANCE;

    public static FileService FILE_SERVICE;

    private AppConfig config;

    private Cicerone<Router> cicerone;

    private Retrofit retrofit;

    private Retrofit.Builder retrofitBuilder;

    private RemoteMonitoringWebClient remoteMonitoringWebClient;

    private DeviceRepository deviceRepository;

    private final String HTTP_START = "http://";

    private Gson gson;


    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
        INSTANCE = this;
        FILE_SERVICE = new FileService(this);
        cicerone = Cicerone.create();
        AppConfig result = FILE_SERVICE.read(FileNames.CONFIG_NAME, AppConfig.class);
        config = (result == null) ? new AppConfig() : result;
        gson = new GsonBuilder().registerTypeAdapter(
                        OffsetDateTime.class,
                        new OffsetDateTimeAdapter()
                ).create();
    }

    public NavigatorHolder getNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    public Router getRouter() {
        return cicerone.getRouter();
    }

    public void updateConfig(String serverIp, String baseUrl, Long updateInterval) {
        config.update(serverIp, baseUrl, updateInterval);
        FILE_SERVICE.write(FileNames.CONFIG_NAME, config);
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(HTTP_START + config.getServerIp() + config.getBaseUrl());
        retrofit = null;
        remoteMonitoringWebClient = null;

    }

    public DeviceRepository getDeviceRepository() {
        if (deviceRepository == null) {
            deviceRepository = new DeviceFileRepository();
        }
        return deviceRepository;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            if (retrofitBuilder == null) {
                retrofitBuilder = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(HTTP_START + config.getServerIp() + config.getBaseUrl());
            }
            retrofit = retrofitBuilder.build();
        }
        return retrofit;
    }

    public RemoteMonitoringWebClient getRemoteMonitoringWebClient() {
        if (remoteMonitoringWebClient == null) {
            remoteMonitoringWebClient = getRetrofit().create(RemoteMonitoringWebClient.class);
        }
        return remoteMonitoringWebClient;
    }

}
