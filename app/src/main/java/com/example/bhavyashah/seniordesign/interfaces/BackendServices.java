package com.example.bhavyashah.seniordesign.interfaces;

import com.example.bhavyashah.seniordesign.Device;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface BackendServices {
    @GET("/app/devices")
    Observable<Response<ArrayList<Device>>> getDevices();

    @GET("/app/fakedata")
    Observable<Response<ArrayList<Device>>> getMockDevices();
}
