package com.example.bhavyashah.seniordesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> dataHeaders;
    private HashMap<String, Device> devices;

    public ExpandableListViewAdapter(Context context, List<String> dataHeaders, HashMap<String, Device> devices) {
        this.context = context;
        this.dataHeaders = dataHeaders;
        this.devices = devices;
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Device device = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.content, null);
        }
        TextView macAddress = convertView.findViewById(R.id.device_mac_address);
        TextView ipAddress = convertView.findViewById(R.id.device_ip_address);
        TextView ulData = convertView.findViewById(R.id.device_ul_data);
        TextView dlData = convertView.findViewById(R.id.device_dl_data);
        macAddress.setText(device.getMacAddress());
        ipAddress.setText(device.getIpAddress());
        ulData.setText(device.getUploadData());
        dlData.setText(device.getDownloadData());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
