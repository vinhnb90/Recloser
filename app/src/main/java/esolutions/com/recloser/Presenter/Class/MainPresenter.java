package esolutions.com.recloser.Presenter.Class;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Spinner;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Entity.HeaderNavigationMenuEntity;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.MobileCountDevice;
import esolutions.com.recloser.Entity.ObjectDetailInfoDevice;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectPublishAsyntask;
import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamJSONEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.Model.Class.MainModel;
import esolutions.com.recloser.Model.Inteface.IMainModel;
import esolutions.com.recloser.Model.NavigationMenuApdater;
import esolutions.com.recloser.Presenter.Interface.IMainPresenter;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.View.Activity.Class.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static esolutions.com.recloser.Presenter.Class.ConnectServer.AsyncCallGetRecyclerParamDevice.convertJSONInfoUpdate;
import static esolutions.com.recloser.Presenter.Class.ConnectServer.AsyncCallGetRecyclerParamDevice.convertJSONPassUpdate;
import static esolutions.com.recloser.Utils.Class.Define.TAG;

/**
 * Created by VinhNB on 2/18/2017.
 */

public class MainPresenter implements
        IMainPresenter,
        Parcelable,
        ConnectServer.AsyncCallMobileCountDevice.OnProcessAsyncMobileCountDevice,
        ConnectServer.AsyncCallUpdateInfo.OnProcessAsyncUpdateInfo,
        ConnectServer.AsyncCallGetInfo.OnProcessAsyncGetInfo,
        ConnectServer.AsyncCallUpdatePass.OnProcessAsyncUpdatePass,
        ConnectServer.AsyncCallGetDeviceOnOff.OnProcessAsyncGetDevice,
