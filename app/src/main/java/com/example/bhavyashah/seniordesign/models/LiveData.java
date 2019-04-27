package com.example.bhavyashah.seniordesign.models;

import com.google.gson.annotations.SerializedName;

public class LiveData {
    @SerializedName("mac_address") private String macAddress;
    @SerializedName("name") private String name;
    @SerializedName("ul") private String uploadSpeed;
    @SerializedName("dl") private String downloadSpeed;

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public String getUploadSpeed() {
        return uploadSpeed;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    @Override
    public String toString() {
        return "LiveData{" +
                "macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                ", uploadSpeed='" + uploadSpeed + '\'' +
                ", downloadSpeed='" + downloadSpeed + '\'' +
                '}';
    }
}
