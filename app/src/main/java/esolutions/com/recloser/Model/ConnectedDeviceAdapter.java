package esolutions.com.recloser.Model;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/11/2017.
 */

public class ConnectedDeviceAdapter extends RecyclerView.Adapter<ConnectedDeviceAdapter.ConnectedViewHolder> {
    private Context mContext;
    private List<DeviceOnOffEntity> mListDeviceConnected;

    public ConnectedDeviceAdapter(Context context, List<DeviceOnOffEntity> deviceOnOffEntityList) {
        this.mContext = context;
        if (deviceOnOffEntityList == null)
            return;
        this.mListDeviceConnected = new ArrayList<>();
        this.mListDeviceConnected.addAll(deviceOnOffEntityList);
    }

    @Override
    public ConnectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_connected_device, parent, false);
        ConnectedViewHolder viewHolder = new ConnectedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConnectedViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.row_connected_device, null);
            holder = new ConnectedViewHolder(view);
        }

        holder.getTvName().setText(mListDeviceConnected.get(position).getName());
        holder.getTvIP().setText(mListDeviceConnected.get(position).getIpAddress());
        boolean statusOnOff = mListDeviceConnected.get(position).isStatus();
        if (statusOnOff) {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
        } else {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
        }
    }

    @Override
    public int getItemCount() {
        return mListDeviceConnected.size();
    }

    public static class ConnectedViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName, tvIP;
        public final ImageView ivOnOff;

        public ConnectedViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_row_connected_device_name);
            tvIP = (TextView) itemView.findViewById(R.id.tv_row_connected_device_ip);
            ivOnOff = (ImageView) itemView.findViewById(R.id.iv_row_connected_device_name_on_off);
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvIP() {
            return tvIP;
        }

        public ImageView getIvOnOff() {
            return ivOnOff;
        }
    }

    public void refreshAdapter(List<DeviceOnOffEntity> deviceOnOffEntityList) {
        if (deviceOnOffEntityList == null)
            return;
        mListDeviceConnected = deviceOnOffEntityList;
        notifyDataSetChanged();
    }
}