//        ConnectServer.AsyncCallGetAllDevice.OnProcessAsyncGetAllDevice,
        ConnectServer.AsyncCallGetInfoDetailDevice.OnProcessAsyncGetInfoDetailDevice,
        ConnectServer.AsyncCallGetChartParamHistoryToDayDevice.OnProcessAsyncGetChartParamHistoryDevice,
        ConnectServer.AsyncCallGetSpinnerParamDevice.OnProcessAsyncGetSpinnerParamDevice,
        ConnectServer.AsyncCallGetRecyclerParamDevice.OnProcessAsyncGetRecyclerParamDevice,
        ConnectServer.AsyncCallGetRecyclerEventDevice.OnProcessAsyncGetRecyclerEventDevice {
    private IMainModel mIMainModel;

    private WeakReference<esolutions.com.recloser.View.Activity.Interface.IMainView> mIMainViewWeakReference;
    private NavigationMenuApdater mListAdapter;
    private List<HeaderNavigationMenuEntity> navigationMenuEntities;
    private final String stringIP, userName;

    //region var LineChart
    private static ArrayList<ILineDataSet> mDataSetsTemp;
    private ArrayList<ILineDataSet> mDataSetsDetailDevice;
    private ArrayList<ILineDataSet> mDataSetsTrends;

    private static List<ObjectPivotXEntity> mAllPivotXTemp;
    private List<ObjectPivotXEntity> mAllPivotXDetailDevice;
    private List<ObjectPivotXEntity> mAllPivotXTrends;
    //endregion

    //region var spinner
    List<ObjectSpinnerParamEntity> paramListSpinnerDetailDevice;
    //endregion

    public MainPresenter(esolutions.com.recloser.View.Activity.Interface.IMainView mIMainView) {
        this.mIMainViewWeakReference = new WeakReference<esolutions.com.recloser.View.Activity.Interface.IMainView>(mIMainView);
        mIMainModel = new MainModel(this);
        stringIP = mIMainModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_CONFIG_IP, "");
        userName = mIMainModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "");
    }

    protected MainPresenter(Parcel in) {
        stringIP = in.readString();
        userName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stringIP);
        dest.writeString(userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainPresenter> CREATOR = new Creator<MainPresenter>() {
        @Override
        public MainPresenter createFromParcel(Parcel in) {
            return new MainPresenter(in);
        }

        @Override
        public MainPresenter[] newArray(int size) {
            return new MainPresenter[size];
        }
    };

    //region IMainPresenter
    @Override
    public void callMenuNavigation() {
        if (navigationMenuEntities == null)
            navigationMenuEntities = new ArrayList<>();

        navigationMenuEntities = mIMainModel.getDataNaviMenu();
        mListAdapter = new NavigationMenuApdater(mIMainViewWeakReference.get().getContextView(), navigationMenuEntities);

        // setting list adapter
        mIMainViewWeakReference.get().visibleNavigationMenu(mListAdapter);
    }

    @Override
    public void callUnderValueRecycler() {

    }

    @Override
    public void callOverValueRecycler() {

    }

    @Override
    public void callRefreshFragment(String fragmentTag) {
        if (fragmentTag == null || fragmentTag.isEmpty())
            return;

        if (fragmentTag.equals(Define.FRAGMENT_TAG.DASHBOARD_FRAG.name())) {
            mIMainViewWeakReference.get().showDashboardFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.ALL_DEVICE_FRAG.name())) {
            mIMainViewWeakReference.get().showAllDeviceFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.CONNECTED_FRAG.name())) {
            mIMainViewWeakReference.get().showConnectedFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.DISCONNECTED_FRAG.name())) {
            mIMainViewWeakReference.get().showDisconnectedFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.ALARM_OR_EVENT_VALUE_FRAG.name())) {
            //check fragment visibling and get type
            mIMainViewWeakReference.get().refreshEventAndAlarmTodayFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.TRENDS_FRAG.name())) {
            mIMainViewWeakReference.get().refreshTrendsFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.HISTORY_ALARM_EVENT_FRAG.name())) {
            mIMainViewWeakReference.get().refreshHistoryAlarmFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.DETAIL_DEVICE_FRAG.name())) {
            mIMainViewWeakReference.get().refreshDeviceDetailFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.HISTORY_DETAIL_DEVICE_FRAG.name())) {
            mIMainViewWeakReference.get().refreshHistoryDetailFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.UPDATE_INFO_FRAG.name())) {
            mIMainViewWeakReference.get().showUpdateInfoFragment(userName);
        }
        if (fragmentTag.equals(Define.FRAGMENT_TAG.UPDATE_PASS_FRAG.name())) {
            mIMainViewWeakReference.get().showUpdatePassFragment(userName);
        }

    }

    @Override
    public void callJSONAllDevice(String user) {
        if (user == null || user.isEmpty())
            return;

        ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice entityAllDeviceAsyntaskImp = new ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice() {
            @Override
            void processOnPreExecuteGetAllDevice(final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTest, String user) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallGetAllDeviceTest.cancel(true);
                    asyncCallGetAllDeviceTest.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetAllDevice(ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetAllDevice(objectPublish.getMessage(), objectPublish.isResponse);
            }

            @Override
            void processOnPostExecuteGetAllDevice(List<DeviceEntity> deviceEntities, String userName) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (deviceEntities == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }
                mIMainViewWeakReference.get().responseDataGetAllDevice(deviceEntities);
            }

            @Override
            void processOnCountdownAllDevice(final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTest) {
                asyncCallGetAllDeviceTest.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetAllDeviceTest.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetAllDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                        }
                    }
                });
            }
        };

        final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDevice = new ConnectServer.AsyncCallGetAllDevice(entityAllDeviceAsyntaskImp, stringIP, userName);
        try {
            if (asyncCallGetAllDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetAllDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread mAllDeviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<DeviceEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetAllDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetAllDevice.onCountdown(asyncCallGetAllDevice);
                    }
                }
            }
        });
        mAllDeviceThread.start();
    }

    @Override
    public void callJSONMobileCountDevice() {
        final ConnectServer.AsyncCallMobileCountDevice asyncCallMobileCountDevice = new ConnectServer.AsyncCallMobileCountDevice(this, stringIP, userName);
        try {
            if (asyncCallMobileCountDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallMobileCountDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread mCountDeviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                MobileCountDevice result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallMobileCountDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallMobileCountDevice.onCountdown(asyncCallMobileCountDevice);
                    }
                }
            }
        });
        mCountDeviceThread.start();
    }

    @Override
    public void callCommitInfoUpdate(String user, String fullName, String phone, String email) {
        try {
            if (user == null || user.isEmpty())
                return;
            if (fullName == null || fullName.isEmpty())
                return;
            if (phone == null || phone.isEmpty())
                return;
            if (email == null || email.isEmpty())
                return;

            JSONObject jsonObject = convertJSONInfoUpdate(user, fullName, phone, email);

            final ConnectServer.AsyncCallUpdateInfo asyncCallUpdateInfo = new ConnectServer.AsyncCallUpdateInfo(this, stringIP, userName);
            if (asyncCallUpdateInfo.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallUpdateInfo.execute(jsonObject);
            }
        } catch (Exception e) {
            DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mIMainViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật thông tin thất bại!\nNội dung: " + e.getMessage()).build();
            mIMainViewWeakReference.get().showDialogMessage(dialogMessageEntity);
        }
    }

    @Override
    public void callGetInfo(String user) {
        if (user == null || user.isEmpty())
            return;

        final ConnectServer.AsyncCallGetInfo asyncCallGetInfo = new ConnectServer.AsyncCallGetInfo(this, stringIP, userName);
        try {
            if (asyncCallGetInfo.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetInfo.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetInfoThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                InfoEntity result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetInfo.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetInfo.onCountdown(asyncCallGetInfo);
                    }
                }
            }
        });
        mGetInfoThread.start();

    }

    @Override
    public void callJSONDeviceOnOff(String user, Define.STATE_GET_DEVICE stateGetDevice) throws Exception {
        if (user == null || user.isEmpty())
            return;
        if (stateGetDevice == null)
            return;

        final ConnectServer.AsyncCallGetDeviceOnOff asyncCallGetDeviceOnOff = new ConnectServer.AsyncCallGetDeviceOnOff(this, stringIP, userName, stateGetDevice);

        try {
            if (asyncCallGetDeviceOnOff.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetDeviceOnOff.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetDeviceOnOffThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                List<DeviceOnOffEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetDeviceOnOff.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetDeviceOnOff.onCountdown(asyncCallGetDeviceOnOff);
                    }
                }
            }
        });
        mGetDeviceOnOffThread.start();
    }

    @Override
    public void callCommitPassUpdate(String user, String currentPass, String newPass, String retypeNewPass) {
        try {
            if (user == null || user.isEmpty())
                return;
            if (currentPass == null || currentPass.isEmpty())
                return;
            if (newPass == null || newPass.isEmpty())
                return;
            if (retypeNewPass == null || retypeNewPass.isEmpty())
                return;

            JSONObject jsonObject = convertJSONPassUpdate(user, currentPass, newPass, retypeNewPass);

            final ConnectServer.AsyncCallUpdatePass asyncCallUpdatePass = new ConnectServer.AsyncCallUpdatePass(this, stringIP, userName);
            if (asyncCallUpdatePass.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallUpdatePass.execute(jsonObject);
            }
        } catch (Exception e) {
            DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mIMainViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật thông tin thất bại!\nNội dung: " + e.getMessage()).build();
            mIMainViewWeakReference.get().showDialogMessage(dialogMessageEntity);
        }
    }

    @Override
    public void callJSONInfoDetailDevice(String user, int idDevice) {
        if (idDevice <= 0)
            return;

        if (user == null || user.isEmpty())
            return;

        final ConnectServer.AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice = new ConnectServer.AsyncCallGetInfoDetailDevice(this, stringIP, userName, idDevice);

        try {
            if (asyncCallGetInfoDetailDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetInfoDetailDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetDetailDeviceThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                ObjectDetailInfoDevice result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetInfoDetailDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetDeviceOnOffThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetInfoDetailDevice.onCountdown(asyncCallGetInfoDetailDevice);
                    }
                }
            }
        });
        mGetDetailDeviceThread.start();
    }

    @Override
    public void callJSONParamHistoryToDayChart(final Spinner spinner, String user, int idDevice) {
        if (spinner == null)
            return;

        if (user == null || user.isEmpty())
            return;

        if (idDevice <= 0)
            return;

        if (paramListSpinnerDetailDevice == null || paramListSpinnerDetailDevice.size() == 0)
            return;

        //merger chuỗi param cùng nhau
        int indexParam = 1;
        StringBuilder params = new StringBuilder();
        for (indexParam = 1; indexParam < paramListSpinnerDetailDevice.size(); indexParam++) {
            if (paramListSpinnerDetailDevice.get(indexParam).ismSelected()) {
                params.append(paramListSpinnerDetailDevice.get(indexParam).getParamDescription());
                params.append(Define.SYMBOL_PARAM_URL);
            }
        }

        //xóa kí tự SYMBOL_PARAM_URL cuối
        params.replace(params.length() - 1, params.length(), "");

        final ConnectServer.AsyncCallGetChartParamHistoryToDayDevice asyncCallGetChartParamHistoryToDayDevice = new ConnectServer.AsyncCallGetChartParamHistoryToDayDevice(this, stringIP, userName, idDevice, params.toString());

        try {
            if (asyncCallGetChartParamHistoryToDayDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetChartParamHistoryToDayDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetParamHistoryDeviceThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                List<ParamHistoryEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetChartParamHistoryToDayDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetChartParamHistoryToDayDevice.onCountdown(asyncCallGetChartParamHistoryToDayDevice);
                    }
                }
            }
        });
        mGetParamHistoryDeviceThread.start();
    }

    @Override
    public void callJSONSpinnerParamDevice(String user, int idDevice) {
        if (user == null || user.isEmpty())
            return;

        if (idDevice <= 0)
            return;

        final ConnectServer.AsyncCallGetSpinnerParamDevice asyncCallGetSpinnerParamDevice = new ConnectServer.AsyncCallGetSpinnerParamDevice(this, stringIP, userName, idDevice);

        try {
            if (asyncCallGetSpinnerParamDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetSpinnerParamDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetSpinnerParamDeviceThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                List<ObjectSpinnerParamJSONEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetSpinnerParamDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetSpinnerParamDevice.onCountdown(asyncCallGetSpinnerParamDevice);
                    }
                }
            }
        });
        mGetSpinnerParamDeviceThread.start();
    }

    @Override
    public void callJSONRecyclerParamDevice(String user, int idDevice) {
        if (user == null || user.isEmpty())
            return;

        if (idDevice <= 0)
            return;

        final ConnectServer.AsyncCallGetRecyclerParamDevice asyncCallGetRecyclerParamDevice = new ConnectServer.AsyncCallGetRecyclerParamDevice(this, stringIP, userName, idDevice);

        try {
            if (asyncCallGetRecyclerParamDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetRecyclerParamDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetRecyclerParamDeviceThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                List<ObjectParamaterDeviceJSON> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetRecyclerParamDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetRecyclerParamDevice.onCountdown(asyncCallGetRecyclerParamDevice);
                    }
                }
            }
        });
        mGetRecyclerParamDeviceThread.start();
    }

    @Override
    public void callJSONRecyclerEventDevice(String user, int idDevice) {
        if (user == null || user.isEmpty())
            return;

        if (idDevice <= 0)
            return;

        final ConnectServer.AsyncCallGetRecyclerEventDevice asyncCallGetRecyclerEventDevice = new ConnectServer.AsyncCallGetRecyclerEventDevice(this, stringIP, userName, idDevice);

        try {
            if (asyncCallGetRecyclerEventDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetRecyclerEventDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
            return;
        }

        Thread mGetRecyclerEventDeviceThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                List<ObjectEventDevice> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetRecyclerEventDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetParamHistoryDeviceThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetRecyclerEventDevice.onCountdown(asyncCallGetRecyclerEventDevice);
                    }
                }
            }
        });
        mGetRecyclerEventDeviceThread.start();
    }

    @Override
    public Bitmap callGetAvartarDevice(int deviceID, String deviceName, String deviceAvartar) {
        if (deviceID <= 0)
            return null;

        if (deviceAvartar == null || deviceAvartar.isEmpty())
            return null;

        //TODO image Name:= {user}_{deviceName}.jpg
        String imageName = userName + "_" + deviceName + ".jpg";

        //lấy bitmap từ sdcard
        Bitmap bitmap = mIMainModel.getAvatarDevice(imageName);
        if (bitmap == null) {
            bitmap = CommonMethod.convertByte64ToBitmap(deviceAvartar);
            CommonMethod.saveBitmap(imageName, bitmap);
        }
        return bitmap;
    }

    @Override
    public void callGetParamSpinner(List<ObjectSpinnerParamEntity> entityList, int posLineChart) {
        if (entityList == null)
            return;
        if (posLineChart < 0 || posLineChart > entityList.size() - 1)
            return;

        if (paramListSpinnerDetailDevice == null)
            paramListSpinnerDetailDevice = new ArrayList<>();

        paramListSpinnerDetailDevice.clear();
        paramListSpinnerDetailDevice.addAll(entityList);
    }

    @Override
    public void callJSONAllDeviceTrends(String user) {
        if (user == null || user.isEmpty())
            return;

        ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice entityTrendsAsyntaskImp = new ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice() {
            @Override
            void processOnPreExecuteGetAllDevice(ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTest, String user) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallGetAllDeviceTest.cancel(true);
                    asyncCallGetAllDeviceTest.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetAllDevice(ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceTrends(objectPublish.getMessage(), objectPublish.isResponse);
            }

            @Override
            void processOnPostExecuteGetAllDevice(List<DeviceEntity> deviceEntities, String userName) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (deviceEntities == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }
                mIMainViewWeakReference.get().responseDataGetAllDeviceTrends(deviceEntities);
            }

            @Override
            void processOnCountdownAllDevice(final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTest) {
                asyncCallGetAllDeviceTest.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetAllDeviceTest.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                        }
                    }
                });
            }
        };

        final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTrends = new ConnectServer.AsyncCallGetAllDevice(entityTrendsAsyntaskImp, stringIP, userName);
        try {
            if (asyncCallGetAllDeviceTrends.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetAllDeviceTrends.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread mAllDeviceTrendsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<DeviceEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetAllDeviceTrends.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetAllDeviceTrends.onCountdown(asyncCallGetAllDeviceTrends);
                    }
                }
            }
        });
        mAllDeviceTrendsThread.start();
    }

    @Override
    public void callJSONAllParamHistoryChartTrends(int idDevice, String nameDevice, String startDate, String endDate) {
        if (idDevice <= 0)
            return;
        if (nameDevice == null || nameDevice.isEmpty())
            return;
        if (startDate == null || startDate.isEmpty())
            return;
        if (endDate == null || endDate.isEmpty())
            return;

        ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends.ProcessAsyncGetChartParamHistoryDevice process = new ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends.ProcessAsyncGetChartParamHistoryDevice() {
            @Override
            void processOnPreExecuteGetLineChartParamHistoryDevice(final ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDevice) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallGetChartParamHistoryDevice.cancel(true);
                    asyncCallGetChartParamHistoryDevice.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetLineChartParamHistoryDevice(ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends(objectPublish.getMessage(), objectPublish.isResponse);
            }

            @Override
            void processOnPostExecuteLineChartParamHistoryDeviceTrends(final List<ParamHistoryEntity> paramHistoryEntityList, int idDevice, String nameDevice, final String dateBegin, final String dateEnd) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (idDevice <= 0)
                    return;
                if (nameDevice == null || nameDevice.isEmpty()) {
                    return;
                }
                if (paramHistoryEntityList == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }

                if (paramHistoryEntityList.size() == 0)
                {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không có dữ liệu mới!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //sắp xếp theo description
                        final List<ParamHistoryEntity> paramHistoryEntityListClone = new ArrayList<>();
                        paramHistoryEntityListClone.addAll(paramHistoryEntityList);
                        Comparator<ParamHistoryEntity> rankOrder = new Comparator<ParamHistoryEntity>() {
                            @Override
                            public int compare(ParamHistoryEntity paramHistoryEntity, ParamHistoryEntity t1) {
                                int result = paramHistoryEntity.getDescription().compareTo(t1.getDescription());
                                return result;
                            }
                        };
                        Collections.sort(paramHistoryEntityListClone, rankOrder);

                        //lọc các parram ra
                        String param = paramHistoryEntityListClone.get(0).getDescription();
                        StringBuilder params = new StringBuilder(param);
                        for (int index = 0; index < paramHistoryEntityListClone.size(); index++) {
                            if (param.compareTo(paramHistoryEntityListClone.get(index).getDescription()) != 0) {
                                param = paramHistoryEntityListClone.get(index).getDescription();
                                params.append(Define.SYMBOL_PARAM_URL);
                                params.append(param);
                            }
                        }

                        //cắt kí tự thừa
                        String[] arrayParam = params.toString().split(Define.SYMBOL_PARAM_URL);

                        String timeEnd = CommonMethod.getBeginTimeOfDay(dateEnd, Define.TYPE_DATE_TIME_YYYY_MM_DD);
                        String timeBegin = CommonMethod.getBeginTimeOfDay(dateBegin, Define.TYPE_DATE_TIME_YYYY_MM_DD);

                        setDataLineMutilParam(arrayParam, paramHistoryEntityList, Define.TIME_BETWEEN_PIVOT_CHART_TRENDS, timeBegin, timeEnd, Define.TYPE_DATE_TIME_FULL_TYPE_2);
                        if (mDataSetsTemp == null || mAllPivotXTemp == null) {
                            mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends("Lỗi khi khởi tạo các giá trị tọa độ biểu đồ.", false);
                            return;
                        }

                        mDataSetsTrends = new ArrayList<>();
                        mDataSetsTrends.addAll(mDataSetsTemp);
                        mDataSetsTemp.clear();

                        mAllPivotXTrends = new ArrayList<>();
                        mAllPivotXTrends.addAll(mAllPivotXTemp);
                        mAllPivotXTemp.clear();

                        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIMainViewWeakReference.get().hidePbarConnect();
                                mIMainViewWeakReference.get().responseDataChartTrends(mDataSetsTrends, (ArrayList<ObjectPivotXEntity>) mAllPivotXTrends);
                                mIMainViewWeakReference.get().responseDataRecyclerTrends(paramHistoryEntityList);
                            }
                        });
                    }
                });
                thread.start();

            }

            @Override
            void processOnCountdownLineChartParamHistoryDevice(final ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDevice) {
                asyncCallGetChartParamHistoryDevice.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetChartParamHistoryDevice.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetAllParamHistoryTrends(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                        }
                    }
                });
            }
        };

        final ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDeviceTrends = new ConnectServer.AsyncCallGetChartParamHistoryDeviceTrends(process, stringIP, idDevice, nameDevice, startDate, endDate);
        try {
            if (asyncCallGetChartParamHistoryDeviceTrends.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetChartParamHistoryDeviceTrends.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread countdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<ParamHistoryEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetChartParamHistoryDeviceTrends.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetChartParamHistoryDeviceTrends.onCountdown(asyncCallGetChartParamHistoryDeviceTrends);
                    }
                }
            }
        });
        countdownThread.start();
    }

    @Override
    public void callJSONAllDeviceHistoryAlarm(String mUser) {
        if (mUser == null || mUser.isEmpty())
            return;

        ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice entityAllDeviceHistoryAndAlarmAsyntaskImp = new ConnectServer.AsyncCallGetAllDevice.ProcessAsyncGetAllDevice() {
            @Override
            void processOnPreExecuteGetAllDevice(final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceTest, String user) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceHistoryAndAlarm(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallGetAllDeviceTest.cancel(true);
                    asyncCallGetAllDeviceTest.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetAllDevice(ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceHistoryAndAlarm(objectPublish.getMessage(), objectPublish.isResponse);
            }

            @Override
            void processOnPostExecuteGetAllDevice(List<DeviceEntity> deviceEntities, String userName) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (deviceEntities == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceHistoryAndAlarm(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }

                if (deviceEntities.size() == 0) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không có dữ liệu mới!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceHistoryAndAlarm(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }

                mIMainViewWeakReference.get().responseDataGetAllDeviceHistoryAndAlarm(deviceEntities);
            }

            @Override
            void processOnCountdownAllDevice(final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceHistoryAndAlarm) {
                asyncCallGetAllDeviceHistoryAndAlarm.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetAllDeviceHistoryAndAlarm.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetAllDeviceHistoryAndAlarm(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                        }
                    }
                });
            }
        };


        final ConnectServer.AsyncCallGetAllDevice asyncCallGetAllDeviceHistoryAndAlarm = new ConnectServer.AsyncCallGetAllDevice(entityAllDeviceHistoryAndAlarmAsyntaskImp, stringIP, userName);
        try {
            if (asyncCallGetAllDeviceHistoryAndAlarm.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetAllDeviceHistoryAndAlarm.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread mAllDeviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<DeviceEntity> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetAllDeviceHistoryAndAlarm.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetAllDeviceHistoryAndAlarm.onCountdown(asyncCallGetAllDeviceHistoryAndAlarm);
                    }
                }
            }
        });
        mAllDeviceThread.start();
    }

    @Override
    public void callJSONHistoryAndAlarm(int idDevice, String nameDevice, String typeString, String beginDate, String endDate) {
        if (idDevice <= 0)
            return;
        if (nameDevice == null || nameDevice.isEmpty())
            return;
        if (typeString == null || typeString.isEmpty())
            return;
        if (beginDate == null || beginDate.isEmpty())
            return;
        if (endDate == null || endDate.isEmpty())
            return;

        ConnectServer.AsyncCallGetHistoryAndAlarmEvent.ProcessAsyncGetHistoryAlarm processAsyncGetHistoryAlarm = new ConnectServer.AsyncCallGetHistoryAndAlarmEvent.ProcessAsyncGetHistoryAlarm() {
            @Override
            void processOnPreExecuteGetHistoryAlarm(ConnectServer.AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent, String typeString) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);

                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecycler(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                    asyncCallGetHistoryAndAlarmEvent.cancel(true);
                    asyncCallGetHistoryAndAlarmEvent.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetHistoryAlarm(String typeString, ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecycler(objectPublish.getMessage(), objectPublish.isResponse, typeString);
            }

            @Override
            void processOnPostExecuteGetHistoryAlarm(List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs, int idDevice, String nameDevice, String typeString, String dateBegin, String dateEnd) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (historyAndAlarmEventJSONs == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecycler(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                    return;
                }
                mIMainViewWeakReference.get().responseDataGetHistoryAndAlarmRecycler(historyAndAlarmEventJSONs, typeString);
            }

            @Override
            void processOnCountdownGetHistoryAlarm(final ConnectServer.AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent, final String typeString) {
                asyncCallGetHistoryAndAlarmEvent.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetHistoryAndAlarmEvent.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecycler(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                        }
                    }
                });
            }
        };


        final ConnectServer.AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent = new ConnectServer.AsyncCallGetHistoryAndAlarmEvent(processAsyncGetHistoryAlarm, stringIP, idDevice, nameDevice, typeString, beginDate, endDate);
        try {
            if (asyncCallGetHistoryAndAlarmEvent.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetHistoryAndAlarmEvent.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread mAllDeviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<HistoryAndAlarmEventJSON> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetHistoryAndAlarmEvent.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: mGetInfoThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetHistoryAndAlarmEvent.onCountdown(asyncCallGetHistoryAndAlarmEvent);
                    }
                }
            }
        });
        mAllDeviceThread.start();
    }

    @Override
    public void callJSONHistoryEventAndAlarmToday(Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (typeString == null)
            return;

        ConnectServer.AsyncCallGetHistoryAndAlarmEventToday.ProcessAsyncCallGetHistoryAndAlarmEventToday processAsyncCallGetHistoryAndAlarmEventToday = new ConnectServer.AsyncCallGetHistoryAndAlarmEventToday.ProcessAsyncCallGetHistoryAndAlarmEventToday() {
            @Override
            void processOnPreExecuteGetHistoryAlarm(final ConnectServer.AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);

                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecyclerToday(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                    asyncCallGetHistoryAndAlarmEventToday.cancel(true);
                    asyncCallGetHistoryAndAlarmEventToday.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetHistoryAlarm(Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString, ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecyclerToday(objectPublish.getMessage(), objectPublish.isResponse, typeString);
            }

            @Override
            void processOnPostExecuteGetHistoryAlarm(List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (historyAndAlarmEventJSONTodays == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecyclerToday(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                    return;
                }


                if (historyAndAlarmEventJSONTodays.size() == 0) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không có dữ liệu!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecyclerToday(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                    return;
                }

                mIMainViewWeakReference.get().responseDataGetHistoryAndAlarmRecyclerToday(historyAndAlarmEventJSONTodays, typeString);
            }

            @Override
            void processOnCountdownGetHistoryAlarm(final ConnectServer.AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday, final Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
                asyncCallGetHistoryAndAlarmEventToday.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetHistoryAndAlarmEventToday.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetHistoryAndAlarmRecyclerToday(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse, typeString);
                        }
                    }
                });
            }
        };


        final ConnectServer.AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday = new ConnectServer.AsyncCallGetHistoryAndAlarmEventToday(processAsyncCallGetHistoryAndAlarmEventToday,userName, stringIP, typeString);
        try {
            if (asyncCallGetHistoryAndAlarmEventToday.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetHistoryAndAlarmEventToday.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<HistoryAndAlarmEventJSONToday> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetHistoryAndAlarmEventToday.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryAndAlarmEventToday " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryAndAlarmEventToday " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryAndAlarmEventToday " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryAndAlarmEventToday " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetHistoryAndAlarmEventToday.onCountdown(asyncCallGetHistoryAndAlarmEventToday);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void callJSONHistoryDetailDevice(int idDevice, String beginDate, String endDate) {
        if (idDevice <= 0)
            return;
        if (beginDate == null || beginDate.isEmpty())
            return;
        if (endDate == null || endDate.isEmpty())
            return;

        ConnectServer.AsyncCallGetHistoryDetailDevice.ProcessAsyncCallGetHistoryDetailDevice processAsyncCallGetHistoryDetailDevice = new ConnectServer.AsyncCallGetHistoryDetailDevice.ProcessAsyncCallGetHistoryDetailDevice() {
            @Override
            void processOnPreExecuteGetHistoryDetailDevice(final ConnectServer.AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice) {
                mIMainViewWeakReference.get().showPbarConnect();
                //check wifi
                boolean isHasWifi = CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView());
                if (!isHasWifi) {
                    mIMainViewWeakReference.get().hidePbarConnect();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);

                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallGetHistoryDetailDevice.cancel(true);
                    asyncCallGetHistoryDetailDevice.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            void processOnProgressUpdateGetHistoryDetailDevice(ObjectPublishAsyntask... values) {
                //TODO set UI
                mIMainViewWeakReference.get().hidePbarConnect();
                ObjectPublishAsyntask objectPublish = values[0];
                mIMainViewWeakReference.get().responseNotifyErrorGetHistoryDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);
            }

            @Override
            void processOnPostExecuteGetHistoryDetailDevice(List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs, int idDevice, String dateBegin, String dateEnd) {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (userName == null || userName.isEmpty())
                    return;

                if (historyDetailDeviceJSONs == null) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetHistoryDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }
                mIMainViewWeakReference.get().responseDataGetHistoryDetailDeviceRecycler(historyDetailDeviceJSONs);
            }

            @Override
            void processOnCountdownGetHistoryDetailDevice(final ConnectServer.AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice) {
                asyncCallGetHistoryDetailDevice.cancel(true);
                //thread call asyntask is running. must call in other thread to update UI
                ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainViewWeakReference.get().hidePbarConnect();
                        if (!asyncCallGetHistoryDetailDevice.ismHasReveicedResponseServer()) {
                            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                            objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                            objectPublishAsyntask.setResponse(true);
                            mIMainViewWeakReference.get().responseNotifyErrorGetHistoryDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                        }
                    }
                });
            }

        };

        final ConnectServer.AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice = new ConnectServer.AsyncCallGetHistoryDetailDevice(processAsyncCallGetHistoryDetailDevice, stringIP, idDevice, beginDate, endDate);
        try {
            if (asyncCallGetHistoryDetailDevice.getStatus() != AsyncTask.Status.RUNNING) {
                asyncCallGetHistoryDetailDevice.execute();
            }
        } catch (Exception e) {
            Log.e(TAG, "callJSONMobileCountDevice: countDevice null: \n" + e.getMessage());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<HistoryDetailDeviceJSON> result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallGetHistoryDetailDevice.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryDetailDevice " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryDetailDevice " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryDetailDevice " + e.getMessage());
                } catch (Exception e) {
                    Log.e(ContentValues.TAG, "run: AsyncCallGetHistoryDetailDevice " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallGetHistoryDetailDevice.onCountdown(asyncCallGetHistoryDetailDevice);
                    }
                }
            }
        });
        thread.start();
    }

