package com.example.bhavyashah.seniordesign.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.Device;
import com.example.bhavyashah.seniordesign.R;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;

import java.util.ArrayList;

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
        devicesManager.getMockDevices(devicesCallback);
    }

    private BackendServiceSubscriber<Response<ArrayList<Device>>> devicesCallback = new BackendServiceSubscriber<Response<ArrayList<Device>>>() {

        private Response<ArrayList<Device>> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                ArrayList<Device> devices = mResponse.body();
                for (Device device : devices)
                {
                    Log.i("blahblah name", device.getMacAddress());
                    Log.i("blahblah ul", device.getUploadData());
                    Log.i("blahblah dl", device.getDownloadData());
                    Log.i("blahblah last", device.getLastContact());
                }
                devicesTextView.setText(devices.get(0).getMacAddress());
            } else {
                devicesTextView.setText("Error");
            }
        }

        @Override
        public void onNext(Response<ArrayList<Device>> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            Log.i("Error", e.toString());
        }
    };
}
