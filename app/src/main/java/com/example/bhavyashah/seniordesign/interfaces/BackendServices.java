package com.example.bhavyashah.seniordesign.interfaces;

import com.example.bhavyashah.seniordesign.Device;
import com.example.bhavyashah.seniordesign.QueueDisc;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BackendServices {
    @GET("/app/devices")
    Observable<Response<ArrayList<Device>>> getDevices();

    @GET("/app/fakedata")
    Observable<Response<ArrayList<Device>>> getMockDevices();

    @POST("/app/namechange")
    Observable<Response<String>> changeName(@Body Device device);

    @POST("/app/setdisc")
    Observable<Response<String>> setDisc(@Body QueueDisc queueDisc);
}
