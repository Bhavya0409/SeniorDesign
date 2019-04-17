package com.example.bhavyashah.seniordesign.managers;

import android.content.Context;

import com.example.bhavyashah.seniordesign.models.Device;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.BackendServices;
import com.example.bhavyashah.seniordesign.models.DeviceDisc;

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
                callback.onNext(response);
            }

            @Override
            public void error(Throwable throwable) {
                callback.onError(throwable);
            }

            @Override
            public void complete() {
                callback.onCompleted();
            }
        });

        mServices.getDevices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }

    public void getMockDevices(final BackendServiceSubscriber callback) {
        BaseObserver baseObserver = new BaseObserver(new BaseObserver.ObserverInterface() {
            @Override
            public void next(Response response) {
                callback.onNext(response);
            }

            @Override
            public void error(Throwable throwable) {
                callback.onError(throwable);
            }

            @Override
            public void complete() {
                callback.onCompleted();
            }
        });

        mServices.getMockDevices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }

    public void setDeviceName(Device newNameDevice, final BackendServiceSubscriber callback) {
        BaseObserver baseObserver = new BaseObserver(new BaseObserver.ObserverInterface() {
            @Override
            public void next(Response response) {
                callback.onNext(response);
            }

            @Override
            public void error(Throwable throwable) {
                callback.onError(throwable);
            }

            @Override
            public void complete() {
                callback.onCompleted();
            }
        });

        mServices.changeName(newNameDevice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }

    public void setDeviceDisc(DeviceDisc deviceDisc, final BackendServiceSubscriber callback) {
        BaseObserver baseObserver = new BaseObserver(new BaseObserver.ObserverInterface() {
            @Override
            public void next(Response response) {
                callback.onNext(response);
            }

            @Override
            public void error(Throwable throwable) {
                callback.onError(throwable);
            }

            @Override
            public void complete() {
                callback.onCompleted();
            }
        });

        mServices.setDeviceDisc(deviceDisc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
}
