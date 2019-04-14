package com.example.bhavyashah.seniordesign.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class DevicesFragment extends Fragment implements OnSubmitListener {

    @BindView(R.id.get_devices_button) Button getDevicesButton;
    @BindView(R.id.devices_error_text) TextView devicesTextView;
    @BindView(R.id.devices_list) ExpandableListView expandableListView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Inject DevicesManager devicesManager;

    private ExpandableListViewAdapter adapter;
    private static List<String> headers = new ArrayList<>();
    private static HashMap<String, Device> devices = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.devices_fragment, container, false);
        ButterKnife.bind(this, view);


        ((SeniorDesignApplication)getActivity().getApplicationContext()).getApplicationComponent().inject(this);

        adapter = new ExpandableListViewAdapter(getActivity(), headers, devices, this);
        expandableListView.setAdapter(adapter);

        return view;
    }

    @OnClick(R.id.get_devices_button)
    public void onClick(View v) {
        headers.clear();
        devices.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        devicesManager.getDevices(devicesCallback);
    }

    @Override
    public void onSubmit(int groupPosition, String newName) {
        expandableListView.collapseGroup(groupPosition);
        String oldName = headers.get(groupPosition);
        headers.set(groupPosition, newName);
        devices.put(newName, devices.remove(oldName));
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
}
