package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Model.HistoryDeviceTrendsAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_DD_MM_YYYY;
import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_FULL;
import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_FULL_TYPE_2;
import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_FULL_TYPE_3;

public class TrendsFragment extends BaseV4Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnChartValueSelectedListener {
    private OnTrendsFragmentListener mListener;
    private TextView mTvStartDate, mTvEndDate;
    private Spinner mSpinDevice;

    private RecyclerView mRvParam;
    private LineChart mChartParam;
    private static int sIdTextViewChooseDate;
    private HistoryDeviceTrendsAdapter adapter;

    //region lưu giá trị khi rotate screen
    private ArrayList<ILineDataSet> mDataSet;
    private ArrayList<ObjectPivotXEntity> mListPivotX;
    private List<DeviceEntity> deviceEntityList;
    private ArrayAdapter<String> arrayAdapter;
    private String[] deviceStrings;
    private int spinnerDeviceIndexChoose = 0;
    private String dateBegin, dateEnd;
    private int idDeviceChoose = 0;
    private String nameDeviceChoose = "";
    //endregion

    private static final String HOLDER_DATASET_CHART_TRENDS_ROTATE = "HOLDER_DATASET_CHART_TRENDS_ROTATE";
    private static final String HOLDER_DATA_PIVOT_TRENDS_ROTATE = "HOLDER_DATA_PIVOT_TRENDS_ROTATE";
    private static final String HOLDER_DATA_DEVICE_ROTATE = "HOLDER_DATA_DEVICE_ROTATE";
    private static final String HOLDER_DATA_SPINNER_TRENDS_ROTATE = "HOLDER_DATA_SPINNER_TRENDS_ROTATE";
    private static final String HOLDER_DATA_INDEX_SPINNER_ROTATE = "HOLDER_DATA_INDEX_SPINNER_ROTATE";
    private static final String HOLDER_DATA_DATE_BEGIN_ROTATE = "HOLDER_DATA_DATE_BEGIN_ROTATE";
    private static final String HOLDER_DATA_DATE_END_ROTATE = "HOLDER_DATA_DATE_END_ROTATE";

    public TrendsFragment() {
        // Required empty public constructor
    }

    public static TrendsFragment newInstance() {
        TrendsFragment fragment = new TrendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDataSet = (ArrayList<ILineDataSet>) savedInstanceState.getSerializable(HOLDER_DATASET_CHART_TRENDS_ROTATE);
            mListPivotX = (ArrayList<ObjectPivotXEntity>) savedInstanceState.getSerializable(HOLDER_DATA_PIVOT_TRENDS_ROTATE);
            deviceEntityList = (List<DeviceEntity>) savedInstanceState.getSerializable(HOLDER_DATA_DEVICE_ROTATE);
            deviceStrings = savedInstanceState.getStringArray(HOLDER_DATA_SPINNER_TRENDS_ROTATE);
            spinnerDeviceIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_ROTATE);
            dateBegin = savedInstanceState.getString(HOLDER_DATA_DATE_BEGIN_ROTATE);
            dateEnd = savedInstanceState.getString(HOLDER_DATA_DATE_END_ROTATE);

            if (arrayAdapter == null) {
                arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HOLDER_DATASET_CHART_TRENDS_ROTATE, (Serializable) mDataSet);
        outState.putSerializable(HOLDER_DATA_PIVOT_TRENDS_ROTATE, (Serializable) mListPivotX);
        outState.putSerializable(HOLDER_DATA_DEVICE_ROTATE, (Serializable) deviceEntityList);
        outState.putStringArray(HOLDER_DATA_SPINNER_TRENDS_ROTATE, deviceStrings);
        outState.putInt(HOLDER_DATA_INDEX_SPINNER_ROTATE, spinnerDeviceIndexChoose);
        outState.putString(HOLDER_DATA_DATE_BEGIN_ROTATE, dateBegin);
        outState.putString(HOLDER_DATA_DATE_END_ROTATE, dateEnd);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mDataSet = (ArrayList<ILineDataSet>) savedInstanceState.getSerializable(HOLDER_DATASET_CHART_TRENDS_ROTATE);
            mListPivotX = (ArrayList<ObjectPivotXEntity>) savedInstanceState.getSerializable(HOLDER_DATA_PIVOT_TRENDS_ROTATE);
            deviceEntityList = (List<DeviceEntity>) savedInstanceState.getSerializable(HOLDER_DATA_DEVICE_ROTATE);
            deviceStrings = savedInstanceState.getStringArray(HOLDER_DATA_SPINNER_TRENDS_ROTATE);
            spinnerDeviceIndexChoose = savedInstanceState.getInt(HOLDER_DATA_INDEX_SPINNER_ROTATE);
            dateBegin = savedInstanceState.getString(HOLDER_DATA_DATE_BEGIN_ROTATE);
            dateEnd = savedInstanceState.getString(HOLDER_DATA_DATE_END_ROTATE);

            if (arrayAdapter == null) {
                arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrendsFragmentListener) {
            mListener = (OnTrendsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTrendsFragmentListener");
        }
        deviceStrings = new String[]{Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN};
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mDataSet = new ArrayList<>();
        mListPivotX = new ArrayList<>();
        deviceEntityList = new ArrayList<>();
        sIdTextViewChooseDate = 0;
    }

