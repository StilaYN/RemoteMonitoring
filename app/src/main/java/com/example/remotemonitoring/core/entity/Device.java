package com.example.remotemonitoring.core.entity;

public class Device {

    public Device(String name, String uuid) {
        this.name = (name!=null)?name:"name";
        this.uuid = (uuid!=null)?uuid:"uuid";
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
