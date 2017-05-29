package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Model.Inteface.HistoryAndAlarmAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_DD_MM_YYYY;

public class HistoryAndAlarmFragment extends BaseV4Fragment implements View.OnClickListener {
    private Spinner mSpinDevice, mSpinType;
    private TextView tvDateBegin, tvDateEnd;
    private RecyclerView rvEvent;

    private OnHistoryAndAlarmFragmentListener mListener;
    private List<DeviceEntity> deviceEntityList;
    private String[] deviceStrings;
    private ArrayAdapter<String> arrayAdapterDevice;
    private ArrayAdapter<String> arrayAdapterType;
    private int spinnerDeviceIndexChoose = 0;
    private int spinnerTypeIndexChoose = 0;
    private static int sIdTextViewChooseDate;
    private String dateBegin, dateEnd;
    private HistoryAndAlarmAdapter historyAndAlarmAdapter;
    private List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs;

    private int idDeviceChoosen = 0;
    private String nameDeviceChoosen = "";
    private String typeSpinnerHistory = "";

    private static final String HOLDER_DATA_DEVICE_ROTATE = "HOLDER_DATA_DEVICE_ROTATE";
    private static final String HOLDER_DATA_SPINNER_DEVICE_ROTATE = "HOLDER_DATA_SPINNER_TRENDS_ROTATE";
    private static final String HOLDER_DATA_INDEX_SPINNER_DEVICE_ROTATE = "HOLDER_DATA_INDEX_SPINNER_DEVICE_ROTATE";
    private static final String HOLDER_DATA_INDEX_SPINNER_TYPE_ROTATE = "HOLDER_DATA_INDEX_SPINNER_TYPE_ROTATE";
    private static final String HOLDER_DATA_DATE_BEGIN_ROTATE = "HOLDER_DATA_DATE_BEGIN_ROTATE";
    private static final String HOLDER_DATA_DATE_END_ROTATE = "HOLDER_DATA_DATE_END_ROTATE";
    private static final String HOLDER_DATA_HISTORY_ALARM_ROTATE = "HOLDER_DATA_HISTORY_ALARM_ROTATE";

    public HistoryAndAlarmFragment() {
        // Required empty public constructor
    }

    public static HistoryAndAlarmFragment newInstance() {
        HistoryAndAlarmFragment fragment = new HistoryAndAlarmFragment();
        return fragment;
    }

