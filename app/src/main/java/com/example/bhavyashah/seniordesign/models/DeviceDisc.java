package com.example.bhavyashah.seniordesign.models;

import com.google.gson.annotations.SerializedName;

public class DeviceDisc {
    @SerializedName("mac_address") private String macAddress;
    @SerializedName("rate") private int rate;
    @SerializedName("ceiling") private int ceiling;
    @SerializedName("priority") private int priority;

    public DeviceDisc(String macAddress, int rate, int ceiling, int priority) {
        this.macAddress = macAddress;
        this.rate = rate;
        this.ceiling = ceiling;
        this.priority = priority;
    }
}