//
//    private ArrayList<ILineDataSet> setAllDataLineDevice(int idDevice, String nameDevice, List<ParamHistoryEntity> paramHistoryEntityList) {
//        if (idDevice <= 0)
//            return null;
//        if (nameDevice == null || nameDevice.isEmpty()) {
//            return null;
//        }
//        if (paramHistoryEntityList == null) {
//            return null;
//        }
//
//        if (mAllPivotXTrends == null)
//            mAllPivotXTrends = new ArrayList<>();
//
//        if (mDataSetsTrends == null)
//            mDataSetsTrends = new ArrayList<>();
//        mDataSetsTrends.clear();
//
//        //lấy tọa độ lớn nhất và nhỏ nhất
//        long maxPivotAnchor = 0;
//        long minPivotAnchor = 0;
//
//        //mỗi param ta tạo 1 line cho nó
//        String[] arrayParam = new String[4];
////                paramSet.split(Define.SYMBOL_PARAM_URL);
//        ArrayList<String> paramNotHasData = new ArrayList<>();
//
//        for (int indexParamList = 0; indexParamList < arrayParam.length; indexParamList++) {
//            //lọc
//            List<ParamHistoryEntity> params = new ArrayList<>();
//            for (int indexParamHistory = 0; indexParamHistory < paramHistoryEntityList.size(); indexParamHistory++) {
//                String paramHistoryEntity1 = paramHistoryEntityList.get(indexParamHistory).getDescription();
//                String arrayParam1 = arrayParam[indexParamList];
//                if (paramHistoryEntity1.equals(arrayParam1)) {
//                    params.add(paramHistoryEntityList.get(indexParamHistory));
//                }
//            }
//
//            //Lấy đơn vị để vẽ lên điểm tọa độ Y
//            final String param = arrayParam[indexParamList];
//            final String unit;
//            if (params.size() > 0)
//                unit = params.get(0).getUnit();
//            else
//                unit = "";
//
//            //sắp xếp tăng dần
//            ParamHistoryEntity[] paramHistoryEntities = new ParamHistoryEntity[params.size()];
//            for (int i = 0; i < params.size(); i++) {
//                paramHistoryEntities[i] = params.get(i);
//            }
//            Arrays.sort(paramHistoryEntities, ParamHistoryEntity.ParamHistoryEntityComparator);
//            params.clear();
//            params.addAll(Arrays.asList(paramHistoryEntities));
//
//            LineDataSet d;
//
//            if (paramHistoryEntities.length > 0) {
//                //lấy tọa độ lớn nhất và nhỏ nhất
//                if (minPivotAnchor == 0 && indexParamList == 0)
//                    minPivotAnchor = CommonMethod.convertDateToLong(paramHistoryEntities[0].getTimestamp(), Define.TYPE_DATE_TIME_FULL);
//                long maxPivot = CommonMethod.convertDateToLong(paramHistoryEntities[paramHistoryEntities.length - 1].getTimestamp(), Define.TYPE_DATE_TIME_FULL);
//                if (maxPivot > maxPivotAnchor)
//                    maxPivotAnchor = maxPivot;
//
//                //add thêm các tọa độ khác vào,
//                //tại các tọa độ không có dữ liệu và có dữ liệu
//                mAllPivotX.clear();
//                ArrayList<Entry> entryArrayList = new ArrayList<>();
//                long anchor = getBeginTimeOfDay(minPivotAnchor);
//                int countDataParam = 0;
//                int countPivotX = 0;
//                String datePivot;
//                Entry entry;
//                do {
//                    datePivot = CommonMethod.convertLongToDate(anchor, Define.TYPE_DATE_TIME_FULL);
//                    ObjectPivotXEntity objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
//
//                    mAllPivotX.add(objectPivotXEntity);
//
//                    //nếu là điểm có dữ liệu hoặc không có dữ liệu
//                    if (countDataParam == paramHistoryEntities.length) {
//                        entry = new Entry(countPivotX, Define.VALUE_ENTRY_NULL);
//                        entryArrayList.add(entry);
//                    } else {
//                        if (anchor == CommonMethod.convertDateToLong(paramHistoryEntities[countDataParam].getTimestamp(), Define.TYPE_DATE_TIME_FULL)) {
//                            String getValue = params.get(countDataParam).getValue().replace(" ", "");
//                            entry = new Entry(countPivotX, Float.parseFloat(getValue));
//                            countDataParam++;
//                        } else {
////                            entry = new Entry();
////                            entry.setX(countPivotX);
//                            entry = new Entry(countPivotX, Define.VALUE_ENTRY_NULL);
//                        }
//                        entryArrayList.add(entry);
//                    }
//                    anchor += Define.TIME_BETWEEN_PIVOT_CHART_TRENDS;
//                    countPivotX++;
//                }
//                while (anchor <= maxPivot);
//
//                d = new LineDataSet(entryArrayList, paramListSpinnerDetailDevice.get(indexParamList + 1).getParamDescription());
//                d.setLineWidth(Define.LINE_CHART_WIDTH);
//                d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
//                d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);
//                d.setCircleColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
//                d.setColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
//                d.setValueTextColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
//                IValueFormatter formatterY = new IValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float getValue, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                        StringBuilder result = new StringBuilder();
//                        if (getValue != 0.0f)
//                            result.append(param + ": " + getValue + " " + unit);
//                        else
//                            result.append(getValue);
//                        return result.toString();
//                    }
//                };
//                d.setValueFormatter(formatterY);
//
//                mDataSets.add(d);
//            } else {
//                // những line không có dữ liệu nào sẽ lưu lại để vẽ sau cùng
//                paramNotHasData.add(arrayParam[indexParamList]);
//            }
//        }
//
//        //lấy tất cả các điểm tọa độ có thể
//        mAllPivotX = new ArrayList<>();
//
//        //lấp đầy các tọa độ còn thiếu trong TIME_BETWEEN_PIVOT_CHART_TRENDS phút
//        //điểm neo tịnh tiến tăng dần
//        long anchor = getBeginTimeOfDay(minPivotAnchor);
//        do {
//            String datePivot = CommonMethod.convertLongToDate(anchor, Define.TYPE_DATE_TIME_FULL);
//            ObjectPivotXEntity objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
//            mAllPivotX.add(objectPivotXEntity);
//            anchor += Define.TIME_BETWEEN_PIVOT_CHART_TRENDS;
//        }
//        while (anchor <= maxPivotAnchor);
//
//        //nếu có line nào không có dữ liệu thì khởi tạo lại line đó
//        for (int i = 0; i < paramNotHasData.size(); i++) {
//            LineDataSet d;
//            ArrayList<Entry> entryArrayList = new ArrayList<>();
//            for (int j = 0; j < mAllPivotX.size(); j++) {
//                Entry entry = new Entry(j, Define.VALUE_ENTRY_NULL);
//                entryArrayList.add(entry);
//            }
//
//            d = new LineDataSet(entryArrayList, paramNotHasData.get(i));
//            d.setLineWidth(Define.LINE_CHART_WIDTH);
//            d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
//            d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);
//            d.setCircleColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);
//            d.setColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);
//            d.setValueTextColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);
//
//            mDataSets.add(d);
//        }
//
//        return mDataSets;
//    }

    @Override
    public Context getContextView() {
        return mIMainViewWeakReference.get().getContextView();
    }
    //endregion

    //region AsyncCallMobileCountDevice.OnProcessAsyncMobileCountDevice
    @Override
    public void processOnPreExecuteCountDevice(final ConnectServer.AsyncCallMobileCountDevice asyncCallMobileCountDevice) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateCountDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorCountDeviceOnOff(objectPublish.getMessage(), objectPublish.isResponse);

    }

    @Override
    public void processOnPostExecuteCountDevice(MobileCountDevice countDevice) {
        mIMainViewWeakReference.get().hidePbarConnect();
        if (countDevice == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorCountDeviceOnOff(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataToDashboard(countDevice);
    }

    @Override
    public void processOnCountdownCountDevice(final ConnectServer.AsyncCallMobileCountDevice asyncCallMobileCountDevice) {
        asyncCallMobileCountDevice.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallMobileCountDevice.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorCountDeviceOnOff(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });
    }
    //endregion

    //region AsyncCallUpdateInfo.OnProcessAsyncUpdateInfo
    @Override
    public void processOnPreExecuteUpdateInfo(final ConnectServer.AsyncCallUpdateInfo asyncCallUpdateInfo) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateInfo(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublishAsyntask = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorUpdateInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
    }

    @Override
    public void processOnPostExecuteUpdateInfo(ResponseServerLoginJSON responseServerLoginJSON, String userName) {
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
        if (userName == null || userName.isEmpty())
            return;

        if (responseServerLoginJSON == null) {
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorUpdateInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataToUpdateInfo(responseServerLoginJSON);
    }

    @Override
    public void processOnCountdownUpdateInfo(final ConnectServer.AsyncCallUpdateInfo asyncCallUpdateInfo) {
        asyncCallUpdateInfo.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallUpdateInfo.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorUpdateInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });

    }
    //endregion

    //region AsyncCallGetInfo.OnProcessAsyncGetInfo
    @Override
    public void processOnPreExecuteGetInfo(ConnectServer.AsyncCallGetInfo asyncCallGetInfo, String user) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateGetInfo(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorGetInfo(objectPublish.getMessage(), objectPublish.isResponse);
    }

    @Override
    public void processOnPostExecuteGetInfo(InfoEntity infoEntity, String userName) {
        mIMainViewWeakReference.get().hidePbarConnect();
        if (userName == null || userName.isEmpty())
            return;

        if (infoEntity == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorUpdateInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }
        mIMainViewWeakReference.get().responseDataInfoUpdate(infoEntity);
    }

    @Override
    public void processOnCountdownGetInfo(final ConnectServer.AsyncCallGetInfo asyncCallGetInfo) {
        asyncCallGetInfo.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetInfo.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });
    }
    //endregion

    //region AsyncCallUpdatePass.OnProcessAsyncUpdatePass
    @Override
    public void processOnPreExecuteUpdatePass(ConnectServer.AsyncCallUpdatePass asyncCallUpdatePass) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdatePass(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublishAsyntask = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorUpdatePass(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
    }

    @Override
    public void processOnPostExecuteUpdatePass(ResponseServerLoginJSON responseServerLoginJSON, String userName) {
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
        if (userName == null || userName.isEmpty())
            return;

        if (responseServerLoginJSON == null) {
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorUpdatePass(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataUpdatePass(responseServerLoginJSON);
    }

    @Override
    public void processOnCountdownUpdatePass(final ConnectServer.AsyncCallUpdatePass asyncCallUpdatePass) {
        asyncCallUpdatePass.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallUpdatePass.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorUpdatePass(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });

    }
    //endregion

    //region AsyncCallGetDeviceOnOff.OnProcessAsyncGetDisconnectedDevice
    @Override
    public void processOnPreExecuteDeviceOnOff(Define.STATE_GET_DEVICE state, ConnectServer.AsyncCallGetDeviceOnOff asyncCallGetDeviceOnOff, String user) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateDeviceOnOff(Define.STATE_GET_DEVICE state, ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorUpdateDeviceOnOff(objectPublish.getMessage(), objectPublish.isResponse);
    }

    @Override
    public void processOnPostExecuteDeviceOnOff(Define.STATE_GET_DEVICE state, List<DeviceOnOffEntity> deviceOnOffEntityList, String userName) {

        mIMainViewWeakReference.get().hidePbarConnect();
        DialogEntity dialogMessageEntity;
        if (userName == null || userName.isEmpty())
            return;

        if (deviceOnOffEntityList == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorUpdateInfo(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }
        if (state == Define.STATE_GET_DEVICE.CONNECTED)
            mIMainViewWeakReference.get().responseDataConnected(deviceOnOffEntityList);
        if (state == Define.STATE_GET_DEVICE.DISCONNECTED)
            mIMainViewWeakReference.get().responseDataDisconnected(deviceOnOffEntityList);
    }

    @Override
    public void processOnCountdownDeviceOnOff(Define.STATE_GET_DEVICE state, final ConnectServer.AsyncCallGetDeviceOnOff asyncCallGetDeviceOnOff) {
        asyncCallGetDeviceOnOff.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetDeviceOnOff.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorCountDeviceOnOff(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });

    }
    //endregion

    //region AsyncCallGetInfoDetailDevice.OnProcessAsyncGetInfoDetailDevice
    @Override
    public void processOnPreExecuteGetInfoDetailDevice(ConnectServer.AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice, String user) {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateGetInfoDetailDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorGetInfoDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);
    }

    @Override
    public void processOnPostExecuteInfoDetailDevice(ObjectDetailInfoDevice objectDetailInfoDeviceList, String userName) {
        mIMainViewWeakReference.get().hidePbarConnect();
        DialogEntity dialogMessageEntity;
        if (userName == null || userName.isEmpty())
            return;

        if (objectDetailInfoDeviceList == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorGetInfoDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataDetailDevice(objectDetailInfoDeviceList);
    }

    @Override
    public void processOnCountdownInfoDetailDevice(final ConnectServer.AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice) {
        asyncCallGetInfoDetailDevice.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetInfoDetailDevice.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetInfoDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });

    }
    //endregion

    //region AsyncCallGetChartParamHistoryToDayDevice.OnProcessAsyncGetChartParamHistoryDevice
    @Override
    public void processOnPreExecuteGetLineChartParamHistoryDevice() {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateGetLineChartParamHistoryDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorGetLineParamChartDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);
    }


    @Override
    public void processOnPostExecuteLineChartParamDeviceToday(List<ParamHistoryEntity> paramHistoryEntity, String paramArray) {
        mIMainViewWeakReference.get().hidePbarConnect();

        if (paramArray == null || paramArray.isEmpty())
            return;

        if (paramHistoryEntity == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorGetLineParamChartDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        if (paramHistoryEntity.size() == 0)
        {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Chưa có dữ liệu mới!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorGetLineParamChartDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

      /*  ArrayList<ILineDataSet> dataSet = setDataLineMutilParam(paramArray, paramHistoryEntity);
        if (dataSet != null)
            mIMainViewWeakReference.get().responseDataGetLinePramHistoryDevice(dataSet, (ArrayList<ObjectPivotXEntity>) mAllPivotX);*/

        String[] arrayParam = paramArray.split(Define.SYMBOL_PARAM_URL);

        String timeEnd = CommonMethod.getDateNow(Define.TYPE_DATE_TIME_FULL);
        String timeBegin = CommonMethod.getBeginTimeOfDay(timeEnd, Define.TYPE_DATE_TIME_FULL);

        setDataLineMutilParam(arrayParam, paramHistoryEntity, Define.TIME_BETWEEN_PIVOT_CHART_DETAIL_DEVICE, timeBegin, timeEnd, Define.TYPE_DATE_TIME_FULL);
        if (mDataSetsTemp == null || mAllPivotXTemp == null) {
            mIMainViewWeakReference.get().responseNotifyErrorGetLineParamChartDetailDevice("Lỗi khi khởi tạo các giá trị tọa độ biểu đồ.", false);
            return;
        }

        mDataSetsDetailDevice = new ArrayList<>();
        mDataSetsDetailDevice.addAll(mDataSetsTemp);

        mAllPivotXDetailDevice = new ArrayList<>();
        mAllPivotXDetailDevice.addAll(mAllPivotXTemp);

        mIMainViewWeakReference.get().responseDataGetLinePramHistoryDevice(mDataSetsDetailDevice, (ArrayList<ObjectPivotXEntity>) mAllPivotXDetailDevice);
    }


    @Override
    public void processOnCountdownLineChartParamHistoryDevice(final ConnectServer.AsyncCallGetChartParamHistoryToDayDevice asyncCallGetChartParamHistoryToDayDevice) {
        asyncCallGetChartParamHistoryToDayDevice.cancel(true);

        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetChartParamHistoryToDayDevice.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(false);
                    mIMainViewWeakReference.get().responseNotifyErrorGetLineParamChartDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });
    }
    //endregion

    //region AsyncCallGetSpinnerParamDevice.OnProcessAsyncGetSpinnerParamDevice
    @Override
    public void processOnPreExecuteGetSpinnerParamDevice() {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateGetSpinnerParamDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorGetSpinnerParamDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);

    }

    @Override
    public void processOnPostExecuteGetSpinnerParamDevice(List<ObjectSpinnerParamJSONEntity> entityList) {
        mIMainViewWeakReference.get().hidePbarConnect();
        DialogEntity dialogMessageEntity;

        if (entityList == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorGetSpinnerParamDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataSpinnerParamDevice(entityList);
    }

    @Override
    public void processOnCountdownSpinnerParamDevice(final ConnectServer.AsyncCallGetSpinnerParamDevice asyncCallGetSpinnerParamDevice) {
        asyncCallGetSpinnerParamDevice.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetSpinnerParamDevice.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetSpinnerParamDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });
    }
    //endregion

    //region AsyncCallGetRecyclerParamDevice.OnProcessAsyncGetRecyclerParamDevice
    @Override
    public void processOnPreExecuteGetRecyclerParamDevice() {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }


    @Override
    public void processOnProgressUpdateGetRecyclerParamDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorGetRecyclerParamDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);
    }

    @Override
    public void processOnPostExecuteRecyclerParamDevice(List<ObjectParamaterDeviceJSON> entityList) {
        mIMainViewWeakReference.get().hidePbarConnect();
        DialogEntity dialogMessageEntity;

        if (entityList == null) {
            ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
            objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
            objectPublishAsyntask.setResponse(true);
            mIMainViewWeakReference.get().responseNotifyErrorGetSpinnerParamDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
            return;
        }

        mIMainViewWeakReference.get().responseDataGetRecyclerParamDevice(entityList);
    }

    @Override
    public void processOnCountdownRecyclerParamDevice(final ConnectServer.AsyncCallGetRecyclerParamDevice asyncCallGetRecyclerParamDevice) {
        asyncCallGetRecyclerParamDevice.cancel(true);
        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (asyncCallGetRecyclerParamDevice.ismHasReveicedResponseServer()) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mIMainViewWeakReference.get().responseNotifyErrorGetSpinnerParamDetailDevice(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        });

    }
    //endregion

    //region AsyncCallGetRecyclerEventDevice.OnProcessAsyncGetRecyclerEventDevice
    @Override
    public void processOnPreExecuteRecyclerEventDevice() {
        mIMainViewWeakReference.get().showPbarConnect();
        //check wifi
        checkWifi();
    }

    @Override
    public void processOnProgressUpdateRecyclerEventDevice(ObjectPublishAsyntask... values) {
        //TODO set UI
        mIMainViewWeakReference.get().hidePbarConnect();
        ObjectPublishAsyntask objectPublish = values[0];
        mIMainViewWeakReference.get().responseNotifyErrorRecyclerEventDetailDevice(objectPublish.getMessage(), objectPublish.isResponse);
    }

    @Override
    public void processOnPostExecuteRecyclerEventDevice(List<ObjectEventDevice> entityList) {
        mIMainViewWeakReference.get().hidePbarConnect();
        DialogEntity dialogMessageEntity;

        if (entityList == null) {
            dialogMessageEntity = new DialogEntity.DialogBuilder(mIMainViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Không nhận được dữ liệu từ máy chủ!").build();
            mIMainViewWeakReference.get().showDialogMessage(dialogMessageEntity);
            return;
        }

        mIMainViewWeakReference.get().responseDataRecyclerEventDevice(entityList);
    }

    @Override
    public void processOnCountdownRecyclerEventDevice(final ConnectServer.AsyncCallGetRecyclerEventDevice asyncCallGetRecyclerEventDevice) {
        asyncCallGetRecyclerEventDevice.cancel(true);

        //thread call asyntask is running. must call in other thread to update UI
        ((MainActivity) mIMainViewWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMainViewWeakReference.get().hidePbarConnect();
                if (!asyncCallGetRecyclerEventDevice.ismHasReveicedResponseServer()) {
                    DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mIMainViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Rất tiếc không nhân được bất kỳ dữ liệu nào từ máy chủ!").build();
                    mIMainViewWeakReference.get().showDialogMessage(dialogMessageEntity);
                }
            }
        });
    }
    //endregion

    //region private
    private void checkWifi() {
        if (!CommonMethod.isConnectingWifi(mIMainViewWeakReference.get().getContextView())) {
            DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mIMainViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Đăng nhập thất bại!\nNội dung: Wifi chưa cài đặt mạng internet.").build();
            mIMainViewWeakReference.get().showDialogMessage(dialogMessageEntity);
            return;
        }
    }

    public void setDataLineMutilParam(String[] arrayParam, final List<ParamHistoryEntity> paramHistoryEntity, long timeBetweenPivot, String timeBegin, String timeEnd, String formatDatePivotX) {

        if (arrayParam == null || arrayParam.length == 0)
            return;

        if (paramHistoryEntity == null || paramHistoryEntity.size() == 0)
            return;

        if (mAllPivotXTemp == null)
            mAllPivotXTemp = new ArrayList<>();
        mAllPivotXTemp.clear();

        if (mDataSetsTemp == null)
            mDataSetsTemp = new ArrayList<>();
        mDataSetsTemp.clear();


        //lấy tọa độ lớn nhất và nhỏ nhất
        long maxPivotAnchor = 0;
        long minPivotAnchor = 0;

        //mỗi param ta tạo 1 line cho nó
//        String[] arrayParam = paramSet.split(Define.SYMBOL_PARAM_URL);
        ArrayList<String> paramNotHasData = new ArrayList<>();

        for (int indexParamList = 0; indexParamList < arrayParam.length; indexParamList++) {
            //lọc
            List<ParamHistoryEntity> params = new ArrayList<>();
            for (int indexParamHistory = 0; indexParamHistory < paramHistoryEntity.size(); indexParamHistory++) {
                String paramHistoryEntity1 = paramHistoryEntity.get(indexParamHistory).getDescription();
                String arrayParam1 = arrayParam[indexParamList];
                if (paramHistoryEntity1.equals(arrayParam1)) {
                    params.add(paramHistoryEntity.get(indexParamHistory));
                }
            }

            //Lấy đơn vị để vẽ lên điểm tọa độ Y
            final String param = arrayParam[indexParamList];
            final String unit;
            if (params.size() > 0)
                unit = params.get(0).getUnit();
            else
                unit = "";

            //sắp xếp tăng dần
            ParamHistoryEntity[] paramHistorys = new ParamHistoryEntity[params.size()];
            for (int i = 0; i < params.size(); i++) {
                paramHistorys[i] = params.get(i);
            }
            Arrays.sort(paramHistorys, ParamHistoryEntity.ParamHistoryEntityComparator);
            params.clear();
            params.addAll(Arrays.asList(paramHistorys));

            LineDataSet d;

            if (paramHistorys.length > 0) {
                //lấy tọa độ lớn nhất và nhỏ nhất
                if (minPivotAnchor == 0 && indexParamList == 0)
                    minPivotAnchor = CommonMethod.convertDateToLong(paramHistorys[0].getTimestamp(), Define.TYPE_DATE_TIME_FULL);
                long maxPivot = CommonMethod.convertDateToLong(timeEnd, Define.TYPE_DATE_TIME_FULL);
//                long maxPivot = CommonMethod.convertDateToLong(paramHistorys[paramHistorys.length - 1].getTimestamp(), Define.TYPE_DATE_TIME_FULL);
                if (maxPivot > maxPivotAnchor)
                    maxPivotAnchor = maxPivot;

                //add thêm các tọa độ khác vào,
                //tại các tọa độ không có dữ liệu và có dữ liệu
                mAllPivotXTemp.clear();
                ArrayList<Entry> entryArrayList = new ArrayList<>();
//                long anchor = getBeginTimeOfDay(minPivotAnchor);
                long anchor = CommonMethod.convertDateToLong(timeBegin, Define.TYPE_DATE_TIME_FULL);
                int countDataParam = 0;
                int countPivotX = 0;
                String datePivot;
                Entry entry;
                do {
                    datePivot = CommonMethod.convertLongToDate(anchor, formatDatePivotX);
                    ObjectPivotXEntity objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);

                    mAllPivotXTemp.add(objectPivotXEntity);

                    //nếu là điểm có dữ liệu hoặc không có dữ liệu
                    if (countDataParam == paramHistorys.length) {
                        entry = new Entry(countPivotX, Define.VALUE_ENTRY_NULL);
                        entryArrayList.add(entry);
                    } else {
                        if (anchor == CommonMethod.convertDateToLong(paramHistorys[countDataParam].getTimestamp(), Define.TYPE_DATE_TIME_FULL)) {
                            String value = params.get(countDataParam).getValue().replace(" ", "");
                            entry = new Entry(countPivotX, Float.parseFloat(value));
                            countDataParam++;
                        } else {
//                            entry = new Entry();
//                            entry.setX(countPivotX);
                            entry = new Entry(countPivotX, Define.VALUE_ENTRY_NULL);
                        }
                        entryArrayList.add(entry);
                    }
                    anchor += timeBetweenPivot;
                    countPivotX++;
                }
                while (anchor <= maxPivot);

                d = new LineDataSet(entryArrayList, arrayParam[indexParamList]);
                d.setLineWidth(Define.LINE_CHART_WIDTH);
                d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
                d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);
                d.setCircleColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
                d.setColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
                d.setValueTextColor(Define.COLORS_LINE_CHART[indexParamList % Define.COLORS_LINE_CHART.length]);
                IValueFormatter formatterY = new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        StringBuilder result = new StringBuilder();
                        if (value != 0.0f)
                            result.append(param + ": " + value + " " + unit);
                        else
                            result.append(value);
                        return result.toString();
                    }
                };
                d.setValueFormatter(formatterY);

                mDataSetsTemp.add(d);
            } else {
                // những line không có dữ liệu nào sẽ lưu lại để vẽ sau cùng
                paramNotHasData.add(arrayParam[indexParamList]);
            }
        }

        //lấy tất cả các điểm tọa độ có thể
        mAllPivotXTemp = new ArrayList<>();

        //lấp đầy các tọa độ còn thiếu trong timeBetweenPivot phút
        //điểm neo tịnh tiến tăng dần

        //TODO lấy mốc thời gian từ ngày đầu nhập vào
        //dateBegin: 28/03/2017 to long of 2017-03-28T00:00:00
        long anchor = CommonMethod.convertDateToLong(timeBegin, Define.TYPE_DATE_TIME_FULL);
