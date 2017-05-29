package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Entity.ObjectHistoryAndAlarmInside;
import esolutions.com.recloser.Entity.ObjectHistoryAndAlarmTodayInside;
import esolutions.com.recloser.Model.Inteface.HistoryAndAlarmAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/11/2017.
 */

public class EventAndAlarmTodayAdapter extends RecyclerView.Adapter<EventAndAlarmTodayAdapter.EventAndAlarmTodayAdapterHolder> {
    private List<HistoryAndAlarmEventJSONToday> todayList;
    private static List<String> timeStampList;

    private static RecyclerView rvEventAlarmTodayInside;

    private Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString;
    private static Context sContext;

    public EventAndAlarmTodayAdapter(Context context, List<HistoryAndAlarmEventJSONToday> todayList, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (context == null)
            return;
        if (typeString == null)
            return;
        if (todayList == null)
            return;

        this.sContext = context;
        this.todayList = new ArrayList<>();
        this.todayList.addAll(todayList);
        this.typeString = typeString;

        //sap xep list theo timestamp
        Collections.sort(this.todayList, new HistoryAndAlarmEventJSONToday.ComparHistoryAndAlarmEventJSONTodayByTimeStamp());

        //lọc các time stamp
        timeStampList = new ArrayList<>();
        String timeStamp = this.todayList.get(0).getTimestamp();
        timeStampList.add(timeStamp);
        int index = 1;
        for (; index < this.todayList.size(); index++) {
            if (!timeStamp.equals(this.todayList.get(index).getTimestamp())) {
                timeStamp = this.todayList.get(index).getTimestamp();
                timeStampList.add(timeStamp);
            }
        }
    }

    @Override
    public EventAndAlarmTodayAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(sContext).inflate(R.layout.row_event_and_alarm_today, parent, false);
        EventAndAlarmTodayAdapterHolder holder = new EventAndAlarmTodayAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventAndAlarmTodayAdapterHolder holder, int position) {
        if (holder == null) {
            View view = LayoutInflater.from(sContext).inflate(R.layout.row_event_and_alarm_today, null, false);
            holder = new EventAndAlarmTodayAdapterHolder(view);
        }
        String time = timeStampList.get(position);

        //loc list theo timeStamp
        List<ObjectHistoryAndAlarmTodayInside> insideList = new ArrayList<>();
        for (HistoryAndAlarmEventJSONToday element :
                todayList) {
            if (element.getTimestamp().equals(time)) {
                ObjectHistoryAndAlarmTodayInside inside = new ObjectHistoryAndAlarmTodayInside();
                inside.setDeviceName(element.getName());
                inside.setEvents(element.getEvents());
                inside.setDescription(element.getDescription());
                inside.setValue(element.getValue());
                insideList.add(inside);
            }
        }

        //sắp xếp theo device Name
        Collections.sort(insideList, new ObjectHistoryAndAlarmTodayInside.ComparHistoryAndAlarmEventJSONTodayByDeviceName());


        //loc list theo device Name va add lan luot vao list object
        ArrayList<Object> itemsAdapter = new ArrayList<>();
        String deviceName = insideList.get(0).getDeviceName();
        itemsAdapter.add(deviceName);
        int index = 1;
        for (; index < insideList.size(); index++) {
            if (deviceName.equals(insideList.get(index).getDeviceName())) {
                itemsAdapter.add(insideList.get(index));
            } else {
                deviceName = insideList.get(index).getDeviceName();
                itemsAdapter.add(deviceName);
                itemsAdapter.add(insideList.get(index));
            }
        }

        holder.insideTodayAdapter = new HisoryAndAlarmInsideTodayAdapter(itemsAdapter);
        rvEventAlarmTodayInside.setAdapter(holder.insideTodayAdapter);

        time = CommonMethod.convertDateToDate(time, Define.TYPE_DATE_TIME_FULL, Define.TYPE_DATE_TIME_FULL_TYPE_2);
        holder.tvTimestamp.setText(time);
    }

    @Override
    public int getItemCount() {
        return timeStampList.size();
    }

    public static class EventAndAlarmTodayAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvTimestamp;
        private HisoryAndAlarmInsideTodayAdapter insideTodayAdapter;

        public EventAndAlarmTodayAdapterHolder(View itemView) {
            super(itemView);

            tvTimestamp = (TextView) itemView.findViewById(R.id.tv_row_history_and_alarm_today_time_stamp);
            rvEventAlarmTodayInside = (RecyclerView) itemView.findViewById(R.id.rv_row_history_and_alarm_today_inside);
            rvEventAlarmTodayInside.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
        }
    }

    private static class HisoryAndAlarmInsideTodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        //        private List<ObjectHistoryAndAlarmTodayInside> insideList;
        private List<Object> objectList;
        private final int DEVICE_NAME = 0, DEVICE_EVENT = 1;

        public HisoryAndAlarmInsideTodayAdapter(List<Object> objectList) {
            if (objectList == null)
                return;
            this.objectList = new ArrayList<>();
            this.objectList.addAll(objectList);
        }

        //        public HisoryAndAlarmInsideTodayAdapter(List<ObjectHistoryAndAlarmTodayInside> insideList) {
