package com.uni.aychat.dto;

import com.google.gson.annotations.SerializedName;

public class ResLogout {
    @SerializedName("signal")
    private int signal;

    public ResLogout(){}
    public ResLogout(int signal) {
        this.signal = signal;
    }

    public int getSignal() {
        return signal;
    }
}