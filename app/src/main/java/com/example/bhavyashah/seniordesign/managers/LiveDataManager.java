package com.example.bhavyashah.seniordesign.managers;

import android.content.Context;

import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.BackendServices;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LiveDataManager {
    @Inject
    BackendServices mServices;

    public LiveDataManager(Context context) {
        ((SeniorDesignApplication)context).getApplicationComponent().inject(this);
    }

    public void getLiveData(final BackendServiceSubscriber callback) {
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

        mServices.getLiveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
}
