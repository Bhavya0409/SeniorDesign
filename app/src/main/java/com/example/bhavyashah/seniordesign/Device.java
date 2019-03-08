package com.example.bhavyashah.seniordesign;

import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("mac_address")
    private String macAddress;

    @SerializedName("ip_address")
    private String ipAddress;

    @SerializedName("ul_data")
    private String uploadData;

    @SerializedName("dl_data")
    private String downloadData;

    @SerializedName("last_contact")
    private String lastContact;

    public String getMacAddress() {
        return macAddress;
    }

    public String getUploadData() {
        return uploadData;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public String getDownloadData() {
        return downloadData;
    }

    public String getLastContact() {
        return lastContact;
    }
}
