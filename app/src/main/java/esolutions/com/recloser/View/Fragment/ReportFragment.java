package esolutions.com.recloser.View.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esolutions.com.recloser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends BaseV4Fragment {

    private IReportFragmentView mListener;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment reportFragment = new ReportFragment();
        return reportFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IReportFragmentView)
            mListener = (IReportFragmentView) context;
        else
            throw new ClassCastException(context.getClass().getName() + " must implement IReportFragmentView!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    //region imp method

    @Override
    protected void initViewV4Fragment(View view) {

    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {

    }

    //endregion

    public interface IReportFragmentView {

    }
}
