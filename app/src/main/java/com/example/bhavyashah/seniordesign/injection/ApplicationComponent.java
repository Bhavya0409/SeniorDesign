package com.example.bhavyashah.seniordesign.injection;

import com.example.bhavyashah.seniordesign.ExpandableListViewAdapter;
import com.example.bhavyashah.seniordesign.fragments.DevicesFragment;
import com.example.bhavyashah.seniordesign.fragments.LiveDataFragment;
import com.example.bhavyashah.seniordesign.fragments.RouterFragment;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;
import com.example.bhavyashah.seniordesign.managers.LiveDataManager;
import com.example.bhavyashah.seniordesign.managers.RouterManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(DevicesFragment devicesFragment);

    void inject(RouterFragment routerFragment);

    void inject(LiveDataFragment liveDataFragment);

    void inject(DevicesManager devicesManager);

    void inject(RouterManager routerManager);

    void inject(LiveDataManager liveDataManager);

    void inject(ExpandableListViewAdapter expandableListViewAdapter);
}
