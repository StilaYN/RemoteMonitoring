package com.example.remotemonitoring.core.Repository;

import com.example.remotemonitoring.core.entity.Device;

import java.util.List;

public interface DeviceRepository {

    void addNewDevice(Device device);

    void deleteDevice(String uuid);

    Device getDeviceByUuid(String uuid);

    List<Device> getAllDevice();
}
