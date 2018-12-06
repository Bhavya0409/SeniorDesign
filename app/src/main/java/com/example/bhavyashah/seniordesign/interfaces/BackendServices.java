package com.example.bhavyashah.seniordesign.interfaces;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;

public interface BackendServices {
    @GET("/devices")
    Observable<Response<ResponseBody>> getDevices();
}