//        long anchor = getBeginTimeOfDay(minPivotAnchor);
        do {
            String datePivot = CommonMethod.convertLongToDate(anchor, formatDatePivotX);
            ObjectPivotXEntity objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
            mAllPivotXTemp.add(objectPivotXEntity);
            anchor += timeBetweenPivot;
        }
        while (anchor <= maxPivotAnchor);

        //nếu có line nào không có dữ liệu thì khởi tạo lại line đó
        for (int i = 0; i < paramNotHasData.size(); i++) {
            LineDataSet d;
            ArrayList<Entry> entryArrayList = new ArrayList<>();
            for (int j = 0; j < mAllPivotXTemp.size(); j++) {
                Entry entry = new Entry(j, Define.VALUE_ENTRY_NULL);
                entryArrayList.add(entry);
            }

            d = new LineDataSet(entryArrayList, paramNotHasData.get(i));
            d.setLineWidth(Define.LINE_CHART_WIDTH);
            d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
            d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);
            d.setCircleColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);
            d.setColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);
            d.setValueTextColor(Define.COLORS_LINE_CHART[i % Define.COLORS_LINE_CHART.length]);

            mDataSetsTemp.add(d);
        }
    }

    private long getBeginTimeOfDay(long minPivotAnchor) {
        String dayPivot = CommonMethod.convertLongToDate(minPivotAnchor, Define.TYPE_DATE_TIME_FULL);
        //2017-03-28T21:00:00 to 2017-03-28T00:00:00
        String[] pivotXSetT = dayPivot.split("T");
        String result = pivotXSetT[0] + "T" + "00:00:00";
        return CommonMethod.convertDateToLong(result, Define.TYPE_DATE_TIME_FULL);
    }

  /*  public ArrayList<ILineDataSet> setDataLine(String param, List<ParamHistoryEntity> paramHistoryEntity) {
        //mỗi một line chart sẽ có tập điểm cũ + tập điểm mới
        ArrayList<Entry> entryArrayListNewest;
        ArrayList<Entry> entryArrayListFull = new ArrayList<>();

        if (param == null || param.isEmpty())
            return mDataSets;

        if (paramHistoryEntity == null)
            return mDataSets;

        //lấy tập điểm mới nhất, và thêm pivot mới nhất(nếu có)
        entryArrayListNewest = refreshAllPivotX(paramHistoryEntity);

        if (mDataSets == null || mDataSets.isEmpty()) {
            mDataSets = new ArrayList<>();

            //chart add đường line đầu tiên
            entryArrayListFull.addAll(entryArrayListNewest);

            LineDataSet d = new LineDataSet(entryArrayListNewest, param);
            d.setLineWidth(Define.LINE_CHART_WIDTH);
            d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
            d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);

            mDataSets.add(d);
            return mDataSets;
        }

        //nếu có dữ liệu cũ
        //tìm lại dữ liệu cũ
        int position = -1;

        for (int i = 0; i < mDataSets.size(); i++) {
            if (mDataSets.get(i).equals(param)) {
                position = i;
            }
        }
        if (position < 0)
            return mDataSets;

        ILineDataSet element = mDataSets.get(position);
        mDataSets.remove(position);

        for (int i = 0; i < element.getEntryCount(); i++) {
            Entry entry = element.getEntryForIndex(i);
            entryArrayListFull.add(entry);
        }

        //nối thêm dữ liệu mới
        entryArrayListFull.addAll(entryArrayListNewest);

        //khi có array entry mới rồi thì refresh lại LineDataSet đó
        LineDataSet d = new LineDataSet(entryArrayListFull, element.getLabel());
        d.setLineWidth(Define.LINE_CHART_WIDTH);
        d.setCircleRadius(Define.LINE_CHART_CRICLE_RADIUS);
        d.setCircleHoleRadius(Define.LINE_CHART_CRICLE_HOLE_RADIUS);
        mDataSets.add(d);

        return mDataSets;
    }
*/

   /* private ArrayList<Entry> refreshAllPivotX(
            final List<ParamHistoryEntity> paramHistoryEntity) {

        //sắp xếp tăng dần
        ParamHistoryEntity[] paramHistorys = new ParamHistoryEntity[paramHistoryEntity.size()];
        for (int i = 0; i < paramHistoryEntity.size(); i++) {
            paramHistorys[i] = paramHistoryEntity.get(i);
        }
        Arrays.sort(paramHistorys, ParamHistoryEntity.ParamHistoryEntityComparator);
        paramHistoryEntity.clear();
        paramHistoryEntity.addAll(Arrays.asList(paramHistorys));

//        long a = CommonMethod.convertDateToLong("2017-03-27T02:00:00") - CommonMethod.convertDateToLong("2017-03-27T01:00:00");

        ArrayList<Entry> entryArrayListTemp = new ArrayList<>();
        ObjectPivotXEntity objectPivotXEntity;
        String datePivot;
        ArrayList<Long> pivotLongDateTemp = new ArrayList<>();
        long[] pivotLongDateOld;
        long[] pivotLongDateNew = new long[paramHistoryEntity.size()];

        //nếu chart null data
        if (mAllPivotX == null) {
            mAllPivotX = new ArrayList<>();
            for (int i = 0; i < paramHistoryEntity.size(); i++) {
                pivotLongDateNew[i] = CommonMethod.convertDateToLong(paramHistoryEntity.get(i).getTimestamp(), Define.TYPE_DATE_TIME_FULL);
            }

            //TODO lấp đầy các tọa độ còn thiếu trong TIME_BETWEEN_PIVOT_CHART_TRENDS phút
            //điểm neo tịnh tiến tăng dần
            long anchor = pivotLongDateNew[0];
            pivotLongDateTemp.add(anchor);
            datePivot = CommonMethod.convertLongToDate(anchor, Define.TYPE_DATE_TIME_FULL);
            objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
            mAllPivotX.add(objectPivotXEntity);

            int count = 0;
            while (anchor != pivotLongDateNew[pivotLongDateNew.length - 1]) {
                anchor += Define.TIME_BETWEEN_PIVOT_CHART_TRENDS;

                //thêm vào tập tọa độ
                datePivot = CommonMethod.convertLongToDate(anchor, Define.TYPE_DATE_TIME_FULL);
                objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
                mAllPivotX.add(objectPivotXEntity);

                //thêm giá trị gốc hoặc 0 tại điểm tọa dộ anchor này
                Entry entry;
                if (datePivot.equals(paramHistoryEntity.get(count).getTimestamp())) {
                    entry = new Entry(anchor, Float.parseFloat(paramHistoryEntity.get(count).getValue()));
                    count++;
                } else {
                    entry = new Entry(anchor, Define.VALUE_ENTRY_NULL);
                }
                entryArrayListTemp.add(entry);
            }
            return entryArrayListTemp;
        }

        // nếu có dữ liệu cũ, nó đã được sắp xếp
        pivotLongDateOld = new long[mAllPivotX.size()];
        for (int i = 0; i < mAllPivotX.size(); i++) {
            pivotLongDateOld[i] = CommonMethod.convertDateToLong(mAllPivotX.get(i).getTimeStamp(), Define.TYPE_DATE_TIME_FULL);
        }

        //và dữ liệu mới cũng đã được sắp xếp
        for (int i = 0; i < paramHistoryEntity.size(); i++) {
            pivotLongDateNew[i] = CommonMethod.convertDateToLong(paramHistoryEntity.get(i).getTimestamp(), Define.TYPE_DATE_TIME_FULL);
        }

        //kiểm tra điểm cuối của dữ liệu cũ
        //so sánh
        //điểm cuối của dữ liệu mới
        long maxPivotLongNew = pivotLongDateNew[pivotLongDateNew.length - 1];
        long maxPivotLongOld = pivotLongDateOld[pivotLongDateOld.length - 1];

        if (maxPivotLongNew > maxPivotLongOld) {
            //tạo điểm neo tịnh tính giảm dần
            long anchor = maxPivotLongNew;

            //mảng động chứa tất cả các điểm có thể từ maxPivotLongOld tới maxPivotLongNew
            ArrayList<Long> temp = new ArrayList<>();
            temp.add(anchor);

            int count = pivotLongDateNew.length - 1;
            while (anchor > maxPivotLongOld) {
                anchor -= Define.TIME_BETWEEN_PIVOT_CHART_TRENDS;

                //ngưỡng
                if (anchor < maxPivotLongOld)
                    break;

                temp.add(anchor);
                Entry entry;
                if (anchor == pivotLongDateNew[count]) {
                    count--;
                    entry = new Entry(anchor, Float.parseFloat(paramHistoryEntity.get(count).getValue()));
                } else {
                    entry = new Entry(anchor, Define.VALUE_ENTRY_NULL);
                }
                entryArrayListTemp.add(entry);
            }

            //đảo ngược
            Collections.reverse(entryArrayListTemp);

            //thêm vào mAllPivotX
            for (int i = temp.size(); i > 0; i--) {
                datePivot = CommonMethod.convertLongToDate(temp.get(i), Define.TYPE_DATE_TIME_FULL);
                objectPivotXEntity = new ObjectPivotXEntity(datePivot, true);
                mAllPivotX.add(objectPivotXEntity);
            }

            return entryArrayListTemp;
        }

        return entryArrayListTemp;
    }

*/
//    private void removeDataLine(int position) {
//        if (mDataSets == null)
//            return;
//        if (position < 0)
//            return;
//        mDataSets.remove(position);
//    }


    //endregion
}


