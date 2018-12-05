package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
}
