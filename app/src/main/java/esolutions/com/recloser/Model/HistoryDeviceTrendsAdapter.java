package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 3/11/2017.
 */

public class HistoryDeviceTrendsAdapter extends RecyclerView.Adapter<HistoryDeviceTrendsAdapter.HistoryTrendsViewHolder> {
    private Context mContext;
    private static List<ParamHistoryEntity> paramHistoryEntities;
//    private static CallBackHistoryDeviceTrendsApdater sListener;

    public HistoryDeviceTrendsAdapter(Context context, List<ParamHistoryEntity> paramHistoryEntities) {
        this.mContext = context;
//        if (context instanceof CallBackHistoryDeviceTrendsApdater) {
//            sListener = (CallBackHistoryDeviceTrendsApdater) context;
//        } else
//            throw new ClassCastException("class must be implement CallBackHistoryDeviceTrendsApdater!");
        if (paramHistoryEntities == null)
            return;
        this.paramHistoryEntities = new ArrayList<>();
        this.paramHistoryEntities.addAll(paramHistoryEntities);
    }

    @Override
    public HistoryTrendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_history_trends, parent, false);
        HistoryTrendsViewHolder viewHolder = new HistoryTrendsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryTrendsViewHolder holder, int position) {
        if (holder == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.row_history_trends, null);
            holder = new HistoryTrendsViewHolder(view);
        }

        holder.tvDescription.setText(paramHistoryEntities.get(position).getDescription());
        holder.tvValue.setText(paramHistoryEntities.get(position).getValue());
        holder.tvUnit.setText(paramHistoryEntities.get(position).getUnit());
        String time = paramHistoryEntities.get(position).getTimestamp();
        time = CommonMethod.convertDateToDate(time, Define.TYPE_DATE_TIME_FULL, Define.TYPE_DATE_TIME_FULL_TYPE_2);
        holder.tvTimeStamp.setText(time);
    }

    @Override
    public int getItemCount() {
        return paramHistoryEntities.size();
    }

    public static class HistoryTrendsViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvDescription, tvValue, tvUnit, tvTimeStamp;

        public HistoryTrendsViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_row_history_trends_unit);
            tvValue = (TextView) itemView.findViewById(R.id.tv_row_history_trends_value);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_row_history_trends_unit);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tv_row_history_trends_time_stamp);
        }
    }

    public void refreshAdapter(List<ParamHistoryEntity> entityList) {
        if (entityList == null)
            return;
        paramHistoryEntities = entityList;
        notifyDataSetChanged();
    }

//    public interface CallBackHistoryDeviceTrendsApdater {
//        void clickRowHistoryDeviceTrends(ParamHistoryEntity deviceEntity);
//    }
}
