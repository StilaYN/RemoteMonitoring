package com.example.remotemonitoring.webclients.model;

import java.time.OffsetDateTime;

public record Temperature(
        int temperature,

        OffsetDateTime timestamp,

        boolean isCritical
) {
}
