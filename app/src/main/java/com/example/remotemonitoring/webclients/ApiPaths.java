package com.example.remotemonitoring.webclients;

public class ApiPaths {

    private ApiPaths(){

    }

    public final static String BASE_URL = "api/mobile";

    public final static String CONFIGURATION = "configuration/{deviceId}";

    public final static String LAST_TEMPERATURE = "temperature/last/{deviceId}";

    public final static String TEMPERATURE = "temperature/{deviceId}";

}
