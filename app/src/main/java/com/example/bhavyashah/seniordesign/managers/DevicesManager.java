package com.example.bhavyashah.seniordesign.managers;

import android.content.Context;
import android.util.Log;

import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.BackendServices;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DevicesManager {
    @Inject BackendServices mServices;

    public DevicesManager(Context context) {
        ((SeniorDesignApplication)context).getApplicationComponent().inject(this);
    }

    public void getDevices(final BackendServiceSubscriber callback) {
        BaseObserver baseObserver = new BaseObserver(new BaseObserver.ObserverInterface() {
            @Override
            public void next(Response response) {
                Log.i("blahblah", "in next" + response.message());
                callback.onNext(response);
            }

            @Override
            public void error(Throwable throwable) {
                Log.i("blahblah", "in error" + throwable.getMessage());
                callback.onError(throwable);
            }

            @Override
            public void complete() {
                Log.i("blahblah", "in complete");
                callback.onCompleted();
            }
        });

        mServices.getDevices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
}
