package esolutions.com.recloser.View.Fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

import static esolutions.com.recloser.Utils.Class.Define.PARAM_CALLBACK_DATE_TIME_PICKER;

public class DateTimePickerCustomFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 2000;
    private static NumberPicker sDatePicker, sMonthPicker, sYearPicker;
    private static int sDate, sMonth, sYear;

    private DateTimePickerCustormListenerImp mListener;
    private static Dialog sDialog;

    public DateTimePickerCustomFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DateTimePickerCustomFragment newInstance() {
        DateTimePickerCustomFragment fragment = new DateTimePickerCustomFragment();
        return fragment;
    }

    public void setmListener(DateTimePickerCustormListenerImp mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_date_time_picker_custom, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();

        View viewDialog = inflater.inflate(R.layout.fragment_date_time_picker_custom, null);
        sDatePicker = (NumberPicker) viewDialog.findViewById(R.id.numberPicker_fragment_date_time_picker_custom_date);
        sMonthPicker = (NumberPicker) viewDialog.findViewById(R.id.numberPicker_fragment_date_time_picker_custom_month);
        sYearPicker = (NumberPicker) viewDialog.findViewById(R.id.numberPicker_fragment_date_time_picker_custom_year);

        viewDialog.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.e(Define.TAG, "onKey: ");
                }
                return false;
            }
        });

        sMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Calendar mycal = new GregorianCalendar(sYear, i1 - 1, 1);
                final int maxdateNew = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDatePicker.post(new Runnable() {
                    @Override
                    public void run() {
                        sDatePicker.setMaxValue(maxdateNew);
                    }
                });
            }
        });

        sYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Calendar mycal = new GregorianCalendar(sYear, i1 - 1, 1);
                final int maxdateNew = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDatePicker.post(new Runnable() {
                    @Override
                    public void run() {
                        sDatePicker.setMaxValue(maxdateNew);
                    }
                });
            }
        });

        sDate = cal.get(Calendar.DATE);
        int maxDate = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        sDatePicker.setMinValue(1);
        sDatePicker.setMaxValue(maxDate);
        sDatePicker.setValue(sDate);

        sMonth = cal.get(Calendar.MONTH);
        sMonthPicker.setMinValue(1);
        sMonthPicker.setMaxValue(12);
        sMonthPicker.setValue(sMonth + 1);

        sYear = cal.get(Calendar.YEAR);
        sYearPicker.setMinValue(MIN_YEAR);
        sYearPicker.setMaxValue(MAX_YEAR);
        sYearPicker.setValue(sYear);


        builder.setView(viewDialog)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDateSet(null, sYearPicker.getValue(), sMonthPicker.getValue(), sDatePicker.getValue());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DateTimePickerCustomFragment.this.getDialog().cancel();
                    }
                });
        sDialog = builder.create();

        return sDialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static abstract class DateTimePickerCustormListenerImp implements OnDateSetListener {

    }
}
