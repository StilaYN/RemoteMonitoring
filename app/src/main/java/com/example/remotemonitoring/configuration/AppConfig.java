package com.example.remotemonitoring.configuration;

public class AppConfig {

    public AppConfig() {
        serverIp = "37.193.53.6:7329";
        baseUrl = "/api/mobile/";
        updateInterval = 300000;
    }

    private String serverIp;

    private String baseUrl;

    private long updateInterval;

    public void update(String serverIp, String baseUrl, Long updateInterval) {
        this.serverIp = (serverIp == null) ? this.serverIp : serverIp;
        this.baseUrl = (baseUrl == null) ? this.baseUrl : baseUrl;
        this.updateInterval = (updateInterval == null) ? this.updateInterval : updateInterval;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }
}
