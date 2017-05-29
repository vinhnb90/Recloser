package esolutions.com.recloser.Model.Inteface;

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
import esolutions.com.recloser.Entity.ObjectHistoryAndAlarmInside;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by VinhNB on 3/11/2017.
 */

public class HistoryAndAlarmAdapter extends RecyclerView.Adapter<HistoryAndAlarmAdapter.HistoryAndAlarmViewHolder> {
    private static Context sContext;
    private static List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONsList;
    private static List<String> timeStampList;
    private static RecyclerView sRvInside;
    private static String typeString;
//    private static CallBackHistoryDeviceTrendsApdater sListener;

    public HistoryAndAlarmAdapter(Context context, List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONsList, String typeString) {
        this.sContext = context;
//        if (context instanceof CallBackHistoryDeviceTrendsApdater) {
//            sListener = (CallBackHistoryDeviceTrendsApdater) context;
//        } else
//            throw new ClassCastException("class must be implement CallBackHistoryDeviceTrendsApdater!");

        if (typeString == null || typeString.isEmpty())
            return;

        if (historyAndAlarmEventJSONsList == null || historyAndAlarmEventJSONsList.size() == 0)
            return;

        this.historyAndAlarmEventJSONsList = new ArrayList<>();
        this.historyAndAlarmEventJSONsList.addAll(historyAndAlarmEventJSONsList);

        //sap xep list theo timestamp
        Collections.sort(this.historyAndAlarmEventJSONsList, new HistoryAndAlarmEventJSON.ComparHistoryAndAlarmEventJSONByTimeStamp());

        //lọc các time stamp
        timeStampList = new ArrayList<>();
        String timeStamp = this.historyAndAlarmEventJSONsList.get(0).getTimestamp();
        timeStampList.add(timeStamp);
        int index = 1;
        for (; index < this.historyAndAlarmEventJSONsList.size(); index++) {
            if (!timeStamp.equals(this.historyAndAlarmEventJSONsList.get(index).getTimestamp())) {
                timeStamp = this.historyAndAlarmEventJSONsList.get(index).getTimestamp();
                timeStampList.add(timeStamp);
            }
        }
        this.typeString = typeString;
    }

    @Override
    public HistoryAndAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(sContext);
        View view = layoutInflater.inflate(R.layout.row_history_and_alarm, parent, false);
        HistoryAndAlarmViewHolder viewHolder = new HistoryAndAlarmViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAndAlarmViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(sContext);
            View view = layoutInflater.inflate(R.layout.row_history_and_alarm, null);
            holder = new HistoryAndAlarmViewHolder(view);
        }

        String time = timeStampList.get(position);
        //loc list
        List<ObjectHistoryAndAlarmInside> insideList = new ArrayList<>();
        for (HistoryAndAlarmEventJSON element :
                historyAndAlarmEventJSONsList) {
            if (element.getTimestamp().equals(time)) {
                ObjectHistoryAndAlarmInside inside = new ObjectHistoryAndAlarmInside();
                inside.setParamName(element.getDescriptionData());
                inside.setValue(element.getValue());
                inside.setInfo(element.getDescriptionInfo());
                insideList.add(inside);
            }
        }
        holder.insideAdapter.setData(insideList);
        sRvInside.setAdapter(holder.insideAdapter);

        time = CommonMethod.convertDateToDate(time, Define.TYPE_DATE_TIME_FULL, Define.TYPE_DATE_TIME_FULL_TYPE_2);
        holder.tvTimeStamp.setText(time);
    }

    @Override
    public int getItemCount() {
        return timeStampList.size();
    }

    public static class HisoryAndAlarmInsideAdapter extends Adapter<HisoryAndAlarmInsideHolder> {
        private static List<ObjectHistoryAndAlarmInside> insideList;

        public HisoryAndAlarmInsideAdapter() {
            this.insideList = new ArrayList<>();
        }

        public void setData(List<ObjectHistoryAndAlarmInside> insideList) {
            this.insideList = insideList;
        }

        @Override
        public HisoryAndAlarmInsideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_and_alarm_inside, parent, false);
            HisoryAndAlarmInsideHolder insideHolder = new HisoryAndAlarmInsideHolder(view);
            return insideHolder;
        }

        @Override
        public void onBindViewHolder(HisoryAndAlarmInsideHolder holder,final int position) {
            if (holder == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(sContext);
                View view = layoutInflater.inflate(R.layout.row_history_and_alarm_inside, null);
                holder = new HisoryAndAlarmInsideHolder(view);
            }
            holder.tvParamInside.setText(insideList.get(position).getParamName());
            //nếu là param
            if (typeString.equals(Define.TYPE_SPINNER_HISTORY[1])) {
                holder.tvValueInside.setVisibility(View.VISIBLE);
                holder.ivIconOnOff.setVisibility(View.GONE);
                holder.tvValueInside.setText(insideList.get(position).getValue());
            } else {
                holder.tvValueInside.setVisibility(View.GONE);
                holder.ivIconOnOff.setVisibility(View.VISIBLE);
                if (insideList.get(position).getValue().equalsIgnoreCase(String.valueOf(true))) {
                    holder.ivIconOnOff.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
                } else
                    holder.ivIconOnOff.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
            }

            holder.tvInfo.setText(insideList.get(position).getInfo());
        }

        @Override
        public int getItemCount() {
            return insideList.size();
        }
    }

    public static class HistoryAndAlarmViewHolder extends ViewHolder {
        public final TextView tvTimeStamp;
        private HisoryAndAlarmInsideAdapter insideAdapter;

        public HistoryAndAlarmViewHolder(View itemView) {
            super(itemView);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tv_row_history_and_alarm_time_stamp);
            sRvInside = (RecyclerView) itemView.findViewById(R.id.rv_row_history_and_alarm_inside);
            sRvInside.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
            insideAdapter = new HisoryAndAlarmInsideAdapter();
            sRvInside.setAdapter(insideAdapter);
        }
    }


    public static class HisoryAndAlarmInsideHolder extends ViewHolder {
        public HisoryAndAlarmInsideHolder(View itemView) {
            super(itemView);
            tvParamInside = (TextView) itemView.findViewById(R.id.tv_row_history_and_alarm_param_name);
            tvValueInside = (TextView) itemView.findViewById(R.id.tv_row_history_and_alarm_param_value);
            tvInfo = (TextView) itemView.findViewById(R.id.tv_row_history_and_alarm_param_info);
            ivIconOnOff = (ImageView) itemView.findViewById(R.id.iv_row_history_and_alarm_param_icon_event);
        }

        public final TextView tvParamInside, tvValueInside, tvInfo;
        public final ImageView ivIconOnOff;
    }


}
