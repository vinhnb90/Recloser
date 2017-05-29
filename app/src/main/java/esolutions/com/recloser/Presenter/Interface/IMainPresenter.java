package esolutions.com.recloser.Presenter.Interface;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.Spinner;

import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.Inteface.ICommonPresenter;

/**
 * Created by VinhNB on 2/18/2017.
 */

public interface IMainPresenter extends ICommonPresenter {
    void callMenuNavigation();

    void callJSONAllDevice(String user) throws Exception;

    void callJSONDeviceOnOff(String user, Define.STATE_GET_DEVICE stateGetDevice) throws Exception;

    void callUnderValueRecycler();

    void callOverValueRecycler();

    void callRefreshFragment(String fragmentTag);

    void callJSONMobileCountDevice();

    void callCommitInfoUpdate(String user, String fullName, String phone, String email);

    void callGetInfo(String user);

    void callCommitPassUpdate(String user, String currentPass, String newPass, String retypeNewPass);

    void callJSONInfoDetailDevice(String mUser, int idDevice);

    void callJSONParamHistoryToDayChart(Spinner spinner, String mUser, int idDevice);

    void callJSONSpinnerParamDevice(String user, int idDevice);

    //region test line chart
//    void processOnPostExecuteParamHistoryDevice(List<ParamHistoryEntity> paramHistoryEntity, String param);

//    ArrayList<ILineDataSet> setDataLine(String param, List<ParamHistoryEntity> paramHistoryEntity);

    void callJSONRecyclerParamDevice(String user, int idDevice);

    void callJSONRecyclerEventDevice(String user, int idDevice);

    Bitmap callGetAvartarDevice(int deviceID, String deviceName, String deviceAvartar);

    void callGetParamSpinner(List<ObjectSpinnerParamEntity> entityList, int posLineChart);

    void callJSONAllDeviceTrends(String mUser);

    void callJSONAllParamHistoryChartTrends(int idDevice, String nameDevice, String startDate, String endDate);

    void callJSONAllDeviceHistoryAlarm(String mUser);

    void callJSONHistoryAndAlarm(int idDevice, String nameDevice, String typeString, String beginDate, String endDate);

    void callJSONHistoryEventAndAlarmToday(Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);

    void callJSONHistoryDetailDevice(int idDevice, String beginDate, String endDate);

    //endregion
}
