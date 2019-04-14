package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.ExpandableListViewAdapter;
import com.example.bhavyashah.seniordesign.fragments.DevicesFragment;
import com.example.bhavyashah.seniordesign.fragments.RouterFragment;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(DevicesFragment devicesFragment);

    void inject(RouterFragment routerFragment);

    void inject(DevicesManager devicesManager);

    void inject(ExpandableListViewAdapter expandableListViewAdapter);
}
