package com.example.bhavyashah.seniordesign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.fragments.RouterFragment;
import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.OnSubmitListener;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;
import com.example.bhavyashah.seniordesign.models.Device;
import com.example.bhavyashah.seniordesign.models.DeviceDisc;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    @Inject DevicesManager devicesManager;

    private Context context;
    private List<String> dataHeaders;
    private HashMap<String, Device> devices;
    private OnSubmitListener listener;

    private String newName = "";
    private Button buttonClicked;
    private String deviceNameToReset;
    private TextView statusText;
    private int groupPositionOfDeviceNameChange;

    public ExpandableListViewAdapter(Context context, List<String> dataHeaders, HashMap<String, Device> devices, OnSubmitListener listener) {
        this.context = context;
        this.dataHeaders = dataHeaders;
        this.devices = devices;
        this.listener = listener;

        ((SeniorDesignApplication)context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public int getGroupCount() {
        return this.dataHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String  getGroup(int groupPosition) {
        return this.dataHeaders.get(groupPosition);
    }

    @Override
    public Device getChild(int groupPosition, int childPosition) {
        return this.devices.get(this.dataHeaders.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.device_header);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Device device = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.content, null);
        }
        TextView macAddress = convertView.findViewById(R.id.device_mac_address);
        TextView ipAddress = convertView.findViewById(R.id.device_ip_address);
        TextView ulData = convertView.findViewById(R.id.device_ul_data);
        TextView dlData = convertView.findViewById(R.id.device_dl_data);
        statusText = convertView.findViewById(R.id.status_text);

        Button changeDeviceName = convertView.findViewById(R.id.change_device_name);
        Button updateDeviceQdisc = convertView.findViewById(R.id.update_device_qdisc);

        LinearLayout deviceQueueDisc = convertView.findViewById(R.id.device_queue_disc);

        if (RouterFragment.qdisc == 2) {
            deviceQueueDisc.setVisibility(View.VISIBLE);
            final TextView ratePercent = convertView.findViewById(R.id.rate_percent);
            final TextView ceilingPercent = convertView.findViewById(R.id.ceiling_percent);
            final TextView priorityPercent = convertView.findViewById(R.id.priority_percent);

            final SeekBar rateSeekbar = convertView.findViewById(R.id.rate_seekbar);
            final SeekBar ceilingSeekbar = convertView.findViewById(R.id.ceiling_seekbar);
            final SeekBar prioritySeekbar = convertView.findViewById(R.id.priority_seekbar);

            SeekBar.OnSeekBarChangeListener rateSeekbarListener = new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ratePercent.setText(String.valueOf(progress));
                    rateSeekbar.setProgress(progress);

                    int ceiling = ceilingSeekbar.getProgress();
                    if (ceiling > progress) {
                        ceilingSeekbar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
            SeekBar.OnSeekBarChangeListener ceilingSeekbarListener = new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ceilingPercent.setText(String.valueOf(progress));
                    ceilingSeekbar.setProgress(progress);

                    int rate = rateSeekbar.getProgress();
                    if (rate > progress) {
                        rateSeekbar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
            SeekBar.OnSeekBarChangeListener prioritySeekbarListener = new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    priorityPercent.setText(String.valueOf(progress - 5));
                    prioritySeekbar.setProgress(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

            rateSeekbar.setOnSeekBarChangeListener(rateSeekbarListener);
            ceilingSeekbar.setOnSeekBarChangeListener(ceilingSeekbarListener);
            prioritySeekbar.setOnSeekBarChangeListener(prioritySeekbarListener);

            final Button setToDefaultButton = convertView.findViewById(R.id.set_to_default);

            setToDefaultButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked = (Button) v;
                    deviceNameToReset = device.getName();
                    v.setAlpha(.5f);
                    v.setClickable(false);
                    v.setEnabled(false);
                    statusText.setVisibility(View.GONE);
                    buttonClicked.setText("Resetting...");
                    DeviceDisc deviceDisc = new DeviceDisc(device.getMacAddress(), -1, -1, -1);
                    devicesManager.setDeviceDisc(deviceDisc, setDeviceDefaultCallbadk);
                }
            });

            int classRate = device.getClassRate();
            int classCeiling = device.getClassCeiling();
            int classPriority = device.getClassPriority();

            if (classRate == -1) {
                ratePercent.setText("0");
                rateSeekbar.setProgress(0);
            } else {
                ratePercent.setText(String.valueOf(classRate));
                rateSeekbar.setProgress(classRate);
            }


            if (classCeiling == -1) {
                ceilingPercent.setText("100");
                ceilingSeekbar.setProgress(100);
            } else {
                ceilingPercent.setText(String.valueOf(classCeiling));
                ceilingSeekbar.setProgress(classCeiling);
            }


            if (classPriority == -1) {
                priorityPercent.setText("0");
                prioritySeekbar.setProgress(5);
            } else {
                priorityPercent.setText(String.valueOf(classPriority));
                prioritySeekbar.setProgress(classPriority);
            }

            updateDeviceQdisc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked = (Button)v;
                    v.setAlpha(.5f);
                    v.setClickable(false);
                    v.setEnabled(false);
                    statusText.setVisibility(View.GONE);
                    buttonClicked.setText("Updating...");
                    int rate = rateSeekbar.getProgress();
                    int ceiling = ceilingSeekbar.getProgress();
                    int priority = prioritySeekbar.getProgress();
                    DeviceDisc deviceDisc = new DeviceDisc(device.getMacAddress(), rate, ceiling, priority);
                    devicesManager.setDeviceDisc(deviceDisc, setDeviceDiscCallback);
                }
            });

        }


        final EditText newDeviceName = convertView.findViewById(R.id.new_device_name);

        macAddress.setText(device.getMacAddress());
        ipAddress.setText(device.getIpAddress());
        ulData.setText(device.getUploadData());
        dlData.setText(device.getDownloadData());
        if (device.getNetworkName().trim().length() != 0) {
            LinearLayout networkNameContainer = convertView.findViewById(R.id.network_name_container);
            TextView networkName = convertView.findViewById(R.id.device_network_name);

            networkNameContainer.setVisibility(View.VISIBLE);
            networkName.setText(device.getNetworkName());
        }
        changeDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = (Button)v;
                groupPositionOfDeviceNameChange = groupPosition;

                newName = newDeviceName.getText().toString();
                Device newNameDevice = new Device();
                newNameDevice.setName(newName);
                newNameDevice.setMacAddress(device.getMacAddress());
                v.setAlpha(.5f);
                v.setClickable(false);
                v.setEnabled(false);
                ((Button)v).setText("Setting...");
                devicesManager.setDeviceName(newNameDevice, changeDeviceNameCallback);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private BackendServiceSubscriber<Response<String>> changeDeviceNameCallback = new BackendServiceSubscriber<Response<String>>() {

        private Response<String> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                buttonClicked.setAlpha(1f);
                buttonClicked.setClickable(true);
                buttonClicked.setText("Set");
                buttonClicked = null;
                listener.onNameChangeSuccess(groupPositionOfDeviceNameChange, newName);
            } else {
                Log.i("blahblah", "failure");
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

    private BackendServiceSubscriber<Response<String>> setDeviceDiscCallback = new BackendServiceSubscriber<Response<String>>() {

        private Response<String> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                buttonClicked.setAlpha(1f);
                buttonClicked.setClickable(true);
                buttonClicked.setEnabled(true);
                buttonClicked.setText("Update Device Queueing Discipline");
                buttonClicked = null;
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("Success!");
            } else {
                statusText.setText("Failed.");
                Log.i("blahblah", "failure");
            }
        }

        @Override
        public void onNext(Response<String> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            statusText.setText("Something went wrong.");
            Log.i("Error", e.toString());
        }
    };

    private BackendServiceSubscriber<Response<String>> setDeviceDefaultCallbadk = new BackendServiceSubscriber<Response<String>>() {

        private Response<String> mResponse;
        @Override
        public void onCompleted() {
            if (mResponse.isSuccessful()) {
                buttonClicked.setAlpha(1f);
                buttonClicked.setClickable(true);
                buttonClicked.setEnabled(true);
                buttonClicked.setText("Set To Default");
                buttonClicked = null;
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("Success!");
                listener.onResetDevice(deviceNameToReset);
            } else {
                statusText.setText("Failed.");
                Log.i("blahblah", "failure");
            }
        }

        @Override
        public void onNext(Response<String> stringResponse) {
            mResponse = stringResponse;
        }

        @Override
        public void onError(Throwable e) {
            statusText.setText("Something went wrong.");
            Log.i("Error", e.toString());
        }
    };
}
