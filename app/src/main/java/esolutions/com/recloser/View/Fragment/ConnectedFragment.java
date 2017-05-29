package esolutions.com.recloser.View.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Model.DeviceOnOffAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectedFragment extends BaseV4Fragment{
    private OnConnectedFragmentListener mListener;
    private RecyclerView rvConnected;
    private DeviceOnOffAdapter mDeviceApdater;

    private String mUser;

    public ConnectedFragment() {
        // Required empty public constructor
    }

    public static ConnectedFragment newInstance(String user) throws Exception {
        if (user == null || user.isEmpty())
            throw new Exception("ConnectedFragment newInstance null user.");
        ConnectedFragment connectedFragment = new ConnectedFragment();
        Bundle args = new Bundle();
        args.putString(Define.PARAM_NAME_USER, user);
        connectedFragment.setArguments(args);
        return connectedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mUser = getArguments().getString(Define.PARAM_NAME_USER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectedFragmentListener)
            mListener = (OnConnectedFragmentListener) context;
        else
            throw new ClassCastException(context.getClass().getName() + " must implement OnConnectedFragmentListener!");
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
        View rootView = inflater.inflate(R.layout.fragment_connected, container, false);
        initViewV4Fragment(rootView);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return rootView;
    }

    @Override
    protected void initViewV4Fragment(View view) {
        rvConnected = (RecyclerView) view.findViewById(R.id.rv_fragment_connect);
    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvConnected.setHasFixedSize(true);
        rvConnected.setLayoutManager(linearLayoutManager);
        mListener.showConnectRecycler(mUser);
    }

    public void fillDataConnectedDevice(List<DeviceOnOffEntity> deviceOnOffEntityList) {
        if (deviceOnOffEntityList == null)
            return;
        if (mDeviceApdater == null) {
            mDeviceApdater = new DeviceOnOffAdapter(getContext(), deviceOnOffEntityList);
        } else {
            mDeviceApdater.refreshAdapter(deviceOnOffEntityList);
        }
        rvConnected.setAdapter(mDeviceApdater);
        rvConnected.invalidate();
    }

    public interface OnConnectedFragmentListener {
        void showConnectRecycler(String user);
    }
}



