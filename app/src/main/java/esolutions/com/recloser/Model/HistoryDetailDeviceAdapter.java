package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Entity.ObjectHistoryDetailDeviceInside;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/11/2017.
 */

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by VinhNB on 3/11/2017.
 */

public class HistoryDetailDeviceAdapter extends RecyclerView.Adapter<HistoryDetailDeviceAdapter.HistoryDetailDeviceViewHolder> {

    private static Context sContext;
    private static List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs;
    private static List<ObjectHistoryDetailDeviceInside> insideList;
    private static List<String> timeStampList;
    private static RecyclerView sRvInside;

    private HistoryDetailDeviceAdapter historyAndAlarmAdapter;


    public HistoryDetailDeviceAdapter(Context context, List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs) {
        this.sContext = context;
        if (historyDetailDeviceJSONs == null || historyDetailDeviceJSONs.size() == 0)
            return;

        this.historyDetailDeviceJSONs = new ArrayList<>();
        this.historyDetailDeviceJSONs.addAll(historyDetailDeviceJSONs);

        //sap xep list theo timestamp
        Collections.sort(this.historyDetailDeviceJSONs, new HistoryDetailDeviceJSON.ComparHistoryDetailDeviceJSONByTimeStamp());

        //lọc các time stamp
        timeStampList = new ArrayList<>();
        String timeStamp = this.historyDetailDeviceJSONs.get(0).getTimestamp();
        timeStampList.add(timeStamp);
        int index = 1;
        for (; index < this.historyDetailDeviceJSONs.size(); index++) {
            if (!timeStamp.equals(this.historyDetailDeviceJSONs.get(index).getTimestamp())) {
                timeStamp = this.historyDetailDeviceJSONs.get(index).getTimestamp();
                timeStampList.add(timeStamp);
            }
        }
    }

    @Override
    public HistoryDetailDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(sContext).inflate(R.layout.row_history_detail_device, parent, false);
        return new HistoryDetailDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryDetailDeviceViewHolder holder, final int position) {
        String time = timeStampList.get(position);

        //loc list
        insideList = new ArrayList<>();
        for (HistoryDetailDeviceJSON element :
                historyDetailDeviceJSONs) {
            if (element.getTimestamp().equals(time)) {
                ObjectHistoryDetailDeviceInside inside = new ObjectHistoryDetailDeviceInside();
                inside.setTagNameESLink(element.getTagNameESLink());
                inside.setDescription(element.getDescription());
                inside.setValue(element.getValue());
                inside.setUnit(element.getUnit());

                insideList.add(inside);
            }
        }

        if (insideList.size() > 1) {
            Log.d(Define.TAG, "onBindViewHolder: ");
        }
        if (holder.insideAdapter == null) {
            holder.insideAdapter = new HistoryDetailDeviceInsideApdater();
        }
        holder.insideAdapter.setData(insideList); // List of Strings
        holder.insideAdapter.setRowIndex(position);
        time = CommonMethod.convertDateToDate(time, Define.TYPE_DATE_TIME_FULL, Define.TYPE_DATE_TIME_FULL_TYPE_2);
        holder.tvTimeStamp.setText(time);

    }

    @Override
    public int getItemCount() {
        return timeStampList.size();
    }

    public static class HistoryDetailDeviceViewHolder extends ViewHolder {
        public final TextView tvTimeStamp;
        private HistoryDetailDeviceInsideApdater insideAdapter;

        public HistoryDetailDeviceViewHolder(View itemView) {
            super(itemView);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tv_row_history_detail_device_time_stamp);
            sRvInside = (RecyclerView) itemView.findViewById(R.id.rv_row_history_detail_device_inside);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false);
            sRvInside.setLayoutManager(linearLayoutManager);
            insideAdapter = new HistoryDetailDeviceInsideApdater();
            sRvInside.setAdapter(insideAdapter);
        }
    }

    public static class HistoryDetailDeviceInsideApdater extends Adapter<HistoryDetailDeviceInsideViewHolder> {
        private static List<ObjectHistoryDetailDeviceInside> insideList;

        private static int mRowIndex = -1;

        public HistoryDetailDeviceInsideApdater() {
            insideList = new ArrayList<>();
        }

        public void setRowIndex(int index) {
            mRowIndex = index;
        }

        public void setData(List<ObjectHistoryDetailDeviceInside> data) {
            if (data == null)
                return;
            insideList = new ArrayList<>();
            insideList.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public HistoryDetailDeviceInsideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_detail_device_inside, parent, false);
            HistoryDetailDeviceInsideViewHolder holder = new HistoryDetailDeviceInsideViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(HistoryDetailDeviceInsideViewHolder holder, int position) {
            holder.tvParamInside.setText(insideList.get(position).getDescription());
            //nếu là param
            String value = insideList.get(position).getValue();
            if (value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false))) {
                holder.tvValueInside.setVisibility(View.GONE);
                holder.ivIconOnOffInside.setVisibility(View.VISIBLE);
                if (value.equalsIgnoreCase(String.valueOf(true))) {
                    holder.ivIconOnOffInside.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
                } else
                    holder.ivIconOnOffInside.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
            } else {
                holder.tvValueInside.setVisibility(View.VISIBLE);
                holder.ivIconOnOffInside.setVisibility(View.GONE);
                holder.tvValueInside.setText(value + Define.SYMBOL_PARAM_SPACE + insideList.get(position).getUnit());
            }
        }

        @Override
        public int getItemCount() {
            return insideList.size();
        }
    }

    public static class HistoryDetailDeviceInsideViewHolder extends ViewHolder {
        public final TextView tvParamInside, tvValueInside;
        public final ImageView ivIconOnOffInside;

        public HistoryDetailDeviceInsideViewHolder(View itemView) {
            super(itemView);
            tvParamInside = (TextView) itemView.findViewById(R.id.tv_row_history_detail_device_inside_param_name);
            tvValueInside = (TextView) itemView.findViewById(R.id.tv_row_history_detail_device_inside_param_value);
            ivIconOnOffInside = (ImageView) itemView.findViewById(R.id.iv_row_history_detail_device_inside_param_icon_event);
        }
    }

    public static class CustomLinearLayoutManager extends LinearLayoutManager {

        private static final String TAG = CustomLinearLayoutManager.class.getSimpleName();

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            this.setAutoMeasureEnabled(false);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);


                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            try {
                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                            getPaddingLeft() + getPaddingRight(), p.width);

                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                            getPaddingTop() + getPaddingBottom(), p.height);

                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}


