package esolutions.com.recloser.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DetailDeviceEntity;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/17/2017.
 */

public class EventDetailDeviceAdapter extends RecyclerView.Adapter<EventDetailDeviceAdapter.EventViewHolder> {
    private List<ObjectEventDevice> objectEventDevices;

    public EventDetailDeviceAdapter(List<ObjectEventDevice> objectEventDevices) {
        this.objectEventDevices = new ArrayList<>();

        if (objectEventDevices == null)
            return;
        this.objectEventDevices = new ArrayList<>();
        this.objectEventDevices.addAll(objectEventDevices);
    }


    @Override
    public EventDetailDeviceAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.row_event_detail_device_recycler, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        String eventName = objectEventDevices.get(position).getDescriptionEvent();
        boolean eventStatus = objectEventDevices.get(position).getValueEvent();

        holder.mTvEventName.setText(eventName);
        if (eventStatus)
            holder.mIvEventStatus.setImageResource(R.drawable.ring_row_info_dashboard_status_turn_on);
        else
            holder.mIvEventStatus.setImageResource(R.drawable.ring_row_info_dashboard_status_turn_off);
    }

    @Override
    public int getItemCount() {
        return objectEventDevices.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvEventName;
        public ImageView mIvEventStatus;

        public EventViewHolder(View itemView) {
            super(itemView);
            mTvEventName = (TextView) itemView.findViewById(R.id.tv_row_event_detail_device_recycler_name);
            mIvEventStatus = (ImageView) itemView.findViewById(R.id.iv_row_event_detail_device_recycler_status);
        }
    }


    public void refresh(List<ObjectEventDevice> objectEventDevices) {
        if (objectEventDevices != null) {
            this.objectEventDevices = objectEventDevices;
//            this.objectEventDevices.addAll(objectEventDevices);
        }
        notifyDataSetChanged();
    }
}
