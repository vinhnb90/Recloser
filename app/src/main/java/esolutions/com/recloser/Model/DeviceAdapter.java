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

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/11/2017.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private Context mContext;
    private static List<DeviceEntity> sListDevice;
    private static CallBackDeviceApdater sListener;

    public DeviceAdapter(Context context, List<DeviceEntity> sListDevice) {
        this.mContext = context;
        if (context instanceof CallBackDeviceApdater) {
            sListener = (CallBackDeviceApdater) context;
        } else throw new ClassCastException("class must be implement CallBackDeviceApdater!");

        if (sListDevice == null)
            return;
        this.sListDevice = new ArrayList<>();
        this.sListDevice.addAll(sListDevice);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_all_device, parent, false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.row_all_device, null);
            holder = new DeviceViewHolder(view);
        }

        holder.getTvName().setText(sListDevice.get(position).getName());
        holder.getTvIP().setText(sListDevice.get(position).getIpAddress());
        boolean statusOnOff = sListDevice.get(position).isStatus();
        //true = device Connect, false = device Disconnect
        if (statusOnOff) {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
        } else {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
        }
    }

    @Override
    public int getItemCount() {
        return sListDevice.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName, tvIP;
        public final ImageView ivOnOff;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_row_device_name);
            tvIP = (TextView) itemView.findViewById(R.id.tv_row_device_ip);
            ivOnOff = (ImageView) itemView.findViewById(R.id.iv_row_device_name_on_off);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeviceEntity deviceEntity = sListDevice.get(getAdapterPosition());
                    sListener.clickRowDevice(deviceEntity);
                }
            });
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

    public void refreshAdapter(List<DeviceEntity> entityList) {
        if (entityList == null)
            return;
        sListDevice = entityList;
        notifyDataSetChanged();
    }

    public interface CallBackDeviceApdater {
        void clickRowDevice(DeviceEntity deviceEntity);
    }
}
