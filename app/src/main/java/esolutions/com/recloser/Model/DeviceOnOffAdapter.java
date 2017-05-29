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

public class DeviceOnOffAdapter extends RecyclerView.Adapter<DeviceOnOffAdapter.ConnectedViewHolder> {
    private Context mContext;
    private static List<DeviceOnOffEntity> sListDeviceOnOff;
    private static CallbackDeviceOnOffAdapter sListener;

    public DeviceOnOffAdapter(Context context, List<DeviceOnOffEntity> sListDeviceOnOff) {
        this.mContext = context;
        if (context instanceof CallbackDeviceOnOffAdapter) {
            sListener = (CallbackDeviceOnOffAdapter) context;
        } else throw new ClassCastException("class must be implement CallBackDeviceApdater!");

        if (sListDeviceOnOff == null)
            return;
        this.sListDeviceOnOff = new ArrayList<>();
        this.sListDeviceOnOff.addAll(sListDeviceOnOff);
    }

    @Override
    public ConnectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_device_on_off, parent, false);
        ConnectedViewHolder viewHolder = new ConnectedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConnectedViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.row_device_on_off, null);
            holder = new ConnectedViewHolder(view);
        }

        holder.getTvName().setText(sListDeviceOnOff.get(position).getName());
        holder.getTvIP().setText(sListDeviceOnOff.get(position).getIpAddress());
        boolean statusOnOff = sListDeviceOnOff.get(position).isStatus();
        //true = device Connect, false = device Disconnect
        if (statusOnOff) {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
        } else {
            holder.getIvOnOff().setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
        }
    }

    @Override
    public int getItemCount() {
        return sListDeviceOnOff.size();
    }

    public static class ConnectedViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName, tvIP;
        public final ImageView ivOnOff;

        public ConnectedViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_row_connected_device_name);
            tvIP = (TextView) itemView.findViewById(R.id.tv_row_connected_device_ip);
            ivOnOff = (ImageView) itemView.findViewById(R.id.iv_row_connected_device_name_on_off);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sListener.clickRowDeviceOnOff(sListDeviceOnOff.get(getAdapterPosition()));
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

    public void refreshAdapter(List<DeviceOnOffEntity> entityList) {
        if (entityList == null)
            return;
        sListDeviceOnOff = entityList;
        notifyDataSetChanged();
    }

    public interface CallbackDeviceOnOffAdapter {
        void clickRowDeviceOnOff(DeviceOnOffEntity deviceOnOffEntity);
    }
}
