package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;
import com.example.bhavyashah.seniordesign.managers.LiveDataManager;
import com.example.bhavyashah.seniordesign.managers.RouterManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private SeniorDesignApplication mApplication;

    public ApplicationModule(SeniorDesignApplication app) {
        this.mApplication = app;
    }

    @Provides
    @Singleton
    public SeniorDesignApplication getApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public DevicesManager providesDevicesManager() {
        return new DevicesManager(mApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public RouterManager providesRouterManager() {
        return new RouterManager(mApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public LiveDataManager providesLiveDataManager() {
        return new LiveDataManager(mApplication.getApplicationContext());
    }
}
