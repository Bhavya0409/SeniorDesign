package com.example.bhavyashah.seniordesign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.interfaces.BackendServiceSubscriber;
import com.example.bhavyashah.seniordesign.interfaces.OnSubmitListener;
import com.example.bhavyashah.seniordesign.managers.DevicesManager;
import com.example.bhavyashah.seniordesign.models.Device;

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
    private EditText deviceNameEditText;
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
        Button changeDeviceName = convertView.findViewById(R.id.change_device_name);
        final EditText newDeviceName = convertView.findViewById(R.id.new_device_name);

        macAddress.setText(device.getMacAddress());
        ipAddress.setText(device.getIpAddress());
        ulData.setText(device.getUploadData());
        dlData.setText(device.getDownloadData());
        changeDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = (Button)v;
                deviceNameEditText = newDeviceName;
                groupPositionOfDeviceNameChange = groupPosition;

                newName = newDeviceName.getText().toString();
                Device newNameDevice = new Device();
                newNameDevice.setName(newName);
                newNameDevice.setMacAddress(device.getMacAddress());
                v.setAlpha(.5f);
                ((Button)v).setText("Setting...");
                v.setClickable(false);
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
                deviceNameEditText.setText("");
                listener.onSubmit(groupPositionOfDeviceNameChange, newName);
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
}
