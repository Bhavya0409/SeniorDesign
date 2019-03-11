package com.example.bhavyashah.seniordesign.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.Device;
import com.example.bhavyashah.seniordesign.ExpandableListViewAdapter;
import com.example.bhavyashah.seniordesign.R;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.OnSubmitListener;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnSubmitListener {

    @BindView(R.id.get_devices_button) Button getDevicesButton;
    @BindView(R.id.devices_error_text) TextView devicesTextView;
    @BindView(R.id.devices_list) ExpandableListView expandableListView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Inject DevicesManager devicesManager;

    private ExpandableListViewAdapter adapter;
    private List<String> headers;
    private HashMap<String, Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((SeniorDesignApplication)getApplicationContext()).getApplicationComponent().inject(this);

        headers = new ArrayList<>();
        devices = new HashMap<>();
        adapter = new ExpandableListViewAdapter(this, headers, devices, this);
        expandableListView.setAdapter(adapter);
    }

    @OnClick(R.id.get_devices_button)
    public void onClick(View v) {
        headers.clear();
        devices.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        devicesManager.getDevices(devicesCallback);
    }

    private BackendServiceSubscriber<Response<ArrayList<Device>>> devicesCallback = new BackendServiceSubscriber<Response<ArrayList<Device>>>() {

        private Response<ArrayList<Device>> mResponse;
        @Override
        public void onCompleted() {
            progressBar.setVisibility(View.GONE);
            if (mResponse.isSuccessful()) {
                ArrayList<Device> devicesResponse = mResponse.body();
                for (Device device : devicesResponse) {
                    headers.add(device.getName());
                    devices.put(device.getName(), device);
                }
                adapter.notifyDataSetChanged();
            } else {
                devicesTextView.setText("Couldn't get data. Please try again another time.");
            }
        }

        @Override
        public void onNext(Response<ArrayList<Device>> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            progressBar.setVisibility(View.GONE);
            devicesTextView.setText("Something went wrong...");
            Log.i("Error", e.toString());
        }
    };

    @Override
    public void onSubmit(int groupPosition, String newName) {
        expandableListView.collapseGroup(groupPosition);
        String oldName = headers.get(groupPosition);
        headers.set(groupPosition, newName);
        devices.put(newName, devices.remove(oldName));
    }
}