    @Override
    protected void initViewV4Fragment(final View view) {
        mTvStartDate = (TextView) view.findViewById(R.id.tv_fragment_trends_start_date);
        mTvEndDate = (TextView) view.findViewById(R.id.tv_fragment_trends_end_date);
        mSpinDevice = (Spinner) view.findViewById(R.id.sp_fragment_trends);
        mRvParam = (RecyclerView) view.findViewById(R.id.rv_fragment_trends);
        mChartParam = (LineChart) view.findViewById(R.id.lineChart_fragment_trends);
    }

    @Override
    protected void initSoureV4Fragment() {
        String dateNow = CommonMethod.getDateNow(TYPE_DATE_TIME_DD_MM_YYYY);
        mTvStartDate.setText(dateNow);
        mTvEndDate.setText(dateNow);
    }

    @Override
    protected void setActionV4Fragment(@Nullable Bundle savedInstanceState) {
        configChartParam();
        configRecycler();

        if (savedInstanceState != null) {
            mTvStartDate.setText(dateBegin);
            mTvEndDate.setText(dateEnd);
            setDataSpinnerAllDeviceTrends(deviceEntityList);
            refreshDataLineChartTrends(mDataSet, mListPivotX);
        } else {
            //set spin default
            arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
            mSpinDevice.setAdapter(arrayAdapter);
            mSpinDevice.setPrompt(Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN);
            mSpinDevice.setSelection(spinnerDeviceIndexChoose);
            mSpinDevice.invalidate();

            //call new action
            mListener.showSpinnerDeviceTrends();
        }

        mSpinDevice.setOnItemSelectedListener(this);
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);
//        mListener.showDateTimePickerFragmentFromTrendsFragment();
//        mListener.showAllLineChartAndRecyclerTrends();
//        mListener.showRecylerParam();
    }

    private void configRecycler() {
        LinearLayoutManager linearLayoutManagerParam = new LinearLayoutManager(getContext());
        mRvParam.setHasFixedSize(false);
        mRvParam.setLayoutManager(linearLayoutManagerParam);
    }

    private void configChartParam() {

        mChartParam.setOnChartValueSelectedListener(this);
        mChartParam.setDrawGridBackground(true);
        mChartParam.getDescription().setEnabled(false);
        mChartParam.setDrawBorders(true);

        //set đường tham số trục x hiện bên nào
        mChartParam.getAxisLeft().setEnabled(false);
        mChartParam.getAxisRight().setEnabled(true);

        // enable touch gestures
        mChartParam.setTouchEnabled(true);

        // enable scaling and dragging
        mChartParam.setDragEnabled(true);
        mChartParam.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartParam.setPinchZoom(false);
        mChartParam.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBackgroundLineChart));

        //set text show when no data
        mChartParam.setNoDataText(Define.TEXT_NO_DATA);
        Paint p = mChartParam.getPaint(Chart.PAINT_INFO);
        p.setTextSize(Define.SIZE_TEXT_NO_DATA);
        p.setFakeBoldText(true);
        p.setColor(ContextCompat.getColor(getActivity(), R.color.color_text_default));
        Typeface rbfont = Typeface.createFromAsset(getActivity().getAssets(), Define.TYPE_FONT.IOS_VNI.getPathFont());
        p.setTypeface(rbfont);

        Legend l = mChartParam.getLegend();
        l.setFormSize(10f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);

    }

    public void setDataSpinnerAllDeviceTrends(List<DeviceEntity> deviceEntityList) {
        if (deviceEntityList == null)
            return;
        this.deviceEntityList = new ArrayList<>();
        this.deviceEntityList.addAll(deviceEntityList);

        int index = 1;
        int sizeDevice = deviceEntityList.size();
        deviceStrings = new String[deviceEntityList.size() + 1];
        deviceStrings[0] = Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN;
        for (; index < sizeDevice + 1; index++) {
            deviceStrings[index] = deviceEntityList.get(index - 1).getName();
        }

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.row_spinner_trends_all_device, R.id.tv_row_spinner_trends_all_device, deviceStrings);
        mSpinDevice.setPrompt(Define.PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN);
        mSpinDevice.setSelection(spinnerDeviceIndexChoose);
        mSpinDevice.setAdapter(arrayAdapter);
        mSpinDevice.invalidate();
    }

    public String getDateBegin() {
        return CommonMethod.convertDateToDate(mTvStartDate.getText().toString(), Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
    }

    public String getDateEnd() {
        return CommonMethod.convertDateToDate(mTvEndDate.getText().toString(), Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
    }

    public int getIdDeviceChoose() {
        return idDeviceChoose;
    }

    public String getNameDeviceChoose() {
        return nameDeviceChoose;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_trends_start_date:
                sIdTextViewChooseDate = mTvStartDate.getId();

                break;
            case R.id.tv_fragment_trends_end_date:
                sIdTextViewChooseDate = mTvEndDate.getId();
                break;
        }

        mListener.showDateTimePickerFragmentFromTrendsFragment(sIdTextViewChooseDate);
    }

    public void setTextDateTimePicker(int year, int month, int date) {
        if (sIdTextViewChooseDate <= 0)
            return;

        String sMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String sDate = (date < 10) ? "0" + date : String.valueOf(date);
        String timeChoose = sDate + "/" + sMonth + "/" + year;
        long dateBeginL, dateEndL;
        if (sIdTextViewChooseDate == mTvStartDate.getId()) {
            dateBeginL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
            dateEndL = CommonMethod.convertDateToLong(mTvEndDate.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
            if (dateBeginL > dateEndL) {
                mTvStartDate.setText(mTvEndDate.getText().toString());
                Toast.makeText(getContext(), "Thời điểm ngày bắt đầu phải nhỏ hơn ngày kết thúc.", Toast.LENGTH_SHORT).show();
            } else
                mTvStartDate.setText(timeChoose);

        } else {
            dateBeginL = CommonMethod.convertDateToLong(mTvStartDate.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
            dateEndL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
            if (dateBeginL > dateEndL) {
                mTvEndDate.setText(mTvStartDate.getText().toString());
                Toast.makeText(getContext(), "Thời điểm ngày kết thúc phải lớn hơn ngày bắt đầu.", Toast.LENGTH_SHORT).show();
            } else
                mTvEndDate.setText(timeChoose);
        }

        dateBegin = mTvStartDate.getText().toString();
        dateEnd = mTvEndDate.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        spinnerDeviceIndexChoose = position;
        if (position == 0)
            return;
        idDeviceChoose = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getID();
        nameDeviceChoose = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getName();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void refreshDataLineChartTrends(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> listPivotX) {
        if (listPivotX == null)
            return;

        if (dataSet == null)
            return;

        if (mDataSet == null)
            mDataSet = new ArrayList<>();

        if (mListPivotX == null)
            mListPivotX = new ArrayList<>();

        final ArrayList<ObjectPivotXEntity> listPivotXClone = new ArrayList<>();
        listPivotXClone.addAll(listPivotX);
        this.mListPivotX = new ArrayList<>();
        mListPivotX.addAll(listPivotX);

        ArrayList<ILineDataSet> dataSetClone = new ArrayList<>();
        dataSetClone.addAll(dataSet);

        //TODO get lable mark to lineChart
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (Math.round(value) <= listPivotXClone.size()) {
                    String pivotX = listPivotXClone.get((int) value).getTimeStamp();
                    return CommonMethod.convertDateToDate(pivotX, TYPE_DATE_TIME_FULL_TYPE_2, TYPE_DATE_TIME_FULL_TYPE_3);
                }
                return "Null";
            }
        };

        //TODO init property LineChart
        XAxis xAxis = mChartParam.getXAxis();
        xAxis.setGranularity(1); // indentiy auto increment
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        LineData data = new LineData(dataSetClone);
        mDataSet.clear();
        mDataSet.addAll(dataSetClone);
        mListPivotX.clear();
        mListPivotX.addAll(listPivotXClone);

        mChartParam.animateXY(Define.ANIMATE_X, Define.ANIMATE_Y);
        mChartParam.setData(data);
        mChartParam.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void setDataRecyclerTrends(List<ParamHistoryEntity> paramHistoryEntityList) {
        if (paramHistoryEntityList == null)
            return;
        if (adapter == null)
            adapter = new HistoryDeviceTrendsAdapter(getContext(), paramHistoryEntityList);
        else
            adapter.refreshAdapter(paramHistoryEntityList);
        mRvParam.setAdapter(adapter);
        mRvParam.invalidate();
    }

    public void refreshDataFromServer() {

        if (spinnerDeviceIndexChoose == 0) {
            return;
        }
        if (deviceEntityList == null || deviceEntityList.size() == 0)
            return;

        idDeviceChoose = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getID();
        nameDeviceChoose = deviceEntityList.get(spinnerDeviceIndexChoose - 1).getName();
        String beginDate = mTvStartDate.getText().toString();
        String endDate = mTvEndDate.getText().toString();
        beginDate = CommonMethod.convertDateToDate(beginDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
        endDate = CommonMethod.convertDateToDate(endDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
        boolean isRotateAction = mListener.isCheckHasOrientedChange();
        if (!isRotateAction) {
            mListener.showAllLineChartAndRecyclerTrends(idDeviceChoose, nameDeviceChoose, beginDate, endDate);
        }
    }

    public void notifyErrorLineParamChartDeviceTrends(String message, boolean isResponse) {
        if (message == null || message.isEmpty())
            return;
        if (isResponse) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Gặp vấn đề khi tải dữ liệu Trends chart\nNội dung: " + message, Toast.LENGTH_SHORT).show();
    }

    public interface OnTrendsFragmentListener {
        void showSpinnerDeviceTrends();

        void showDateTimePickerFragmentFromTrendsFragment(int idTextView);

        void showAllLineChartAndRecyclerTrends(int idDevice, String nameDevice, String startDate, String endDate);

        boolean isCheckHasOrientedChange();
    }

}
