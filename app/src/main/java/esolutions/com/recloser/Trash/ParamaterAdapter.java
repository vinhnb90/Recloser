package esolutions.com.recloser.Trash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class ParamaterAdapter extends RecyclerView.Adapter<ParamaterAdapter.ParamaterViewHolder> {
    private List<ObjectParamaterDeviceJSON> mObjectParamaterList;
    //mDeviceIndex : thứ tự device
    private int mDeviceIndex = -1;

    public ParamaterAdapter() {
        mObjectParamaterList = new ArrayList<>();
    }

    public void setmParamaterList(List<ObjectParamaterDeviceJSON> mObjectParamaterList) {
        if (mObjectParamaterList != null)
            this.mObjectParamaterList = mObjectParamaterList;
        notifyDataSetChanged();
    }

    public void setDeviceIndex(int index) {
        mDeviceIndex = index;
    }

    @Override
    public ParamaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_item_paramater_all_device, parent, false);
        ParamaterViewHolder holder = new ParamaterViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ParamaterViewHolder viewHolder, int position) {
        String nameParam = mObjectParamaterList.get(position).getTagNameOPCData();
//        Double valueParam = mObjectParamaterList.get(position).getValue();
        String unitParam = mObjectParamaterList.get(position).getUnit();
//        Boolean isStatus = mObjectParamaterList.get(position).isStatus();
//        Boolean isStatusOnOff = mObjectParamaterList.get(position).isStatusOnOff();

//        viewHolder.tvDescription.setText(nameParam);
//        if (isStatus) {
//            viewHolder.llStatus.setVisibility(View.VISIBLE);
//            viewHolder.llValueAndUnit.setVisibility(View.GONE);
//            if (isStatusOnOff) {
//                viewHolder.ivStatusOnOff.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_on);
//                viewHolder.tvStatusOnOff.setText(R.string.ON);
//            } else {
//                viewHolder.ivStatusOnOff.setBackgroundResource(R.drawable.ring_row_info_dashboard_status_turn_off);
//                viewHolder.tvStatusOnOff.setText(R.string.OFF);
//            }
//        } else {
//            viewHolder.llStatus.setVisibility(View.GONE);
//            viewHolder.llValueAndUnit.setVisibility(View.VISIBLE);
//            viewHolder.tvValue.setText(String.valueOf(valueParam));
//            viewHolder.tvUnit.setText((unitParam));
//        }
//        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mObjectParamaterList.size();
    }

    public static class ParamaterViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvValue, tvUnit, tvStatusOnOff;
        private final LinearLayout llValueAndUnit, llStatus;
        private final ImageView ivStatusOnOff;

        public ParamaterViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_row_item_paramater_all_device_name);
            tvValue = (TextView) itemView.findViewById(R.id.tv_row_item_paramater_all_device_value);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_row_item_paramater_all_device_unit);
            tvStatusOnOff = (TextView) itemView.findViewById(R.id.tv_row_item_paramater_all_device_status_on_off);

            llValueAndUnit = (LinearLayout) itemView.findViewById(R.id.ll_row_item_paramater_all_device_value_unit);
            llStatus = (LinearLayout) itemView.findViewById(R.id.ll_row_item_paramater_all_device_status);
            ivStatusOnOff = (ImageView) itemView.findViewById(R.id.iv_row_item_paramater_all_device_statusOnOff);
        }

    }

}
