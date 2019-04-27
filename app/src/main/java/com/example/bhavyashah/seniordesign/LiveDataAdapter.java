package com.example.bhavyashah.seniordesign;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bhavyashah.seniordesign.models.LiveData;

import java.util.ArrayList;

public class LiveDataAdapter extends ArrayAdapter<LiveData> {

    private ArrayList<LiveData> devices;
    private Context context;

    public LiveDataAdapter(Context context, int resource, ArrayList<LiveData> objects) {
        super(context, resource, objects);
        this.devices = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.live_data_row, parent, false);
        TextView deviceNameTextView = rowView.findViewById(R.id.device_name);
        TextView uploadDataTextView = rowView.findViewById(R.id.upload_data);
        TextView downloadDataTextView = rowView.findViewById(R.id.download_data);

        LiveData device = devices.get(position);

        deviceNameTextView.setText(device.getName());
        uploadDataTextView.setText(device.getUploadSpeed());
        downloadDataTextView.setText(device.getDownloadSpeed());

        return rowView;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public LiveData getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
