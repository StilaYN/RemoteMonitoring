package com.example.remotemonitoring.core.entity;

public class Device {

    public Device(String name, String uuid) {
        if(name != null && uuid != null && !name.isEmpty() && uuid.isEmpty()) {
            this.name = name;
            this.uuid = uuid;
        } else {
            throw new IllegalArgumentException("Параметры не могут быть пустыми");
        }
    }

    private String name;

    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
