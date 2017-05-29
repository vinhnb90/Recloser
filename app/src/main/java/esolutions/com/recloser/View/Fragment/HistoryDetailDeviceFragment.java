package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Model.HistoryDetailDeviceAdapter;
import esolutions.com.recloser.Model.Inteface.HistoryAndAlarmAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;

import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_DD_MM_YYYY;

public class HistoryDetailDeviceFragment extends BaseV4Fragment implements View.OnClickListener {
    private TextView tvDateBegin, tvDateEnd;
    private RecyclerView rvHistory;
//    private Button btnLoadData;

    private static final String ARG_DEVICE_ID = "ARG_DEVICE_ID";
    private int mIdDevice;
    private String dateBegin, dateEnd;
    private OnHistoryDetailDeviceListener mListener;
    private static int sIdTextViewChooseDate;
    private List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs;
    private HistoryDetailDeviceAdapter historyDetailDeviceAdapter;

    public HistoryDetailDeviceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryDetailDeviceFragment newInstance(int idDevice) {
        HistoryDetailDeviceFragment fragment = new HistoryDetailDeviceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DEVICE_ID, idDevice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdDevice = getArguments().getInt(ARG_DEVICE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_detail_device, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryDetailDeviceListener) {
            mListener = (OnHistoryDetailDeviceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHistoryDetailDeviceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(View view) {
        tvDateBegin = (TextView) view.findViewById(R.id.tv_fragment_history_detail_device_date_begin);
        tvDateEnd = (TextView) view.findViewById(R.id.tv_fragment_history_detail_device_date_end);
        rvHistory = (RecyclerView) view.findViewById(R.id.rv_fragment_history_detail_device);
//        btnLoadData = (Button) view.findViewById(R.id.btn_fragment_history_detail_device_load_data);
    }

    @Override
    protected void initSoureV4Fragment() {
        String dateNow = CommonMethod.getDateNow(TYPE_DATE_TIME_DD_MM_YYYY);
        if (dateBegin == null && dateEnd == null) {
            dateBegin = dateEnd = dateNow;
        }
        tvDateBegin.setText(dateNow);
        tvDateEnd.setText(dateNow);
    }

    @Override
    protected void setActionV4Fragment(@Nullable Bundle savedInstanceState) {
        configRecycler();

//        btnLoadData.setOnClickListener(this);
        tvDateBegin.setOnClickListener(this);
        tvDateEnd.setOnClickListener(this);
    }

    private void configRecycler() {
        LinearLayoutManager linearLayoutManagerParam = new LinearLayoutManager(getActivity());
        rvHistory.setHasFixedSize(false);
        rvHistory.setLayoutManager(linearLayoutManagerParam);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_history_detail_device_date_begin:
                sIdTextViewChooseDate = tvDateBegin.getId();
                mListener.showDateTimePickerFragmentFromHistoryDetailDevice(sIdTextViewChooseDate);
                break;
            case R.id.tv_fragment_history_detail_device_date_end:
                sIdTextViewChooseDate = tvDateEnd.getId();
                mListener.showDateTimePickerFragmentFromHistoryDetailDevice(sIdTextViewChooseDate);
                break;

//            case R.id.btn_fragment_history_detail_device_load_data:
//                mListener.showRecyclerHistoryDetailDevice(mIdDevice, dateBegin, dateEnd);
//                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIdTextViewChooseDate = 0;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public int getmIdDevice() {
        return mIdDevice;
    }

    public void setTextDateTimePicker(int year, int month, int date) {
        if (sIdTextViewChooseDate <= 0)
            return;

        String sMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String sDate = (date < 10) ? "0" + date : String.valueOf(date);
        String timeChoose = sDate + "/" + sMonth + "/" + year;
        long dateBeginL, dateEndL;
        if (sIdTextViewChooseDate == tvDateBegin.getId()) {
            dateBeginL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
            dateEndL = CommonMethod.convertDateToLong(tvDateEnd.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
            if (dateBeginL > dateEndL) {
                tvDateBegin.setText(tvDateEnd.getText().toString());
                Toast.makeText(getContext(), "Thời điểm ngày bắt đầu phải nhỏ hơn ngày kết thúc.", Toast.LENGTH_SHORT).show();
            } else
                tvDateBegin.setText(timeChoose);

        } else {
            dateBeginL = CommonMethod.convertDateToLong(tvDateBegin.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
            dateEndL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
            if (dateBeginL > dateEndL) {
                tvDateEnd.setText(tvDateBegin.getText().toString());
                Toast.makeText(getContext(), "Thời điểm ngày kết thúc phải lớn hơn ngày bắt đầu.", Toast.LENGTH_SHORT).show();
            } else {
                tvDateEnd.setText(timeChoose);
            }
        }

        dateBegin = tvDateBegin.getText().toString();
        dateEnd = tvDateEnd.getText().toString();
    }

    public void setDataRecyclerHistoryDetailDevice(List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs) {
        if (historyDetailDeviceJSONs == null)
            return;
        this.historyDetailDeviceJSONs = new ArrayList<>();
        this.historyDetailDeviceJSONs.addAll(historyDetailDeviceJSONs);

        if(historyDetailDeviceJSONs.isEmpty())
            return;

        historyDetailDeviceAdapter = new HistoryDetailDeviceAdapter(getContext(), historyDetailDeviceJSONs);
        rvHistory.setAdapter(historyDetailDeviceAdapter);
        rvHistory.invalidate();
    }

    public interface OnHistoryDetailDeviceListener {
        void showDateTimePickerFragmentFromHistoryDetailDevice(int sIdTextViewChooseDate);

        void showRecyclerHistoryDetailDevice(int mIdDevice, String beginDate, String endDate);
    }
}