    private SpinnerSelectListenerImp deviceSpinnerListener = new SpinnerSelectListenerImp() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            // On selecting a spinner item
            spinnerDeviceIndexChoose = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private SpinnerSelectListenerImp typeSpinnerListener = new SpinnerSelectListenerImp() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            boolean isRotateAction = mListener.isCheckHasOrientedChange();
            if (!isRotateAction) {
                spinnerTypeIndexChoose = position;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            deviceEntityList = (List<DeviceEntity>) savedInstanceState.getSerializable(HOLDER_DATA_DEVICE_ROTATE);
            deviceStrings = savedInstanceState.getStringArray(HOLDER_DATA_SPINNER_DEVICE_ROTATE);
            spinnerDeviceIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_DEVICE_ROTATE);
            spinnerTypeIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_TYPE_ROTATE);
            dateBegin = savedInstanceState.getString(HOLDER_DATA_DATE_BEGIN_ROTATE);
            dateEnd = savedInstanceState.getString(HOLDER_DATA_DATE_END_ROTATE);
            historyAndAlarmEventJSONs = (List<HistoryAndAlarmEventJSON>) savedInstanceState.getSerializable(HOLDER_DATA_HISTORY_ALARM_ROTATE);

            if (arrayAdapterDevice == null) {
                arrayAdapterDevice = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
            }

            if (historyAndAlarmAdapter == null) {
                historyAndAlarmAdapter = new HistoryAndAlarmAdapter(getContext(), historyAndAlarmEventJSONs, Define.TYPE_SPINNER_HISTORY[spinnerTypeIndexChoose]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_and_alarm, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryAndAlarmFragmentListener) {
            mListener = (OnHistoryAndAlarmFragmentListener) context;
        } else
            throw new ClassCastException("Class must be to implement OnHistoryAndAlarmFragmentListener.");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(View view) {
        mSpinDevice = (Spinner) view.findViewById(R.id.spin_fragment_history_and_alarm_device);
        mSpinType = (Spinner) view.findViewById(R.id.spin_fragment_history_and_alarm_type);
        tvDateBegin = (TextView) view.findViewById(R.id.tv_fragment_history_and_alarm_date_begin);
        tvDateEnd = (TextView) view.findViewById(R.id.tv_fragment_history_and_alarm_date_end);
        rvEvent = (RecyclerView) view.findViewById(R.id.rv_fragment_history_and_alarm);
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
        showSpinnerTypeDefault();
        showSpinnerDeviceDefault();
        if (savedInstanceState != null) {
            //set spin
            mSpinDevice.setSelection(spinnerDeviceIndexChoose);
            mSpinDevice.setAdapter(arrayAdapterDevice);
            mSpinDevice.invalidate();

            //set recycler
            rvEvent.setAdapter(historyAndAlarmAdapter);
        } else {
            mListener.showSpinnerDeviceHistoryAndAlarm();
        }
        mSpinDevice.setOnItemSelectedListener(deviceSpinnerListener);
        mSpinType.setOnItemSelectedListener(typeSpinnerListener);
        tvDateBegin.setOnClickListener(this);
        tvDateEnd.setOnClickListener(this);

    }

    public String getDateBegin() {
        return CommonMethod.convertDateToDate(tvDateBegin.getText().toString(), Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
    }

    public String getDateEnd() {
        return CommonMethod.convertDateToDate(tvDateEnd.getText().toString(), Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
    }

    public int getIdDeviceChoosen() {
        return idDeviceChoosen;
    }

    public String getNameDeviceChoosen() {
        return nameDeviceChoosen;
    }

    public String getTypeSpinnerHistory() {
        return typeSpinnerHistory;
    }

    private void showSpinnerDeviceDefault() {

        deviceStrings = new String[]{Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN};
        arrayAdapterDevice = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
        mSpinDevice.setSelection(spinnerDeviceIndexChoose);
        mSpinDevice.setAdapter(arrayAdapterDevice);
        mSpinDevice.invalidate();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HOLDER_DATA_DEVICE_ROTATE, (Serializable) deviceEntityList);
        outState.putStringArray(HOLDER_DATA_SPINNER_DEVICE_ROTATE, deviceStrings);
        outState.putInt(HOLDER_DATA_INDEX_SPINNER_DEVICE_ROTATE, spinnerDeviceIndexChoose);
        outState.putInt(HOLDER_DATA_INDEX_SPINNER_TYPE_ROTATE, spinnerTypeIndexChoose);
        outState.putString(HOLDER_DATA_DATE_BEGIN_ROTATE, dateBegin);
        outState.putString(HOLDER_DATA_DATE_END_ROTATE, dateEnd);
        outState.putSerializable(HOLDER_DATA_HISTORY_ALARM_ROTATE, (Serializable) historyAndAlarmEventJSONs);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            deviceEntityList = (List<DeviceEntity>) savedInstanceState.getSerializable(HOLDER_DATA_DEVICE_ROTATE);
            deviceStrings = savedInstanceState.getStringArray(HOLDER_DATA_SPINNER_DEVICE_ROTATE);
            spinnerDeviceIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_DEVICE_ROTATE);
            spinnerTypeIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_TYPE_ROTATE);
            dateBegin = savedInstanceState.getString(HOLDER_DATA_DATE_BEGIN_ROTATE);
            dateEnd = savedInstanceState.getString(HOLDER_DATA_DATE_END_ROTATE);
            historyAndAlarmEventJSONs = (List<HistoryAndAlarmEventJSON>) savedInstanceState.getSerializable(HOLDER_DATA_HISTORY_ALARM_ROTATE);

            if (arrayAdapterDevice == null) {
                arrayAdapterDevice = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
            }

            if (historyAndAlarmAdapter == null) {
                historyAndAlarmAdapter = new HistoryAndAlarmAdapter(getContext(), historyAndAlarmEventJSONs, typeSpinnerHistory);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_history_and_alarm_date_begin:
                sIdTextViewChooseDate = tvDateBegin.getId();

                break;
            case R.id.tv_fragment_history_and_alarm_date_end:
                sIdTextViewChooseDate = tvDateEnd.getId();
                break;
        }

        mListener.showDateTimePickerFragmentFromHistoryAndAlarm(sIdTextViewChooseDate);
    }

    public void setTextDateTimePickerHistoryAndAlarm(int year, int month, int date) {
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

    public void setDataSpinnerDevice(List<DeviceEntity> deviceEntities) {
        if (deviceEntities == null)
            return;

        this.deviceEntityList = new ArrayList<>();
        this.deviceEntityList.addAll(deviceEntities);

        int index = 1;
        int sizeDevice = deviceEntityList.size();
        deviceStrings = new String[deviceEntityList.size() + 1];
        deviceStrings[0] = Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN;
        for (; index < sizeDevice + 1; index++) {
            deviceStrings[index] = deviceEntityList.get(index - 1).getName();
        }

        arrayAdapterDevice = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
        mSpinDevice.setPrompt(Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN);
        mSpinDevice.setSelection(spinnerDeviceIndexChoose);
        mSpinDevice.setAdapter(arrayAdapterDevice);
        mSpinDevice.invalidate();
    }

    public void setDataRecyclerHistoryAndAlarm(List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs, String typeString) {
        if (historyAndAlarmEventJSONs == null)
            return;
        if (typeString == null || typeString.isEmpty())
            return;
        this.historyAndAlarmEventJSONs = new ArrayList<>();
        this.historyAndAlarmEventJSONs.addAll(historyAndAlarmEventJSONs);

        if (historyAndAlarmEventJSONs.isEmpty())
            return;

        historyAndAlarmAdapter = new HistoryAndAlarmAdapter(getContext(), historyAndAlarmEventJSONs, typeString);
        rvEvent.setAdapter(historyAndAlarmAdapter);
        rvEvent.invalidate();
    }

    public void refreshDataFromServer() {

        if (spinnerDeviceIndexChoose == 0) {
            return;
        }
        if (deviceEntityList == null || deviceEntityList.size() == 0)
            return;

        idDeviceChoosen = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getID();
        nameDeviceChoosen = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getName();
        typeSpinnerHistory = Define.TYPE_SPINNER_HISTORY[spinnerTypeIndexChoose];
        String beginDate = tvDateBegin.getText().toString();
        String endDate = tvDateEnd.getText().toString();
        beginDate = CommonMethod.convertDateToDate(beginDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
        endDate = CommonMethod.convertDateToDate(endDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
        boolean isRotateAction = mListener.isCheckHasOrientedChange();
        if (!isRotateAction) {
            mListener.showRecyclerHistoryAndAlarm(idDeviceChoosen, nameDeviceChoosen, typeSpinnerHistory, beginDate, endDate);
        }
    }

    public void setNotifyErrorGetAllDeviceSpinner(String message, boolean isResponse) {
        if (message == null || message.isEmpty())
            return;

        Toast.makeText(getContext(), "Gặp vấn đề khi lấy dữ liệu các device từ máy chủ: " + message, Toast.LENGTH_SHORT).show();
    }

    public interface OnHistoryAndAlarmFragmentListener {
        void showSpinnerDeviceHistoryAndAlarm();

        void showDateTimePickerFragmentFromHistoryAndAlarm(int sIdTextViewChooseDate);

        boolean isCheckHasOrientedChange();

        void showRecyclerHistoryAndAlarm(int idDevice, String nameDevice, String typeString, String beginDate, String endDate);
    }


    private void showSpinnerTypeDefault() {
        arrayAdapterType = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, Define.TYPE_SPINNER_HISTORY);
        mSpinType.setSelection(spinnerTypeIndexChoose);
        mSpinType.setAdapter(arrayAdapterType);
        mSpinType.invalidate();
    }

    private void configRecycler() {
        LinearLayoutManager linearLayoutManagerParam = new LinearLayoutManager(getContext());
        rvEvent.setHasFixedSize(false);
        rvEvent.setLayoutManager(linearLayoutManagerParam);
    }

    private abstract class SpinnerSelectListenerImp implements AdapterView.OnItemSelectedListener {

    }

}
