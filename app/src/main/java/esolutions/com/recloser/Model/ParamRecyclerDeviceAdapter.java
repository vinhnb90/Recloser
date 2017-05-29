package esolutions.com.recloser.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/17/2017.
 */

public class ParamRecyclerDeviceAdapter extends RecyclerView.Adapter<ParamRecyclerDeviceAdapter.ParamViewHolder> {
    private List<ObjectParamaterDeviceJSON> objectParamaters;

    public ParamRecyclerDeviceAdapter(List<ObjectParamaterDeviceJSON> objectParamaters) {
        this.objectParamaters = new ArrayList<>();
        if (objectParamaters == null)
            return;
        this.objectParamaters = new ArrayList<>();
        this.objectParamaters.addAll(objectParamaters);
    }

    @Override
    public ParamRecyclerDeviceAdapter.ParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.row_param_detail_device_recycler, parent, false);
        ParamViewHolder viewHolder = new ParamViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParamViewHolder holder, int position) {
        String paramName = objectParamaters.get(position).getDescriptionData();
        String paramValue = objectParamaters.get(position).getValueData();
        String paramUnit = objectParamaters.get(position).getUnit();

        holder.mTvParamName.setText(paramName);
        holder.mTvParamValue.setText(String.valueOf(paramValue));
        holder.mTvParamUnit.setText(paramUnit);
    }

    @Override
    public int getItemCount() {
        return objectParamaters.size();
    }

    public class ParamViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvParamName;
        public TextView mTvParamValue;
        public TextView mTvParamUnit;

        public ParamViewHolder(View itemView) {
            super(itemView);
            mTvParamName = (TextView) itemView.findViewById(R.id.tv_row_param_detail_device_recycler_name);
            mTvParamValue = (TextView) itemView.findViewById(R.id.tv_row_param_detail_device_recycler_value);
            mTvParamUnit = (TextView) itemView.findViewById(R.id.tv_row_param_detail_device_recycler_unit);
        }
    }


    public void refresh(List<ObjectParamaterDeviceJSON> objectParamaters) {
        if (objectParamaters != null) {
            this.objectParamaters = objectParamaters;
//            this.objectParamaters.addAll(objectParamaters);
        }
        notifyDataSetChanged();
    }
}
