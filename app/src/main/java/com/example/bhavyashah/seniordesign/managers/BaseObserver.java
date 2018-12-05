package com.example.bhavyashah.seniordesign.managers;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import retrofit2.Response;

public class BaseObserver implements Observer<Response> {

    private static final String TAG = BaseObserver.class.getSimpleName();
    private ObserverInterface observerInterface;

    interface ObserverInterface {

        void next(Response response);

        void error(Throwable throwable);

        void complete();

    }

    public BaseObserver(ObserverInterface observerInterface) {
        this.observerInterface = observerInterface;
    }

    @Override
    public void onSubscribe(Disposable d) {
        //no-op
    }

    @Override
    public void onNext(Response response) {
        try {
            observerInterface.next(response);
        } catch (HttpException error) {
            Log.e(TAG, error.getMessage(), error);
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            observerInterface.error(e);
        } catch (Exception error) {
            Log.e(TAG, error.getMessage(), error);
        }
    }

    @Override
    public void onComplete() {
        try {
            observerInterface.complete();
        } catch (Exception error) {
            Log.e(TAG, error.getMessage(), error);
        }
    }
}
