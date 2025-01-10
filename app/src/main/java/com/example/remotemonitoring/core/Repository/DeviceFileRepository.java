package com.example.remotemonitoring.core.Repository;

import com.example.remotemonitoring.RemoteMonitoringApplication;
import com.example.remotemonitoring.configuration.FileNames;
import com.example.remotemonitoring.core.entity.Device;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeviceFileRepository implements DeviceRepository{

    private List<Device> devices;

    public DeviceFileRepository() {
        Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
        devices = RemoteMonitoringApplication.FILE_SERVICE.read(FileNames.DEVICE_LIST, listType);
        if(devices==null) {
            devices = new ArrayList<>();
        }
    }

    @Override
    public void addNewDevice(Device device) {
        devices.add(device);
        RemoteMonitoringApplication.FILE_SERVICE.write(FileNames.DEVICE_LIST, devices);
    }

    @Override
    public void deleteDevice(String uuid) {
        devices.remove(getDeviceByUuid(uuid));
        RemoteMonitoringApplication.FILE_SERVICE.write(FileNames.DEVICE_LIST, devices);
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
