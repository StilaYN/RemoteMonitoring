package com.example.remotemonitoring.core.Repository;

import com.example.remotemonitoring.core.entity.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviseStaticRepository implements DeviceRepository {

    private List<Device> devices;

    public DeviseStaticRepository() {
        this.devices = new ArrayList<Device>();
        devices.add(new Device("1121-1122-2222", "6363633663"));
        devices.add(new Device("1112-1122-2222", "6363373663"));
        devices.add(new Device("2212-1122-2222", "9363373663"));

    }

    @Override
    public void addNewDevice(Device device) {
        devices.add(device);
    }

    @Override
    public Device getDeviceByUuid(String uuid) {
        return devices.stream()
                .filter(device -> {return device.getUuid().equals(uuid);})
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<Device> getAllDevice() {
        return devices;
    }
}
