package esolutions.com.recloser.View.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import esolutions.com.recloser.Entity.MobileCountDevice;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseV4Fragment {
    private OnDashboardFragmentListener listener;
    private String mUser;

    private LinearLayout mLLConnected, mLLDisconnected, mLLEventValue, mLLAlarmValue;
    private TextView mTvConnect, mTvDisconnected, mTvOverValue, mTvUnderValue;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String user) {
        if (user == null || user.isEmpty())
            return null;
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Define.PARAM_NAME_USER, user);
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardFragmentListener) {
            listener = (OnDashboardFragmentListener) context;
        } else {
            throw new ClassCastException(context.getClass().getName()
                    + " must implement OnDashboardFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getString(Define.PARAM_NAME_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViewV4Fragment(rootView);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);

        return rootView;
    }


    @Override
    protected void initViewV4Fragment(View view) {
        mLLConnected = (LinearLayout) view.findViewById(R.id.ll_fragment_dashboard_connected);
        mLLDisconnected = (LinearLayout) view.findViewById(R.id.ll_fragment_dashboard_disconnected);
        mLLEventValue = (LinearLayout) view.findViewById(R.id.ll_fragment_dashboard_event_value);
        mLLAlarmValue = (LinearLayout) view.findViewById(R.id.ll_fragment_dashboard_larm_value);
        mTvConnect = (TextView) view.findViewById(R.id.tv_fragment_dashboard_connected);
        mTvDisconnected = (TextView) view.findViewById(R.id.tv_fragment_dashboard_disconnected);
        mTvOverValue = (TextView) view.findViewById(R.id.tv_fragment_dashboard_over_value);
        mTvUnderValue = (TextView) view.findViewById(R.id.tv_fragment_dashboard_under_value);
    }

    @Override
    protected void initSoureV4Fragment() {
    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        mLLConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showConnectedFragmentFromDashboard(mUser);
            }
        });

        mLLDisconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showDisconnectedFragmentFromDashboard(mUser);
            }
        });

        mLLEventValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showEventValueFragmentFromDashboard(mUser, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY.EVENT);
            }
        });

        mLLAlarmValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showEventValueFragmentFromDashboard(mUser, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY.ALARM);
            }
        });

        listener.getJSONMobileCountDevice();
    }

    public void refreshCountDeviceText(MobileCountDevice countDevice) {
        if (countDevice != null) {
            mTvConnect.setText(String.valueOf(countDevice.getConnected()));
            mTvDisconnected.setText(String.valueOf(countDevice.getDisconnected()));
            mTvOverValue.setText(String.valueOf(countDevice.getOvervalue()));
            mTvUnderValue.setText(String.valueOf(countDevice.getUndervalue()));
        }

    }
    //endregion

    public interface OnDashboardFragmentListener {
        void showConnectedFragmentFromDashboard(String user);

        void showDisconnectedFragmentFromDashboard(String user);

        void showEventValueFragmentFromDashboard(String user, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY type);

        void getJSONMobileCountDevice();
    }

}