//            if (insideList == null)
//                return;
//            this.insideList = new ArrayList<>();
//            this.insideList.addAll(insideList);
//        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HisoryAndAlarmInsideTodayAdapterViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case DEVICE_NAME:
                    View v1 = inflater.inflate(R.layout.row_event_and_alarm_today_header_name_device, parent, false);
                    viewHolder = new ViewHolderHeaderDeviceName(v1);
                    break;
                case DEVICE_EVENT:
                    View v2 = inflater.inflate(R.layout.row_event_and_alarm_today_inside, parent, false);
                    viewHolder = new ViewHolderEvent(v2);
                    break;
                default:
                    viewHolder = null;
                    break;
            }
            return viewHolder;

//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_and_alarm_today_inside, parent, false);
//
//            HisoryAndAlarmInsideTodayAdapterViewHolder holder = new HisoryAndAlarmInsideTodayAdapterViewHolder(view);
//            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case DEVICE_NAME:
                    ViewHolderHeaderDeviceName holderHeaderDeviceName = (ViewHolderHeaderDeviceName) holder;
                    String deviceName = String.valueOf(objectList.get(position));
                    holderHeaderDeviceName.tvHeaderDeviceName.setText(deviceName);
                    break;

                case DEVICE_EVENT:
                    ViewHolderEvent holderEvent = (ViewHolderEvent) holder;
                    ObjectHistoryAndAlarmInside objectHistoryAndAlarmInside = (ObjectHistoryAndAlarmInside) objectList.get(position);

                    holderEvent.tvEventInside.setText(objectHistoryAndAlarmInside.getParamName());
                    //nếu là param
                    String value = objectHistoryAndAlarmInside.getValue();
                    if (value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false))) {
                        holderEvent.tvValueInside.setVisibility(View.GONE);
                        holderEvent.ivIconOnOffInside.setVisibility(View.VISIBLE);
                        if (value.equalsIgnoreCase(String.valueOf(true))) {
                            ((ViewHolderEvent) holder).ivIconOnOffInside.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
                        } else
                            ((ViewHolderEvent) holder).ivIconOnOffInside.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
                    } else {
                        holderEvent.tvValueInside.setVisibility(View.VISIBLE);
                        holderEvent.ivIconOnOffInside.setVisibility(View.GONE);
                        holderEvent.tvValueInside.setText(value);
                    }

                    ((ViewHolderEvent) holder).tvInfoInside.setText(objectHistoryAndAlarmInside.getInfo());
                    break;
            }

//            if (holder == null) {
//                View view = LayoutInflater.from(sContext).inflate(R.layout.row_event_and_alarm_today_inside, null, false);
//                holder = new HisoryAndAlarmInsideTodayAdapterViewHolder(view);
//            }
        }

        @Override
        public int getItemViewType(int position) {
            if (objectList.get(position) instanceof String) {
                return DEVICE_NAME;
            } else if (objectList.get(position) instanceof ObjectHistoryAndAlarmInside) {
                return DEVICE_EVENT;
            }
            return -1;
        }

        @Override
        public int getItemCount() {
            return this.objectList.size();
        }

        private class ViewHolderHeaderDeviceName extends HisoryAndAlarmInsideTodayAdapterViewHolder {
            public TextView tvHeaderDeviceName;

            public ViewHolderHeaderDeviceName(View v1) {
                super(v1);
                tvHeaderDeviceName = (TextView) v1.findViewById(R.id.tv_row_event_and_alarm_today_header_name_device_name);
            }
        }

        private class ViewHolderEvent extends HisoryAndAlarmInsideTodayAdapterViewHolder {
            public final TextView tvEventInside, tvValueInside, tvInfoInside;
            public final ImageView ivIconOnOffInside;

            public ViewHolderEvent(View v2) {
                super(v2);
                tvEventInside = (TextView) itemView.findViewById(R.id.tv_row_event_and_alarm_today_inside_event_name);
                tvValueInside = (TextView) itemView.findViewById(R.id.tv_row_event_and_alarm_today_inside_value);
                ivIconOnOffInside = (ImageView) itemView.findViewById(R.id.iv_row_event_and_alarm_today_inside_value);
                tvInfoInside = (TextView) itemView.findViewById(R.id.tv_row_event_and_alarm_today_inside_info);
            }

        }
    }


    private static class HisoryAndAlarmInsideTodayAdapterViewHolder extends RecyclerView.ViewHolder {
        public HisoryAndAlarmInsideTodayAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
