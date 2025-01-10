package com.example.remotemonitoring.webclients.model;

public record DeviceConfiguration(
        long updateInterval,

        int minCriticalTemperature,

        int maxCriticalTemperature
) {
}
