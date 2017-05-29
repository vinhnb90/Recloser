package esolutions.com.recloser.View.Activity.Interface;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.MobileCountDevice;
import esolutions.com.recloser.Entity.ObjectDetailInfoDevice;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamJSONEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.Model.DeviceAdapter;
import esolutions.com.recloser.Model.NavigationMenuApdater;
import esolutions.com.recloser.Presenter.Interface.IMainPresenter;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.Inteface.ICommonView;

/**
 * Created by VinhNB on 2/18/2017.
 */

public interface IMainView extends ICommonView {
    void visibleNavigationMenu(NavigationMenuApdater mListAdapter);

    void refreshAllDeviceRecycler(DeviceAdapter deviceApdater);

    void refreshDisconnectedRecycler(DeviceAdapter deviceApdater);
//     void visibleFragment(int layout, final Fragment fragment, boolean addToBackStack);

    //do dùng fragment bên trong activity nên phải vận chuyển qua activity
    void showDashboardFragment(String user);

    void showConnectedFragment(String user);

    void showDisconnectedFragment(String user);

    void showEventOrAlarmFragment(String user, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY event);

    void showAllDeviceFragment(String user);

    void showTrendsFragment(String user);

    void showHistoryAlarmFragment(String user);

    void showUpdateInfoFragment(String user);

    void showDeviceDetailFragment(DeviceEntity deviceEntity);

    void showHistoryDeviceFragment(int idDevice);

//     void visibileAllDeviceRecycler(DeviceApdater deviceApdater) throws Exception;

    //phục vụ cho các fragmentView bên trong activity
    IMainPresenter getMainPresenter();

    void showPbarConnect();

    void hidePbarConnect();

    void showTitleFeature(String title);

    void showUpdatePassFragment(String userName);

    void responseDataToDashboard(@Nullable MobileCountDevice countDevice);

    void responseDataToUpdateInfo(ResponseServerLoginJSON responseServerLoginJSON);

    void responseDataInfoUpdate(InfoEntity infoEntity);

    void responseDataUpdatePass(ResponseServerLoginJSON responseServerLoginJSON);

    void responseDataConnected(List<DeviceOnOffEntity> deviceOnOffEntity);

    void responseDataDisconnected(List<DeviceOnOffEntity> deviceOnOffEntity);

    void responseDataGetAllDevice(List<DeviceEntity> deviceEntities);

    void responseDataDetailDevice(ObjectDetailInfoDevice objectDetailInfoDevice);

    void responseDataGetLinePramHistoryDevice(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> listPivotX);

    void responseDataSpinnerParamDevice(List<ObjectSpinnerParamJSONEntity> entityList);

    void responseDataGetRecyclerParamDevice(List<ObjectParamaterDeviceJSON> entityList);

    void responseDataRecyclerEventDevice(List<ObjectEventDevice> entityList);

    void responseDataGetAllDeviceTrends(List<DeviceEntity> deviceEntity);

    void responseNotifyErrorGetInfoDetailDevice(String message, boolean isResponse);

    void responseNotifyErrorGetSpinnerParamDetailDevice(String message, boolean isResponse);

    void responseNotifyErrorGetRecyclerParamDetailDevice(String message, boolean isResponse);

    void responseNotifyErrorRecyclerEventDetailDevice(String message, boolean isResponse);

    void responseNotifyErrorGetLineParamChartDetailDevice(String message, boolean isResponse);

    void responseNotifyErrorCountDeviceOnOff(String message, boolean isResponse);

    void responseNotifyErrorUpdateInfo(String message, boolean isResponse);

    void responseNotifyErrorUpdatePass(String message, boolean isResponse);

    void responseNotifyErrorUpdateDeviceOnOff(String message, boolean isResponse);

    void responseNotifyErrorGetAllDevice(String message, boolean isResponse);

    void responseNotifyErrorGetInfo(String message, boolean isResponse);

    void responseNotifyErrorGetAllDeviceTrends(String message, boolean isResponse);

    void responseNotifyErrorGetAllParamHistoryTrends(String message, boolean isResponse);

    void responseDataChartTrends(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> mAllPivotX);

    void responseDataRecyclerTrends(List<ParamHistoryEntity> paramHistoryEntityList);

    void responseNotifyErrorGetAllDeviceHistoryAndAlarm(String message, boolean isResponse);

    void responseDataGetAllDeviceHistoryAndAlarm(List<DeviceEntity> deviceEntities);

    void responseNotifyErrorGetHistoryAndAlarmRecycler(String message, boolean isResponse, String typeString);

    void responseDataGetHistoryAndAlarmRecycler(List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs, String typeString);

    void responseNotifyErrorGetHistoryAndAlarmRecyclerToday(String message, boolean isResponse, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);

    void responseDataGetHistoryAndAlarmRecyclerToday(List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);

    void refreshEventAndAlarmTodayFragment(String userName);

    void refreshHistoryDetailFragment(String userName);

    void refreshDeviceDetailFragment(String userName);

    void refreshTrendsFragment(String userName);

    void refreshHistoryAlarmFragment(String userName);

    void responseNotifyErrorGetHistoryDetailDevice(String message, boolean isResponse);

    void responseDataGetHistoryDetailDeviceRecycler(List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs);

}
