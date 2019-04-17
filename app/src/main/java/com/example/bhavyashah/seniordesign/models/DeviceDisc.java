package com.example.bhavyashah.seniordesign.models;

import com.google.gson.annotations.SerializedName;

public class DeviceDisc {
    @SerializedName("mac_address") private String macAddress;
    @SerializedName("rate") private int rate;
    @SerializedName("ceiling") private int ceiling;
    @SerializedName("priority") private int priority;

    public DeviceDisc() {
        // Default Constructor
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setRate(int rate) {
        if (rate >= 0 && rate <= 100) {
            this.rate = rate;
        }
    }

    public void setCeiling(int ceiling) {
        if (ceiling >= 0 && ceiling <= 100) {
            this.ceiling = ceiling;
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
