package com.example.bhavyashah.seniordesign;

import android.app.Application;

import com.example.bhavyashah.seniordesign.injection.ApplicationComponent;
import com.example.bhavyashah.seniordesign.injection.ApplicationModule;
import com.example.bhavyashah.seniordesign.injection.DaggerApplicationComponent;
import com.example.bhavyashah.seniordesign.injection.NetworkModule;
import com.facebook.stetho.Stetho;

public class SeniorDesignApplication extends Application {

    protected ApplicationComponent mApplicationComponent;

    private static SeniorDesignApplication mApplication;

    public SeniorDesignApplication() {
        synchronized (this) {
            mApplication = this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(this))
                .build();

        Stetho.initializeWithDefaults(this);
    }

    public static synchronized SeniorDesignApplication get() {
        return mApplication;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
