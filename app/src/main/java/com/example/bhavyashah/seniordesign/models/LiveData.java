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
        if (Integer.parseInt(uploadSpeed) >= 1000) {
            return String.valueOf(Integer.parseInt(uploadSpeed) / 1000);
        }
        return uploadSpeed;
    }

    public String getDownloadSpeed() {
        if (Integer.parseInt(downloadSpeed) >= 1000) {
            return String.valueOf(Integer.parseInt(downloadSpeed) / 1000);
        }
        return downloadSpeed;
    }

    public String getDownloadUnits() {
        if (Integer.parseInt(downloadSpeed) >= 1000) {
            return "mb/s";
        }
        return "kb/s";
    }

    public String getUploadUnits() {
        if (Integer.parseInt(uploadSpeed) >= 1000) {
            return "mb/s";
        }
        return "kb/s";
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
