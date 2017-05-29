package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 3/16/2017.
 */

public class SpinnerDetailDeviceAdapter extends ArrayAdapter<ObjectSpinnerParamEntity> {
    private Context mContext;
    private static List<ObjectSpinnerParamEntity> sListData;
    private static int sIdDevice;

    private static CallBackSpinnerDetailDeviceAdapter sCallBack;

    public SpinnerDetailDeviceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ObjectSpinnerParamEntity> objects, @NonNull int idDevice) {
        super(context, resource, objects);
        if (idDevice <= 0)
            return;
        if (objects == null)
            return;
        if (context instanceof CallBackSpinnerDetailDeviceAdapter)
            sCallBack = (CallBackSpinnerDetailDeviceAdapter) context;
        else
            throw new ClassCastException("Class must implement CallBackSpinnerDetailDeviceAdapter!");

        sListData = initPromtSpiner(objects);
        mContext = context;
        sIdDevice = idDevice;

    }

    public void setPrompt(String promt){
        if(promt == null || promt.isEmpty())
            return;
        sListData.get(0).setParamDescription(promt);
        notifyDataSetChanged();
    }

    private List<ObjectSpinnerParamEntity> initPromtSpiner(List<ObjectSpinnerParamEntity> objects) {
        List<ObjectSpinnerParamEntity> list = new ArrayList<>();
        ObjectSpinnerParamEntity itemDefault = new ObjectSpinnerParamEntity();
        itemDefault.setTagNameOPCData(null);
        itemDefault.setParamDescription(Define.PROMT_SPINER_PARAM_OPEN);
        itemDefault.setmSelected(false);
        list.add(itemDefault);
        list.addAll(objects);
        return list;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        SpinnerViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.row_spinner_detail_device, null);
            holder = new SpinnerViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SpinnerViewHolder) convertView.getTag();
        }
        holder.mText.setText(sListData.get(position).getParamDescription());

        if ((position == 0)) {
            holder.mCb.setVisibility(View.GONE);
        } else {
            holder.mCb.setVisibility(View.VISIBLE);
        }
        holder.mCb.setTag(position);
        // To check weather checked event fire from getview() or user input
        holder.mCb.setChecked(sListData.get(position).ismSelected());

        convertView.setTag(holder);
        return convertView;
    }

    public void refresh(List<ObjectSpinnerParamEntity> mParamSpinList) {
        if (mParamSpinList == null)
            return;
        sListData = initPromtSpiner(mParamSpinList);
        notifyDataSetChanged();
    }

    private static class SpinnerViewHolder {
        private TextView mText;
        private CheckBox mCb;

        public SpinnerViewHolder(View itemView) {
            mText = (TextView) itemView.findViewById(R.id.tv_row_spinner_detail_device);
            mCb = (CheckBox) itemView.findViewById(R.id.cb_row_spinner_detail_device);


            mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    if (getPosition == 0)
                        return;

                    if (buttonView.isPressed()) {
                        sListData.get(getPosition).setmSelected(isChecked);
                        sCallBack.refreshLineChart(sIdDevice, sListData, getPosition, isChecked);
                    }
                }
            });
        }
    }

    public interface CallBackSpinnerDetailDeviceAdapter {
        void refreshLineChart(int idDevice, List<ObjectSpinnerParamEntity> entityList, int posLineChart, boolean isShow);
    }
}
