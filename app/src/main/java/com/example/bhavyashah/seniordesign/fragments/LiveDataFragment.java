package com.example.bhavyashah.seniordesign.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.LiveDataAdapter;
import com.example.bhavyashah.seniordesign.R;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.managers.LiveDataManager;
import com.example.bhavyashah.seniordesign.models.LiveData;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class LiveDataFragment extends Fragment {

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.devices_list) ListView listView;
    @BindView(R.id.devices_error_text) TextView devicesTextView;
    @BindView(R.id.refreshing_text) TextView refreshingText;

    @Inject LiveDataManager liveDataManager;

    private static final int SECONDS_TO_REFRESH = 4;

    private ArrayList<LiveData> devices = new ArrayList<>();
    private LiveDataAdapter adapter;
    private boolean refreshedDataOnce = false;

    private Timer timer = new Timer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_data_fragment, container, false);
        ButterKnife.bind(this, view);
        ((SeniorDesignApplication)getActivity().getApplicationContext()).getApplicationComponent().inject(this);

        adapter = new LiveDataAdapter(getActivity(), -1, devices);
        listView.setAdapter(adapter);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        getLiveData();
                    }
                });
            }
        }, 0, SECONDS_TO_REFRESH * 1000);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void getLiveData() {
        if (refreshedDataOnce) {
            refreshingText.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.VISIBLE);
        liveDataManager.getLiveData(getLiveDataCallback);
    }

    private void getLiveDataComplete(ArrayList<LiveData> liveDataItems) {
        refreshingText.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        refreshedDataOnce = true;

        devices.clear();

        if (liveDataItems != null) {
            devices.addAll(liveDataItems);
            adapter.notifyDataSetChanged();
        } else {
            devicesTextView.setText("No devices to show.");
        }
    }

    private BackendServiceSubscriber<Response<ArrayList<LiveData>>> getLiveDataCallback = new BackendServiceSubscriber<Response<ArrayList<LiveData>>>() {

        private Response<ArrayList<LiveData>> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                ArrayList<LiveData> liveDataItems = mResponse.body();
                getLiveDataComplete(liveDataItems);
            } else {
                devicesTextView.setText("Couldn't get data. Please try again another time.");
            }
        }

        @Override
        public void onNext(Response<ArrayList<LiveData>> stringResponse) {
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
