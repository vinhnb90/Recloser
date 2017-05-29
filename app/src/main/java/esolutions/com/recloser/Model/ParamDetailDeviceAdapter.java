package esolutions.com.recloser.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DetailDeviceEntity;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/17/2017.
 */

public class ParamDetailDeviceAdapter extends RecyclerView.Adapter<ParamDetailDeviceAdapter.ParamViewHolder> {
    private List<DetailDeviceEntity.ParamDetailDevice> paramDetailDeviceList;

    public ParamDetailDeviceAdapter(List<DetailDeviceEntity.ParamDetailDevice> paramDetailDeviceList) {
        this.paramDetailDeviceList = new ArrayList<>();

        if (paramDetailDeviceList == null)
            return;
        this.paramDetailDeviceList = new ArrayList<>();
        this.paramDetailDeviceList.addAll(paramDetailDeviceList);
    }


    @Override
    public ParamDetailDeviceAdapter.ParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.row_param_detail_device_recycler, parent, false);
        ParamViewHolder viewHolder = new ParamViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParamViewHolder holder, int position) {
        String paramName = paramDetailDeviceList.get(position).getDescriptionData();
        double paramValue = paramDetailDeviceList.get(position).getValueData();
        String paramUnit = paramDetailDeviceList.get(position).getUnitData();

        holder.mTvParamName.setText(paramName);
        holder.mTvParamValue.setText(String.valueOf(paramValue));
        holder.mTvParamUnit.setText(paramUnit);
    }

    @Override
    public int getItemCount() {
        return paramDetailDeviceList.size();
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


    public void refresh(List<DetailDeviceEntity.ParamDetailDevice> paramDetailDeviceList) {
        if (paramDetailDeviceList != null) {
            this.paramDetailDeviceList = paramDetailDeviceList;
            this.paramDetailDeviceList.addAll(paramDetailDeviceList);
        }
        notifyDataSetChanged();
    }
}
