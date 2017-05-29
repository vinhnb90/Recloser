package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Model.DeviceAdapter;
import esolutions.com.recloser.Model.DeviceOnOffAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

public class DisconnectedFragment extends BaseV4Fragment  {
    private RecyclerView rvDisconnected;
    private OnDisconnectedFragmentListener mListener;
    private DeviceOnOffAdapter mDeviceApdater;
    private String mUser;

    public DisconnectedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisconnectedFragment newInstance(String user) {
        DisconnectedFragment disconnectedFragment = new DisconnectedFragment();
        Bundle args = new Bundle();
        args.putString(Define.PARAM_NAME_USER, user);
        disconnectedFragment.setArguments(args);
        return disconnectedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mUser = getArguments().getString(Define.PARAM_NAME_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disconnected, container, false);
        initViewV4Fragment(rootView);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDisconnectedFragmentListener) {
            mListener = (OnDisconnectedFragmentListener) context;
        } else {
            throw new ClassCastException(context.getClass().getName() + " must implement IDisconnectFragmentView!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshDeviceAdapter(DeviceAdapter deviceApdater) {
        try {
            if (deviceApdater == null)
                return;
            rvDisconnected.setAdapter(deviceApdater);
            rvDisconnected.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initViewV4Fragment(View view) {
        rvDisconnected = (RecyclerView) view.findViewById(R.id.rv_fragment_disconnect);
    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDisconnected.setHasFixedSize(true);
        rvDisconnected.setLayoutManager(linearLayoutManager);
        mListener.showDisconnectRecycler(mUser);
    }

    public void fillDataDisconnectedDevice(List<DeviceOnOffEntity> deviceOnOffEntityList) {
        if (deviceOnOffEntityList == null)
            return;
        if (mDeviceApdater == null) {
            mDeviceApdater = new DeviceOnOffAdapter(getContext(), deviceOnOffEntityList);
        } else {
            mDeviceApdater.refreshAdapter(deviceOnOffEntityList);
        }
        rvDisconnected.setAdapter(mDeviceApdater);
        rvDisconnected.invalidate();
    }

    public interface OnDisconnectedFragmentListener {
        void showDisconnectRecycler(String user);
    }
}
