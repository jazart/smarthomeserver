package com.home.smarthomeserver.devices;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

public class Light extends AWSIotDevice {
    @AWSIotDeviceProperty
    int power;

    public Light(String thingName) {
        super(thingName);
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }
}
