package com.example.bhavyashah.seniordesign.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.R;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.get_devices_button) Button getDevicesButton;
    @BindView(R.id.devices_textview) TextView devicesTextView;

    @Inject DevicesManager devicesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((SeniorDesignApplication)getApplicationContext()).getApplicationComponent().inject(this);
    }

    @OnClick(R.id.get_devices_button)
    public void onClick(View v) {
        Log.i("blahblah", "clicked");
        devicesManager.getDevices(devicesCallback);
    }

    private BackendServiceSubscriber<Response<String>> devicesCallback = new BackendServiceSubscriber<Response<String>>() {

        private Response<String> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                devicesTextView.setText(mResponse.body());
            } else {
                devicesTextView.setText("Error");
            }
        }

        @Override
        public void onNext(Response<String> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            Log.i("Error", e.toString());
        }
    };
}
