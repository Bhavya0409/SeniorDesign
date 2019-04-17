package com.example.bhavyashah.seniordesign.models;

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

    public String getType() {
        return type;
    }

    public String getQdisc() {
        return qdisc;
    }

    public String getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "QueueDisc{" +
                "type='" + type + '\'' +
                ", qdisc='" + qdisc + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
