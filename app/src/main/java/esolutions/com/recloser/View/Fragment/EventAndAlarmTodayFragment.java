package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Model.EventAndAlarmTodayAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY;

import static esolutions.com.recloser.Utils.Class.Define.TYPE_DATE_TIME_DD_MM_YYYY;

public class EventAndAlarmTodayFragment extends BaseV4Fragment{
    private static final String PARAM_TYPE = "PARAM_TYPE";

    private OnEventOrAlarmTodayFragmentListener mListener;

//    private TextView tvDateBegin, tvDateEnd;
    private RecyclerView rvEventAndAlarm;

    private STATE_GET_HISTORY_ALARM_EVENT_TODAY type;
//    private String dateBegin, dateEnd;
//    private int idTextDateChoose = 0;
    private List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays;
    private EventAndAlarmTodayAdapter eventAndAlarmTodayAdapter;

    public EventAndAlarmTodayFragment() {
        // Required empty public constructor
    }

    public static EventAndAlarmTodayFragment newInstance(STATE_GET_HISTORY_ALARM_EVENT_TODAY type) {
        EventAndAlarmTodayFragment fragment = new EventAndAlarmTodayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public STATE_GET_HISTORY_ALARM_EVENT_TODAY getType() {
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_or_alarm_value, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventOrAlarmTodayFragmentListener) {
            mListener = (OnEventOrAlarmTodayFragmentListener) context;
        } else {
            throw new ClassCastException(context.getClass().getName()
                    + " must implement OnEventOrAlarmTodayFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(final View view) {
//        tvDateBegin = (TextView) view.findViewById(R.id.tv_fragment_value_date_begin);
//        tvDateEnd = (TextView) view.findViewById(R.id.tv_fragment_value_date_end);
        rvEventAndAlarm = (RecyclerView) view.findViewById(R.id.rv_fragment_value);
    }

    @Override
    protected void initSoureV4Fragment() {
        Bundle bundle = getArguments();
        type = (STATE_GET_HISTORY_ALARM_EVENT_TODAY) bundle.getSerializable(PARAM_TYPE);
    }

    @Override
    protected void setActionV4Fragment(@Nullable Bundle savedInstanceState) {
        configRecycler();
//        setDefaultTextDate();
//        tvDateBegin.setOnClickListener(this);
//        tvDateEnd.setOnClickListener(this);
        mListener.showRecyclerFromHistoryEventOrAlarmTodayFragment(type);
    }

   /* private void setDefaultTextDate() {
        String dateNow = CommonMethod.getDateNow(TYPE_DATE_TIME_DD_MM_YYYY);
//        dateBegin = dateEnd = dateNow;
//        tvDateBegin.setText(dateBegin);
//        tvDateEnd.setText(dateEnd);
    }
*/
    private void configRecycler() {
        rvEventAndAlarm.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvEventAndAlarm.setHasFixedSize(false);
    }

    /*@Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_fragment_value_date_begin:
                idTextDateChoose = tvDateBegin.getId();
                mListener.showDateTimePickerFragmentFromEventOrAlarmFragment(idTextDateChoose);
                break;

            case R.id.tv_fragment_value_date_end:
                idTextDateChoose = tvDateEnd.getId();
                mListener.showDateTimePickerFragmentFromEventOrAlarmFragment(idTextDateChoose);
                break;
        }
    }*/

//    public void setTextDateTimePicker(int year, int month, int date) {
//        if (idTextDateChoose <= 0)
//            return;
//
//        String sMonth = (month < 10) ? "0" + month : String.valueOf(month);
//        String sDate = (date < 10) ? "0" + date : String.valueOf(date);
//        String timeChoose = sDate + "/" + sMonth + "/" + year;
//        long dateBeginL, dateEndL;
//        if (idTextDateChoose == tvDateBegin.getId()) {
//            dateBeginL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
//            dateEndL = CommonMethod.convertDateToLong(tvDateEnd.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
//            if (dateBeginL > dateEndL) {
//                tvDateBegin.setText(tvDateEnd.getText().toString());
//                Toast.makeText(getContext(), "Thời điểm ngày bắt đầu phải nhỏ hơn ngày kết thúc.", Toast.LENGTH_SHORT).show();
//            } else
//                tvDateBegin.setText(timeChoose);
//
//        } else {
//            dateBeginL = CommonMethod.convertDateToLong(tvDateBegin.getText().toString(), TYPE_DATE_TIME_DD_MM_YYYY);
//            dateEndL = CommonMethod.convertDateToLong(timeChoose, TYPE_DATE_TIME_DD_MM_YYYY);
//            if (dateBeginL > dateEndL) {
//                tvDateEnd.setText(tvDateBegin.getText().toString());
//                Toast.makeText(getContext(), "Thời điểm ngày kết thúc phải lớn hơn ngày bắt đầu.", Toast.LENGTH_SHORT).show();
//            } else
//                tvDateEnd.setText(timeChoose);
//        }
//
//        dateBegin = tvDateBegin.getText().toString();
//        dateEnd = tvDateEnd.getText().toString();
//    }

    public void setDataRecyclerHistoryAndAlarmToday(List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays, STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (historyAndAlarmEventJSONTodays == null)
            return;
        if (typeString == null)
            return;

        this.historyAndAlarmEventJSONTodays = new ArrayList<>();
        this.historyAndAlarmEventJSONTodays.addAll(historyAndAlarmEventJSONTodays);

        if (historyAndAlarmEventJSONTodays.isEmpty())
            return;

        eventAndAlarmTodayAdapter = new EventAndAlarmTodayAdapter(getContext(), this.historyAndAlarmEventJSONTodays, typeString);
        rvEventAndAlarm.setAdapter(eventAndAlarmTodayAdapter);
        rvEventAndAlarm.invalidate();

    }

    public void refreshData() {
        mListener.showRecyclerFromHistoryEventOrAlarmTodayFragment(type);
    }

    public void setNotifyErrorGetHistoryEventOrAlarm(String message, boolean isResponse, STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (message == null || message.isEmpty())
            return;
        if (typeString == null)
            return;

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnEventOrAlarmTodayFragmentListener {
//        void showDateTimePickerFragmentFromEventOrAlarmFragment(int sIdTextDateChoose);

        void showRecyclerFromHistoryEventOrAlarmTodayFragment(STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);
    }
}
