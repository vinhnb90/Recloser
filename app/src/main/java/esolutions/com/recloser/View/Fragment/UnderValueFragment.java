package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esolutions.com.recloser.R;

public class UnderValueFragment extends BaseV4Fragment {
    private OnUnderValueFragmentListener mListener;

    public UnderValueFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UnderValueFragment newInstance() {
        UnderValueFragment fragment = new UnderValueFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_under_value, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUnderValueFragmentListener) {
            mListener = (OnUnderValueFragmentListener) context;
        } else {
            throw new RuntimeException(context.getClass().getName()
                    + " must implement OnEventOrAlarmTodayFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(View view) {

    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {

    }

    public interface OnUnderValueFragmentListener {
    }
}
