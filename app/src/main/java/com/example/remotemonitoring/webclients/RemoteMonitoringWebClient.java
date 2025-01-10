package com.example.remotemonitoring.webclients;

import com.example.remotemonitoring.webclients.model.DeviceConfiguration;
import com.example.remotemonitoring.webclients.model.Temperature;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemoteMonitoringWebClient {

    @POST(ApiPaths.CONFIGURATION)
    Call<DeviceConfiguration> saveNewConfiguration(
            @Path("deviceId") String deviceId,
            @Body DeviceConfiguration config
    );

    @GET(ApiPaths.CONFIGURATION)
    Call<DeviceConfiguration> getConfiguration(@Path("deviceId") String deviceId);

    @GET(ApiPaths.LAST_TEMPERATURE)
    Call<Temperature> getLastTemperature(@Path("deviceId") String deviceId);

    @GET(ApiPaths.TEMPERATURE)
    Call<List<Temperature>> getAllTemperature(@Path("deviceId") String deviceId);

}
