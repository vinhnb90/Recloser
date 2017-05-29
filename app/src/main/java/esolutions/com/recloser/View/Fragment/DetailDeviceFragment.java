package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DetailDeviceEntity;
import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.Entity.ObjectDetailInfoDevice;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Model.DisconnectWifiAdapter;
import esolutions.com.recloser.Model.EventDetailDeviceAdapter;
import esolutions.com.recloser.Model.ParamRecyclerDeviceAdapter;
import esolutions.com.recloser.Model.SpinnerDetailDeviceAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.CustomView.CustomSpinner;

import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_FULL;

public class DetailDeviceFragment
        extends BaseV4Fragment
        implements
        OnChartValueSelectedListener,
        View.OnClickListener,
        CustomSpinner.OnSpinnerEventsListener {
    private String mUser;
    private DeviceEntity mDevice;
    private OnDetailDeviceListener mListener;

    private TextView mTvDeviceName, mTvDeviceLocation, mTvDeviceTypeName, mTvDeviceDescription;
    private ImageView mIvDeviceAvartar;
    private Button mBtnHistory;
    private CustomSpinner mSpin;
    private RecyclerView mRvParam;
    private RecyclerView mRvEvent;
    private LineChart mChartParam;

    private SpinnerDetailDeviceAdapter spinnerDetailDeviceAdapter;
    private ParamRecyclerDeviceAdapter mParamRecyclerDeviceAdapter;
    private EventDetailDeviceAdapter mEventDetailDeviceAdapter;

    //region lưu giá trị khi rotate screen
    private ArrayList<ILineDataSet> mDataSet;
    private ArrayList<ObjectPivotXEntity> mListPivotX;
    private List<ObjectSpinnerParamEntity> mParamSpinList;
    private String mPrompt;
    private boolean isCallJSONSpinner;
    //endregion

    private static final String HOLDER_DATASET_CHART_ROTATE = "HOLDER_DATA_CHART_ROTATE";
    private static final String HOLDER_DATA_PIVOT_ROTATE = "HOLDER_DATA_PIVOT_ROTATE";
    private static final String HOLDER_DATA_SPINNER_ROTATE = "HOLDER_DATA_SPINNER_ROTATE";

    //region test chart
    private static int count;
    private static LineData sLineData;
    DetailDeviceEntity detailDeviceEntities;
    List<ParamHistoryEntity> paramHistoryEntityList_AIO;
    List<ParamHistoryEntity> paramHistoryEntityList_AI1;
    List<ParamHistoryEntity> paramHistoryEntityList_AI2;
    //endregion

    public DetailDeviceFragment() {
        // Required empty public constructor
    }

    public static DetailDeviceFragment newInstance(String user, DeviceEntity deviceEntity) {
        if (user == null || user.isEmpty())
            return null;
        if (deviceEntity == null)
            return null;
        DetailDeviceFragment fragment = new DetailDeviceFragment();
        Bundle args = new Bundle();
        args.putString(Define.PARAM_NAME_USER, user);
        args.putParcelable(Define.PARAM_DEVICE_DETAIL, deviceEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUser = getArguments().getString(Define.PARAM_NAME_USER);
            mDevice = getArguments().getParcelable(Define.PARAM_DEVICE_DETAIL);
        }
        if (savedInstanceState != null) {
            mDataSet = (ArrayList<ILineDataSet>) savedInstanceState.getSerializable(HOLDER_DATASET_CHART_ROTATE);
            mListPivotX = (ArrayList<ObjectPivotXEntity>) savedInstanceState.getSerializable(HOLDER_DATA_PIVOT_ROTATE);
            mParamSpinList = (ArrayList<ObjectSpinnerParamEntity>) savedInstanceState.getSerializable(HOLDER_DATA_SPINNER_ROTATE);

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HOLDER_DATASET_CHART_ROTATE, (Serializable) mDataSet);
        outState.putSerializable(HOLDER_DATA_PIVOT_ROTATE, (Serializable) mListPivotX);
        outState.putSerializable(HOLDER_DATA_SPINNER_ROTATE, (Serializable) mParamSpinList);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mDataSet = (ArrayList<ILineDataSet>) savedInstanceState.getSerializable(HOLDER_DATASET_CHART_ROTATE);
            mListPivotX = (ArrayList<ObjectPivotXEntity>) savedInstanceState.getSerializable(HOLDER_DATA_PIVOT_ROTATE);
            mParamSpinList = (ArrayList<ObjectSpinnerParamEntity>) savedInstanceState.getSerializable(HOLDER_DATA_SPINNER_ROTATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_device, container, false);

        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailDeviceListener) {
            mListener = (OnDetailDeviceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailDeviceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mDataSet = new ArrayList<>();
        mListPivotX = new ArrayList<>();
        mParamSpinList = new ArrayList<>();
        isCallJSONSpinner = false;
    }

    @Override
    protected void initViewV4Fragment(final View view) {
        mTvDeviceName = (TextView) view.findViewById(R.id.tv_fragment_detail_device_name);
        mTvDeviceLocation = (TextView) view.findViewById(R.id.tv_fragment_detail_device_location);
        mTvDeviceTypeName = (TextView) view.findViewById(R.id.tv_fragment_detail_device_type_name);
        mTvDeviceDescription = (TextView) view.findViewById(R.id.tv_fragment_detail_device_type_description);
        mSpin = (CustomSpinner) view.findViewById(R.id.sp_fragment_detail_device);
        mIvDeviceAvartar = (ImageView) view.findViewById(R.id.iv_fragment_detail_device_device);
        mBtnHistory = (Button) view.findViewById(R.id.btn_fragment_detail_device_history);
        mRvParam = (RecyclerView) view.findViewById(R.id.rv_fragment_detail_device_param);
        mRvEvent = (RecyclerView) view.findViewById(R.id.rv_fragment_detail_device_event);
        mChartParam = (LineChart) view.findViewById(R.id.lineChart_fragment_detail_device);

    }

    @Override
    protected void initSoureV4Fragment() {
    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        mBtnHistory.setOnClickListener(this);

        //config recycler
        configRecyclerView();

        //config char
        configChartParam();

        if (savedInstanceState != null) {
            refreshDataLineChart(mDataSet, mListPivotX);

            setDataSpinnerParam(mParamSpinList, false);
        } else {
            mListener.showInfoSpiner(mDevice.getID());
        }

        mSpin.setSpinnerEventsListener(this);
        mListener.showInfoDetailDevice(mDevice.getID());

        mListener.showInfoRecyclerParam(mDevice.getID());
        mListener.showInfoRecyclerEvent(mDevice.getID());

        //test
//        createDataTest();
    }

    public DeviceEntity getmDevice() {
        return mDevice;
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManagerParam = new LinearLayoutManager(getContext());
        mRvParam.setHasFixedSize(false);
        mRvParam.setLayoutManager(linearLayoutManagerParam);

        LinearLayoutManager linearLayoutManagerEvent = new LinearLayoutManager(getContext());
        mRvEvent.setHasFixedSize(false);
        mRvEvent.setLayoutManager(linearLayoutManagerEvent);
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

    public void fillDataInfoDetailDevice(ObjectDetailInfoDevice objectDetailInfoDevice, Bitmap bitmap) {
        if (objectDetailInfoDevice == null)
            return;

        //textview
        mTvDeviceName.setText(objectDetailInfoDevice.getDeviceName());
        mTvDeviceLocation.setText(objectDetailInfoDevice.getDeviceLocation());
        mTvDeviceTypeName.setText(objectDetailInfoDevice.getDeviceTypename());
        mTvDeviceDescription.setText(objectDetailInfoDevice.getDeviceDescription());
        mIvDeviceAvartar.setImageBitmap(bitmap);
    }

    private String getTimeBeginDay(String timeEnd) {
        return null;
    }

    //region OnChartValueSelectedListener
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void refreshDataLineChart(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> listPivotX) {
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
                    return CommonMethod.convertDateToDate(pivotX, TYPE_DATE_TIME_FULL, Define.TYPE_DATE_TIME_HH_MM);
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

    public void addDataLineChart(final LineDataSet lineDataSet, List<ParamHistoryEntity> paramHistoryEntity, String param) {
        int posLine;
        if (mChartParam.getLineData() == null) {
            //tạo hiệu ứng khi add line
            posLine = 0;
            setDataEachPoint(lineDataSet, paramHistoryEntity, param, posLine);
        } else {
            posLine = checkPosLine(param);
            //nếu ko có line thì add line đó
            if (posLine == -1) {
                setDataEachPoint(lineDataSet, paramHistoryEntity, param, posLine);
            }
        }
    }

    private void setDataEachPoint(final LineDataSet lineDataSet, final List<ParamHistoryEntity> paramHistoryEntity, String param, int posLine) {
        int countLineChart;
//        final int sizePivotFill = lineDataSet.getEntryCount();

        final int sizeParamHistory = paramHistoryEntity.size();

        Entry firstEntry = lineDataSet.getEntryForIndex(0);
        firstEntry.setX(0);
        List<Entry> listEntryOnlyFirst = new ArrayList<>();
        listEntryOnlyFirst.add(firstEntry);
        LineDataSet lineDataSet1 = new LineDataSet(listEntryOnlyFirst, param);
        lineDataSet1.setLineWidth(4.5f);
        lineDataSet1.setCircleRadius(9f);
        lineDataSet1.setCircleHoleRadius(4.5f);

        if (mChartParam.getLineData() == null) {
            countLineChart = 0;
            LineData lineData = new LineData(lineDataSet);
            //TODO get lable mark to lineChart
            IAxisValueFormatter formatterX = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    //do đã lấy 1 điểm gắn vào trước nên getValue thực của x sẽ phải thêm 1
                    int valueReal = Math.round(value);
                    if (valueReal <= sizeParamHistory)
                        return paramHistoryEntity.get((int) valueReal).getTimestamp();
                    return "Null";
                }
            };

            //TODO init property LineChart
            XAxis xAxis = mChartParam.getXAxis();
            xAxis.setGranularity(1); // indentiy auto increment
            xAxis.setValueFormatter(formatterX);
            xAxis.setPosition(XAxis.XAxisPosition.TOP);


//            YAxis yAxis = mChartParam.getAxisLeft();
//            yAxis.setValueFormatter(new MyValueFormatter());

            mChartParam.setData(lineData);
        } else {
            countLineChart = mChartParam.getLineData().getEntryCount();
//            mChartParam.getLineData().addDataSet(lineData);
        }

//        ((LineDataSet) mChartParam.getLineData().getDataSets().get(countLineChart)).setCircleColor(mColors[countLineChart % mColors.length]);
//        ((LineDataSet) mChartParam.getLineData().getDataSets().get(countLineChart)).setColor(mColors[countLineChart % mColors.length]);
//        mChartParam.animateXY(1000, 1000);
//        mChartParam.notifyDataSetChanged();
//        mChartParam.invalidate();


        for (int indexPivot = 1; indexPivot < sizeParamHistory; indexPivot++) {
            Entry newEntry = lineDataSet.getEntryForIndex(indexPivot);
//            mChartParam.getData().addEntry(newEntry, indexPivot);
            mChartParam.getData().getDataSets().get(posLine).addEntryOrdered(newEntry);
//            mChartParam.getLineData().getDataSetByLabel(param, true).addEntry(newEntry);
            mChartParam.animateXY(1000, 1000);
            mChartParam.notifyDataSetChanged();
//            mChartParam.moveViewTo(mChartParam.getData().getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
//            mChartParam.invalidate();
        }
    }

    public void removeDataLineChart(String param) {
        if (param == null || param.isEmpty())
            return;

        if (mChartParam.getLineData() == null) {
            return;
        }

        int posLine = checkPosLine(param);
        //nếu ko có line
        if (posLine <= -1)
            return;
            //nếu có line thì remove line đó
        else {
            mChartParam.getLineData().removeDataSet(posLine);
            mChartParam.notifyDataSetChanged();
            mChartParam.invalidate();
        }
    }

    private void validateChart(List<ILineDataSet> sLineDataCloneBiger, List<ILineDataSet> sLineDataCloneSmaller) {
        //TODO tính tổng các line của 2 bên bigger và smaller
        int totalLineSmaller = sLineDataCloneSmaller.size();
        int totalLineBigger = sLineDataCloneBiger.size();

        //TODO  tính tổng các điểm X của 2 bên bigger và smaller
        int totalPivotXSmaller = sLineDataCloneSmaller.get(0).getEntryCount();
        int totalPivotXBiger = sLineDataCloneBiger.get(0).getEntryCount();

        for (int indexPivotX = 0; indexPivotX < totalPivotXBiger; indexPivotX++) {
            for (int indexLine = 0; indexLine < totalLineBigger; indexLine++) {
                //tại điểm x thứ indexPivotX kiểm tra trạng thái line đó như thế nào
                String lableLineNew = sLineDataCloneBiger.get(indexLine).getLabel();
                boolean hasLine = checkStatusLine(lableLineNew, sLineDataCloneSmaller);
                if (hasLine) {
                    //vẫn còn line cũ thì giữ nguyên
                } else {
                    //ngược lại update điểm mới
                    Entry newPoint = sLineDataCloneBiger.get(indexLine).getEntryForIndex(indexPivotX);

                    //so sánh độ rộng, kiểm tra nếu thực sự tại vị trí index pivot không có thì sẽ add, nếu có thì sẽ remove
                    //mục đích làm chung cho các trường hợp khác đều có thê sử dụng hàm này trong trường hợp phải remove
                    if (mChartParam.getLineData().getDataSets().size() == sLineDataCloneSmaller.size())
                        mChartParam.getLineData().getDataSets().get(indexLine).addEntry(newPoint);
                    else
                        mChartParam.getLineData().getDataSets().get(indexLine).removeEntry(indexPivotX);

                }
            }
            mChartParam.notifyDataSetChanged();
            mChartParam.animateXY(1000, 1000);
            mChartParam.invalidate();
        }
    }

    private boolean checkStatusLine(String lableLineNew, List<ILineDataSet> sLineDataCloneOld) {
        for (ILineDataSet iLineDataSet :
                sLineDataCloneOld) {
            String lableLineOld = iLineDataSet.getLabel();
            if (lableLineNew.equals(lableLineOld))
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_detail_device_history:
                mListener.showHistoryFragmentFromDetailDevice(mDevice.getID());
                break;
        }
    }

    //endregion

    private int checkPosLine(String param) {
        int position = -1;
        if (mChartParam.getLineData() == null) {
            return position;
        }
        List<ILineDataSet> mDataSetsClone = mChartParam.getLineData().getDataSets();
//        ArrayList<ILineDataSet> mDataSetsClone = new ArrayList<>(mDataSets);
        for (int index = 0; index < mDataSetsClone.size(); index++) {
            if (mDataSetsClone.get(index).getLabel().equals(param)) {
                position = index;
            }
        }
        return position;
    }

    public void setDataSpinnerParam(List<ObjectSpinnerParamEntity> entityList, boolean isCallJSONSpinner) {
        if (entityList == null)
            return;

        List<ObjectSpinnerParamEntity> paramSpinListClone = new ArrayList<>();
        paramSpinListClone.addAll(entityList);

        this.isCallJSONSpinner = isCallJSONSpinner;

        if (paramSpinListClone.size() > 0) {
            if (spinnerDetailDeviceAdapter == null) {
                spinnerDetailDeviceAdapter = new SpinnerDetailDeviceAdapter(getActivity(), 0, paramSpinListClone, mDevice.getID());
            } else
                spinnerDetailDeviceAdapter.refresh(paramSpinListClone);

            mSpin.setSelection(0);
            mSpin.setAdapter(spinnerDetailDeviceAdapter);

            //set Prompt khi refill rotate screen
            if (mParamSpinList != null) {
                //set Prompt
                //merger chuỗi param cùng nhau
                StringBuilder params = new StringBuilder();
                for (int indexParam = 0; indexParam < mParamSpinList.size(); indexParam++) {
                    if (mParamSpinList.get(indexParam).ismSelected()) {
                        params.append(mParamSpinList.get(indexParam).getParamDescription());
                        params.append(Define.SYMBOL_PARAM_URL);
                    }
                }

                mPrompt = Define.PROMT_SPINER_PARAM_OPEN;
                if (params.length() > 0) {
                    //xóa kí tự SYMBOL_PARAM_URL cuối
                    params.replace(params.length() - 1, params.length(), "");
                    mPrompt = params.toString();
                }
            }
            ((SpinnerDetailDeviceAdapter) mSpin.getAdapter()).setPrompt(mPrompt);


            mSpin.invalidate();

            if (mParamSpinList == null)
                mParamSpinList = new ArrayList<>();
            mParamSpinList.clear();
            mParamSpinList.addAll(paramSpinListClone);
        }
    }

    public void setDataRecyclerParam(List<ObjectParamaterDeviceJSON> entityList) {
        if (entityList == null)
            return;
        if (mParamRecyclerDeviceAdapter == null)
            mParamRecyclerDeviceAdapter = new ParamRecyclerDeviceAdapter(entityList);
        else
            mParamRecyclerDeviceAdapter.refresh(entityList);
        mRvParam.setAdapter(mParamRecyclerDeviceAdapter);
        mRvParam.invalidate();
    }

    public void setDataRecyclerEvent(List<ObjectEventDevice> entityList) {
        if (entityList == null)
            return;
        if (mEventDetailDeviceAdapter == null)
            mEventDetailDeviceAdapter = new EventDetailDeviceAdapter(entityList);
        else
            mEventDetailDeviceAdapter.refresh(entityList);
        mRvEvent.setAdapter(mEventDetailDeviceAdapter);
        mRvEvent.invalidate();
    }

    public void notifyErrorInfoDetail(String message) {
        if (message == null)
            return;
        mTvDeviceName.setText(Define.ERROR);
        mTvDeviceLocation.setText(Define.ERROR);
        mTvDeviceTypeName.setText(Define.ERROR);
        mTvDeviceDescription.setText(Define.ERROR);
        mIvDeviceAvartar.setImageBitmap(null);
    }

    public void notifyErrorSpinnerParamDetail(String message, boolean isResponse) {
//        mSpin.setPrompt(Define.ERROR);
//        mSpin.invalidate();
    }

    public void notifyErrorRecyclerParamDetail(String message, boolean isResponse) {

        DisconnectWifiAdapter notifyAdapter = new DisconnectWifiAdapter(message, isResponse);

        LinearLayoutManager linearLayoutManagerNotify = new LinearLayoutManager(getContext());
        mRvParam.setHasFixedSize(false);
        mRvParam.setLayoutManager(linearLayoutManagerNotify);

        mRvParam.setAdapter(notifyAdapter);
        mRvParam.invalidate();
    }

    public void notifyErrorRecyclerEventDetailDevice(String message, boolean isResponse) {
        DisconnectWifiAdapter notifyAdapter = new DisconnectWifiAdapter(message, isResponse);

        LinearLayoutManager linearLayoutManagerNotify = new LinearLayoutManager(getContext());
        mRvEvent.setHasFixedSize(false);
        mRvEvent.setLayoutManager(linearLayoutManagerNotify);

        mRvEvent.setAdapter(notifyAdapter);
        mRvEvent.invalidate();
    }

    public void notifyLineParamChartDetailDevice(String message, boolean isResponse) {
        if (message == null || message.isEmpty())
            return;
        if (isResponse) {
            Toast.makeText(getContext(), "Thông tin: " + message, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Gặp vấn đề khi tải dữ liệu chart\nNội dung: " + message, Toast.LENGTH_SHORT).show();
    }

    //region CustomSpinner.OnSpinnerEventsListener
    @Override
    public void onSpinnerOpened(Spinner spinner) {
        mListener.callActionOnSpinnerOpened(spinner, mDevice.getID());
    }

    @Override
    public void onSpinnerClosed(Spinner spinner) {
        if (mParamSpinList != null) {

            //set Prompt
            ////set Prompt khi click in spinner
            StringBuilder params = new StringBuilder();
            for (int indexParam = 0; indexParam < mParamSpinList.size(); indexParam++) {
                if (mParamSpinList.get(indexParam).ismSelected()) {
                    params.append(mParamSpinList.get(indexParam).getParamDescription());
                    params.append(Define.SYMBOL_PARAM_URL);
                }
            }

            mPrompt = Define.PROMT_SPINER_PARAM_OPEN;
            if (params.length() > 0) {
                //xóa kí tự SYMBOL_PARAM_URL cuối
                params.replace(params.length() - 1, params.length(), "");
                mPrompt = params.toString();
            }
        }
        ((SpinnerDetailDeviceAdapter) mSpin.getAdapter()).setPrompt(mPrompt);

        if (isCallJSONSpinner) {
            mListener.callActionOnSpinnerClosed(spinner, mDevice.getID());
        }
    }

    //endregion

    public interface OnDetailDeviceListener {
        void showInfoDetailDevice(int idDevice);

        void showInfoSpiner(int idDevice);

        void showInfoRecyclerParam(int idDevice);

        void showInfoRecyclerEvent(int idDevice);

        void callActionOnSpinnerClosed(Spinner spinner, int idDevice);

        void callActionOnSpinnerOpened(Spinner spinner, int idDevice);

        void showHistoryFragmentFromDetailDevice(int idDevice);

//        void showChart(String user, int idDevice, String param, String timeStart, String timeEnd);
    }
}
