package esolutions.com.recloser.View.Fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String HOLDER_ID_TEXT_DATE_TIME_PICKER_ROTATE = "HOLDER_ID_TEXT_DATE_TIME_PICKER_ROTATE";
    private TextView etNgay;
    private boolean isChooseDate = false;
    private int idTextView = 0;

    public DateTimePickerFragment() {
        // Required empty public constructor
    }

    public static DateTimePickerFragment newInstance(int idTextView) {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(Define.PARAM_ID_TEXT_DATE_TIME_PICKER, idTextView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idTextView = getArguments().getInt(Define.PARAM_ID_TEXT_DATE_TIME_PICKER);
        }

        if (savedInstanceState != null) {
            idTextView = savedInstanceState.getInt(HOLDER_ID_TEXT_DATE_TIME_PICKER_ROTATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(HOLDER_ID_TEXT_DATE_TIME_PICKER_ROTATE, idTextView);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            idTextView = savedInstanceState.getInt(HOLDER_ID_TEXT_DATE_TIME_PICKER_ROTATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        etNgay = (TextView) getActivity().findViewById(idTextView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String d = (dayOfMonth < 10) ? "0" + dayOfMonth : "" + dayOfMonth;
        String m = (monthOfYear + 1 < 10) ? "0" + String.valueOf(monthOfYear + 1) : "" + String.valueOf(monthOfYear + 1);
        etNgay.setText(d + "/" + m + "/" + year);
        isChooseDate = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isChooseDate)
            etNgay.setText("");
    }

    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        Calendar c = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(c.getTime());

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = monthFormat.format(c.getTime());

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(c.getTime());
        int monthReal = Integer.parseInt(month) - 1;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, Integer.parseInt(year), monthReal, Integer.parseInt(date));
    }

}
