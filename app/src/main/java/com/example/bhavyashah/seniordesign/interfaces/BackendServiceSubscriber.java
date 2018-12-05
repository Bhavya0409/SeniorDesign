package com.example.bhavyashah.seniordesign.interfaces;

public interface BackendServiceSubscriber<T> {

    void onCompleted();

    void onNext(T t);

    void onError(Throwable e);

}
