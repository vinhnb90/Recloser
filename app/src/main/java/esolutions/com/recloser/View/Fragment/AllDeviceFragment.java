package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Model.DeviceAdapter;
import esolutions.com.recloser.Model.DeviceOnOffAdapter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;

public class AllDeviceFragment extends BaseV4Fragment {
    private OnAllDeviceFragmentListener mListener;
    private RecyclerView mRecyclerViewDevice;
    private DeviceAdapter mDeviceApdater;
    private String mUser;

    public AllDeviceFragment() {
        // Required empty public constructor
    }

    public static AllDeviceFragment newInstance(String mUser) {
        AllDeviceFragment allDeviceFragment = new AllDeviceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Define.PARAM_NAME_USER, mUser);
        allDeviceFragment.setArguments(bundle);
        return allDeviceFragment;
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_device, container, false);
        initViewV4Fragment(rootView);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAllDeviceFragmentListener)
            mListener = (OnAllDeviceFragmentListener) context;
        else
            throw new ClassCastException(context.getClass().getName() + " must implement OnAllDeviceFragmentListener2!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(View view) {
        mRecyclerViewDevice = (RecyclerView) view.findViewById(R.id.recycleview_fragment_all_device);
    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewDevice.setHasFixedSize(true);
        mRecyclerViewDevice.setLayoutManager(linearLayoutManager);
        mListener.showDeviceRecycler();
    }


    public void fillDataAllDevice(List<DeviceEntity> deviceEntities) {
        if (deviceEntities == null)
            return;
        if (mDeviceApdater == null) {
            mDeviceApdater = new DeviceAdapter(getContext(), deviceEntities);
        } else {
            mDeviceApdater.refreshAdapter(deviceEntities);
        }
        mRecyclerViewDevice.setAdapter(mDeviceApdater);
        mRecyclerViewDevice.invalidate();
    }

    public interface OnAllDeviceFragmentListener {
        void showDeviceRecycler();
    }
}
