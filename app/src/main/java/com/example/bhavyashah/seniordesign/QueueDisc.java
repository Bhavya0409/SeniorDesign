package com.example.bhavyashah.seniordesign;

import com.google.gson.annotations.SerializedName;

public class QueueDisc {
    @SerializedName("type") private String type;
    @SerializedName("qdisc") private String qdisc;
    @SerializedName("rate") private String rate;

    public QueueDisc(String type, String qdisc, String rate) {
        this.type = type;
        this.qdisc = qdisc;
        this.rate = rate;
    }
}
