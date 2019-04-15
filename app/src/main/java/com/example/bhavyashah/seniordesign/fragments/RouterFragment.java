package com.example.bhavyashah.seniordesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.QueueDisc;
import com.example.bhavyashah.seniordesign.R;
import com.example.bhavyashah.seniordesign.SeniorDesignApplication;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.managers.RouterManager;



import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class RouterFragment extends Fragment {

    @BindView(R.id.qdisc_spinner) Spinner qdiscSpinner;
    @BindView(R.id.rate_selection_spinner) Spinner rateSelectionSpinner;
    @BindView(R.id.update_settings) Button updateSettingsButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.status_text) TextView statusText;

    @Inject RouterManager routerManager;

    private int qdisc = 0;
    private String rate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.router_fragment, container, false);
        ButterKnife.bind(this, view);

        ((SeniorDesignApplication)getActivity().getApplicationContext()).getApplicationComponent().inject(this);

        setQdiscSpinner(getActivity());
        setRateSelectionSpinner(getActivity());

        return view;
    }

    private void setQdiscSpinner(Context context) {
        String qdiscs[] = {"Default", "Smooth Traffic", "Random Classful"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, qdiscs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qdiscSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qdisc = position;
                if (position == 1) {
                    rateSelectionSpinner.setEnabled(true);
                    rateSelectionSpinner.setClickable(true);
                } else {
                    rateSelectionSpinner.setEnabled(false);
                    rateSelectionSpinner.setClickable(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        qdiscSpinner.setAdapter(adapter);
    }

    private void setRateSelectionSpinner(Context context) {
        String rates[] = {"5", "10", "15"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, rates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateSelectionSpinner.setAdapter(adapter);
        rateSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rate = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.update_settings)
    public void onClick(View v) {
        updateSettingsButton.setEnabled(false);
        updateSettingsButton.setClickable(false);
        updateSettingsButton.setText("Updating...");
        statusText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String type = (qdisc == 0 || qdisc == 1) ? "ql" : "qf";
        String qdiscs[] = {"pfifo", "tbf", "htb"};
        routerManager.setDisc(setQueueDiscCallback, new QueueDisc(type, qdiscs[qdisc], (qdisc == 0 || qdisc == 2) ? "" : String.valueOf(rate)));
    }

    private void reset() {
        updateSettingsButton.setEnabled(true);
        updateSettingsButton.setClickable(true);
        updateSettingsButton.setText("Update");
        statusText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private BackendServiceSubscriber<Response<String>> setQueueDiscCallback = new BackendServiceSubscriber<Response<String>>() {
        private Response<String> mResponse;
        @Override
        public void onCompleted() {
            statusText.setText("Success!");
            reset();
        }

        @Override
        public void onNext(Response<String> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            statusText.setText("Something went wrong...");
            reset();
            Log.i("Error", e.toString());
        }
    };
}
