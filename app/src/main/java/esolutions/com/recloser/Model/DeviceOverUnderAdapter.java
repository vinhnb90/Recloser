package esolutions.com.recloser.Model;

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

public class DeviceOverUnderAdapter extends RecyclerView.Adapter<DeviceOverUnderAdapter.OverUnderViewHolder> {
    private Context mContext;
    private List<DeviceOnOffEntity> mListDeviceOnOff;

    public DeviceOverUnderAdapter(Context context, List<DeviceOnOffEntity> mListDeviceOnOff) {
        this.mContext = context;

        if (mListDeviceOnOff == null)
            return;
        this.mListDeviceOnOff = new ArrayList<>();
        this.mListDeviceOnOff.addAll(mListDeviceOnOff);
    }

    @Override
    public OverUnderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_device_on_off, parent, false);
        OverUnderViewHolder viewHolder = new OverUnderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OverUnderViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.row_device_on_off, null);
            holder = new OverUnderViewHolder(view);
        }

        holder.getTvName().setText(mListDeviceOnOff.get(position).getName());
        holder.getTvIP().setText(mListDeviceOnOff.get(position).getIpAddress());
        boolean statusOnOff = mListDeviceOnOff.get(position).isStatus();
        //true = device Disconnect, false = device Connect
        if (statusOnOff) {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
        } else {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
        }
    }

    @Override
    public int getItemCount() {
        return mListDeviceOnOff.size();
    }

    public static class OverUnderViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName, tvIP;
        public final ImageView ivOnOff;

        public OverUnderViewHolder(View itemView) {
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

    public void refreshAdapter(List<DeviceOnOffEntity> entityList) {
        if (entityList == null)
            return;
        mListDeviceOnOff = entityList;
        notifyDataSetChanged();
    }
}
