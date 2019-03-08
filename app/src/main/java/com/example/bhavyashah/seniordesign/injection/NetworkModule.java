package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServices;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule {

    private SeniorDesignApplication mApplication;

    private String REMOTE_IP_ADDRESS = "http://192.168.1.9:8080";

    public NetworkModule(SeniorDesignApplication app) {
        this.mApplication = app;
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor());
        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(REMOTE_IP_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public BackendServices providesBackendClient(Retrofit retrofit) {
        return retrofit.create(BackendServices.class);
    }
}
