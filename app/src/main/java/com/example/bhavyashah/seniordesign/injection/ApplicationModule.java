package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;

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
}
