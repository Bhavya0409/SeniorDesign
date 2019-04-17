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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.models.QueueDisc;
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

    @BindView(R.id.queueing_container) RelativeLayout queueingContainer;
    @BindView(R.id.qdisc_spinner) Spinner qdiscSpinner;
    @BindView(R.id.rate_container) RelativeLayout rateContainer;
    @BindView(R.id.rate_selection_spinner) Spinner rateSelectionSpinner;
    @BindView(R.id.update_settings) Button updateSettingsButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.status_text) TextView statusText;

    @Inject RouterManager routerManager;

    String qdiscsAbbrev[] = {"pfo", "tbf", "htb"};
    String qdiscs[] = {"Default", "Smooth Traffic", "Random Classful"};
    String rates[] = {"5", "10", "15", "20", "25"};

    public static int qdisc = 0;
    private String rate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.router_fragment, container, false);
        ButterKnife.bind(this, view);

        ((SeniorDesignApplication)getActivity().getApplicationContext()).getApplicationComponent().inject(this);

        setQdiscSpinner(getActivity());
        setRateSelectionSpinner(getActivity());

        routerManager.getDisc(getQueueDiscCallback);

        return view;
    }

    private void setQdiscSpinner(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, qdiscs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qdiscSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qdisc = position;
                if (position == 1 || position == 2) {
                    rateContainer.setVisibility(View.VISIBLE);
                } else {
                    rateContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        qdiscSpinner.setAdapter(adapter);
    }

    private void setRateSelectionSpinner(Context context) {
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
    public void onClick() {
        updateSettingsButton.setAlpha(.5f);
        updateSettingsButton.setEnabled(false);
        updateSettingsButton.setClickable(false);
        updateSettingsButton.setText("Updating...");
        statusText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String type = (qdisc == 0 || qdisc == 1) ? "ql" : "qf";
        routerManager.setDisc(setQueueDiscCallback, new QueueDisc(type, qdiscsAbbrev[qdisc], qdisc == 0 ? "" : String.valueOf(rate)));
    }

    private void reset() {
        updateSettingsButton.setAlpha(1f);
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
            if (mResponse.isSuccessful()) {
                statusText.setText("Success!");
                reset();
            } else {
                statusText.setText("Couldn't set queue disc.");
            }
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

    private BackendServiceSubscriber<Response<QueueDisc>> getQueueDiscCallback = new BackendServiceSubscriber<Response<QueueDisc>>() {
        private Response<QueueDisc> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                queueingContainer.setVisibility(View.VISIBLE);
                updateSettingsButton.setVisibility(View.VISIBLE);
                QueueDisc queueDisc = mResponse.body();

                if (queueDisc != null) {
                    String qdiscRouter = queueDisc.getQdisc();
                    for (int i = 0; i < qdiscsAbbrev.length; i++) {
                        if (qdiscRouter.equals(qdiscsAbbrev[i])) {
                            qdisc = i;
                            qdiscSpinner.setSelection(qdisc, true);
                            break;
                        }
                    }
                    if (qdisc == 1 || qdisc == 2) {
                        rateContainer.setVisibility(View.VISIBLE);
                    }
                    rate = queueDisc.getRate();

                    for (int i = 0; i < rates.length; i++) {
                        String rateItem = rates[i];
                        if (rate.equals(rateItem)) {
                            rateSelectionSpinner.setSelection(i, true);
                            break;
                        }
                    }

                }

                reset();
            } else {
                statusText.setText("Couldn't get queue disc.");
            }
        }

        @Override
        public void onNext(Response<QueueDisc> queueDiscResponse) {
            mResponse = queueDiscResponse;
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("bhavyawoot error");
            reset();
            Log.i("Error", e.toString());
        }
    };
}
