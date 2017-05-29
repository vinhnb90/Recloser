package esolutions.com.recloser.View.Activity.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.Entity.ObjectPivotXEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamEntity;
import esolutions.com.recloser.Entity.ObjectSpinnerParamJSONEntity;
import esolutions.com.recloser.Entity.MobileCountDevice;
import esolutions.com.recloser.Entity.ObjectDetailInfoDevice;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.Model.DeviceAdapter;
import esolutions.com.recloser.Model.DeviceOnOffAdapter.CallbackDeviceOnOffAdapter;
import esolutions.com.recloser.Model.ImageBlurHolderModer;
import esolutions.com.recloser.Model.NavigationMenuApdater;
import esolutions.com.recloser.Model.SpinnerDetailDeviceAdapter;
import esolutions.com.recloser.Presenter.Class.MainPresenter;
import esolutions.com.recloser.Presenter.Interface.IMainPresenter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.View.Activity.Interface.IMainView;
import esolutions.com.recloser.View.Fragment.AllDeviceFragment;
import esolutions.com.recloser.View.Fragment.ConnectedFragment;
import esolutions.com.recloser.View.Fragment.DashboardFragment;
import esolutions.com.recloser.View.Fragment.DateTimePickerCustomFragment;
import esolutions.com.recloser.View.Fragment.DetailDeviceFragment;
import esolutions.com.recloser.View.Fragment.DisconnectedFragment;
import esolutions.com.recloser.View.Fragment.EventAndAlarmTodayFragment;
import esolutions.com.recloser.View.Fragment.HistoryAndAlarmFragment;
import esolutions.com.recloser.View.Fragment.HistoryDetailDeviceFragment;
import esolutions.com.recloser.View.Fragment.TrendsFragment;
import esolutions.com.recloser.View.Fragment.UnderValueFragment;
import esolutions.com.recloser.View.Fragment.UpdateInfoFragment;
import esolutions.com.recloser.View.Fragment.UpdatePassFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivityAppCompat implements
        IMainView,
        DashboardFragment.OnDashboardFragmentListener,
        AllDeviceFragment.OnAllDeviceFragmentListener,
        ConnectedFragment.OnConnectedFragmentListener,
        DisconnectedFragment.OnDisconnectedFragmentListener,
        EventAndAlarmTodayFragment.OnEventOrAlarmTodayFragmentListener,
        UnderValueFragment.OnUnderValueFragmentListener,
        NavigationView.OnNavigationItemSelectedListener,
        UpdateInfoFragment.OnUpdateInfoFragmentListener,
        UpdatePassFragment.OnUpdatePassFragmentListener,
        DetailDeviceFragment.OnDetailDeviceListener,
        DeviceAdapter.CallBackDeviceApdater,
        SpinnerDetailDeviceAdapter.CallBackSpinnerDetailDeviceAdapter,
        CallbackDeviceOnOffAdapter,
        TrendsFragment.OnTrendsFragmentListener,
        HistoryAndAlarmFragment.OnHistoryAndAlarmFragmentListener,
        HistoryDetailDeviceFragment.OnHistoryDetailDeviceListener,
        View.OnClickListener {

    private ExpandableListView mExpListView;
    private IMainPresenter mIMainPresenter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private static FragmentManager mFragmentManager;

    private RelativeLayout mRlContentMain;
    private ProgressBar mPbarConnect;

    private ImageBlurHolderModer mBlurHolderModer;
    private Bitmap mBackgroundBlur;
    private Button mBtnRefresh;
    private TextView mTvTitle;

    private String mUser;

    private static final String HOLDER_IS_ROTATE = "HOLDER_IS_ROTATE";
    private static final String HOLDER_DATA_MODEL_ROTATE = "HOLDER_DATA_MODEL_ROTATE";
    private static final String HOLDER_DATA_TAG_INDENTIFY_FRAGMENT_ROTATE = "HOLDER_DATA_TAG_INDENTIFY_FRAGMENT_ROTATE";
    private Define.FRAGMENT_TAG mTagFragIsVisibling;

    private int statusScreenPreviewRotate;

    //region MainActivity

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusScreenPreviewRotate = getResources().getConfiguration().orientation;
        //khôi phục trạng thái trước đó
        if (savedInstanceState != null) {
            statusScreenPreviewRotate = savedInstanceState.getInt(HOLDER_IS_ROTATE);
            mIMainPresenter = savedInstanceState.getParcelable(HOLDER_DATA_MODEL_ROTATE);
            mTagFragIsVisibling = (Define.FRAGMENT_TAG) savedInstanceState.getSerializable(HOLDER_DATA_TAG_INDENTIFY_FRAGMENT_ROTATE);
        }

        hideActionBar();
        setContentView(R.layout.activity_main);
        initView();
        initSource();
        setAction(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(HOLDER_IS_ROTATE, statusScreenPreviewRotate);
        outState.putParcelable(HOLDER_DATA_MODEL_ROTATE, (MainPresenter) mIMainPresenter);
        outState.putSerializable(HOLDER_DATA_TAG_INDENTIFY_FRAGMENT_ROTATE, mTagFragIsVisibling);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        statusScreenPreviewRotate = savedInstanceState.getInt(HOLDER_IS_ROTATE);
        mIMainPresenter = savedInstanceState.getParcelable(HOLDER_DATA_MODEL_ROTATE);
        mTagFragIsVisibling = (Define.FRAGMENT_TAG) savedInstanceState.getSerializable(HOLDER_DATA_TAG_INDENTIFY_FRAGMENT_ROTATE);
    }

  /*  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // lấy orientation hiện tại
        int orientationOld = getResources().getConfiguration().orientation;

        // Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == orientationOld) {
            //không đổi
            statusScreenPreviewRotate = false;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //đổi hướng
            statusScreenPreviewRotate = true;
        }
        super.onConfigurationChanged(newConfig);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int backStack = getSupportFragmentManager().getBackStackEntryCount();
            if (backStack == 1) {
                this.finish();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.nav_change_user_profile:
                mIMainPresenter.callRefreshFragment(Define.FRAGMENT_TAG.UPDATE_INFO_FRAG.name());
                break;

            case R.id.nav_change_pass:
                mIMainPresenter.callRefreshFragment(Define.FRAGMENT_TAG.UPDATE_PASS_FRAG.name());
                break;

            case R.id.nav_setting:
                Toast.makeText(this, "Setting menu", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_view_push));
        switch (v.getId()) {
            case R.id.btn_app_bar_main_refresh:
                refreshFragmentVisibling();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentManager = null;
    }
    //endregion

    //region BaseActivityAppCompat
    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mExpListView = (ExpandableListView) findViewById(R.id.ac_main_navi_menu_expandableListView);
        mRlContentMain = (RelativeLayout) findViewById(R.id.rl_content_main);
        mPbarConnect = (ProgressBar) findViewById(R.id.pbar_content_main_connect);
        mBtnRefresh = (Button) findViewById(R.id.btn_app_bar_main_refresh);
        mTvTitle = (TextView) findViewById(R.id.tv_app_bar_main_title);
    }

    @Override
    protected void initSource() {
        if (getIntent() != null)
            mUser = getIntent().getExtras().getString(Define.PARAM_NAME_USER, "");

//        mUser = "Operator";
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mIMainPresenter = new MainPresenter(this);
        mFragmentManager = this.getSupportFragmentManager();

        //set img_background_dashboard blur
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.img_background_dashboard);
        try {
            mBlurHolderModer = ImageBlurHolderModer.getInstance(this);
            mBlurHolderModer.setsBackgroundBlur(this, background, Define.BLUR_RADIUS.BACKGROUND_LOGIN_RADIUS.getValues());
            if (mBackgroundBlur != null)
                mBackgroundBlur.recycle();
            mBackgroundBlur = mBlurHolderModer.getsBackgroundBlur();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setAction(Bundle savedInstanceState) {
        //set default gone pbar
        mPbarConnect.setVisibility(View.INVISIBLE);

        //config navigation menu
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        mBtnRefresh.setOnClickListener(this);

        //listener action menu
        mExpListView.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        String keyGroupMenu = ((NavigationMenuApdater) mExpListView.getExpandableListAdapter()).getGroupKey(groupPosition);
                        if (mExpListView.isGroupExpanded(groupPosition)) {
                            mDrawer.closeDrawer(GravityCompat.START);
                        }
                        mIMainPresenter.callRefreshFragment(keyGroupMenu);
                        return false;
                    }
                });
        // Listview Group expanded listener
        mExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //get key group
                String keyGroupMenu = ((NavigationMenuApdater) mExpListView.getExpandableListAdapter()).getGroupKey(groupPosition);
                mIMainPresenter.callRefreshFragment(keyGroupMenu);
            }
        });

        // Listview Group collasped listener
        mExpListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //get key child
                String keyMenu = ((NavigationMenuApdater) mExpListView.getExpandableListAdapter()).getChildKey(groupPosition, childPosition);
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                }
                mIMainPresenter.callRefreshFragment(keyMenu);
                return false;
            }
        });

        //set blur img_background_dashboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRlContentMain.setBackground(new BitmapDrawable(this.getResources(), mBackgroundBlur));
        } else {
            mRlContentMain.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundBlur));
        }

        //call menu
        mIMainPresenter.callMenuNavigation();

        //default fragment
        if (savedInstanceState == null)
            this.showDashboardFragment(mUser);
        else {
            //lấy fragment top stack
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mTagFragIsVisibling.name());
            try {
                visibleFragment(R.id.ll_content_main_frag, fragment, true, mTagFragIsVisibling);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        DeviceEntity deviceEntity = new DeviceEntity.DeviceBuilder("String ipAddress", "String name", 0, 0, 0, 0, 1, false).build();
//        this.showDeviceDetailFragment(deviceEntity);
    }


    @Override
    public void hideActionBar() {
        super.hideActionBarParent();
    }

    @Override
    public void showToast(String message) {
        super.showToastParent(message);
    }

    @Override
    public void showToast(String message, long time) {
        super.showToastParent(message, time);
    }

    @Override
    public void showDialogMessage(DialogEntity dialogEntity) {
        super.showDialogMessageParent(dialogEntity);
    }
    //endregion

    //region IMainView
    @Override
    public Context getContextView() {
        return super.getContextParent();
    }

    @Override
    public void visibleNavigationMenu(NavigationMenuApdater mListAdapter) {
        if (mListAdapter == null)
            return;
        mExpListView.setAdapter(mListAdapter);
    }

    @Override
    public void refreshAllDeviceRecycler(DeviceAdapter deviceApdater) {

    }

    @Override
    public void refreshDisconnectedRecycler(DeviceAdapter deviceApdater) {
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DisconnectedFragment) {
            ((DisconnectedFragment) fragmentVisibling).refreshDeviceAdapter(deviceApdater);
        } else return;
    }

    @Override
    public IMainPresenter getMainPresenter() {
        if (mIMainPresenter == null)
            mIMainPresenter = new MainPresenter(this);
        return mIMainPresenter;
    }

    @Override
    public void responseDataToDashboard(@Nullable MobileCountDevice countDevice) {
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DashboardFragment) {
            ((DashboardFragment) fragmentVisibling).refreshCountDeviceText(countDevice);
        }
    }

    @Override
    public void responseDataInfoUpdate(InfoEntity infoEntity) {
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof UpdateInfoFragment) {
            ((UpdateInfoFragment) fragmentVisibling).fillDataInfoUser(infoEntity);
        }
    }


    private void responseDataChooseDateTimeTrends(int year, int month, int date) {
        if (month <= 0 || month > 12)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            ((TrendsFragment) fragmentVisibling).setTextDateTimePicker(year, month, date);
        }
    }

    @Override
    public void responseDataConnected(List<DeviceOnOffEntity> deviceOnOffEntity) {
        if (deviceOnOffEntity == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof ConnectedFragment) {
            ((ConnectedFragment) fragmentVisibling).fillDataConnectedDevice(deviceOnOffEntity);
        }
    }

    @Override
    public void responseDataDisconnected(List<DeviceOnOffEntity> deviceOnOffEntity) {
        if (deviceOnOffEntity == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DisconnectedFragment) {
            ((DisconnectedFragment) fragmentVisibling).fillDataDisconnectedDevice(deviceOnOffEntity);
        }
    }

    @Override
    public void responseDataGetAllDevice(List<DeviceEntity> deviceEntities) {
        if (deviceEntities == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof AllDeviceFragment) {
            ((AllDeviceFragment) fragmentVisibling).fillDataAllDevice(deviceEntities);
        }
    }

    @Override
    public void responseDataDetailDevice(ObjectDetailInfoDevice objectDetailInfoDevice) {
        if (objectDetailInfoDevice == null)
            return;

        Bitmap bitmap = mIMainPresenter.callGetAvartarDevice(objectDetailInfoDevice.getDeviceID(), objectDetailInfoDevice.getDeviceName(), objectDetailInfoDevice.getDeviceAvartar());

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).fillDataInfoDetailDevice(objectDetailInfoDevice, bitmap);
        }
    }

    @Override
    public void responseDataGetLinePramHistoryDevice(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> listPivotX) {
        if (dataSet == null)
            return;
        if (listPivotX == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).refreshDataLineChart(dataSet, listPivotX);
        }
    }

    @Override
    public void responseDataSpinnerParamDevice(List<ObjectSpinnerParamJSONEntity> entityLists) {
        if (entityLists == null)
            return;
        List<ObjectSpinnerParamEntity> entityListConvert = new ArrayList<>();
        for (ObjectSpinnerParamJSONEntity entity :
                entityLists) {
            entityListConvert.add(new ObjectSpinnerParamEntity(entity));
        }

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).setDataSpinnerParam(entityListConvert, true);
        }
    }

    @Override
    public void responseDataGetRecyclerParamDevice(List<ObjectParamaterDeviceJSON> entityList) {
        if (entityList == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).setDataRecyclerParam(entityList);
        }
    }

    @Override
    public void responseDataRecyclerEventDevice(List<ObjectEventDevice> entityList) {
        if (entityList == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).setDataRecyclerEvent(entityList);
        }
    }

    @Override
    public void responseDataGetAllDeviceTrends(List<DeviceEntity> deviceEntityList) {
        if (deviceEntityList == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            ((TrendsFragment) fragmentVisibling).setDataSpinnerAllDeviceTrends(deviceEntityList);
        }
    }

    @Override
    public void responseDataChartTrends(ArrayList<ILineDataSet> dataSet, ArrayList<ObjectPivotXEntity> mAllPivotX) {
        if (dataSet == null)
            return;
        if (mAllPivotX == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            ((TrendsFragment) fragmentVisibling).refreshDataLineChartTrends(dataSet, mAllPivotX);
        }
    }

    @Override
    public void responseDataRecyclerTrends(List<ParamHistoryEntity> paramHistoryEntityList) {
        if (paramHistoryEntityList == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            ((TrendsFragment) fragmentVisibling).setDataRecyclerTrends(paramHistoryEntityList);
        }
    }

    @Override
    public void responseDataToUpdateInfo(ResponseServerLoginJSON responseServerLoginJSON) {
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof UpdateInfoFragment) {
            ((UpdateInfoFragment) fragmentVisibling).notifyResultUpdateInfo(responseServerLoginJSON);
        }
    }

    @Override
    public void responseDataUpdatePass(ResponseServerLoginJSON responseServerLoginJSON) {
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof UpdatePassFragment) {
            ((UpdatePassFragment) fragmentVisibling).notifyResultUpdatePass(responseServerLoginJSON);
        }
    }

    @Override
    public void responseNotifyErrorGetInfoDetailDevice(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).notifyErrorInfoDetail(message);
        }
    }

    @Override
    public void responseNotifyErrorGetSpinnerParamDetailDevice(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).notifyErrorSpinnerParamDetail(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorGetRecyclerParamDetailDevice(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).notifyErrorRecyclerParamDetail(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorRecyclerEventDetailDevice(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).notifyErrorRecyclerEventDetailDevice(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorGetLineParamChartDetailDevice(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            ((DetailDeviceFragment) fragmentVisibling).notifyLineParamChartDetailDevice(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorCountDeviceOnOff(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorUpdateInfo(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorUpdatePass(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorUpdateDeviceOnOff(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorGetAllDevice(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorGetInfo(String message, boolean isResponse) {

    }

    @Override
    public void responseNotifyErrorGetAllDeviceTrends(String message, boolean isResponse) {
        Log.e(Define.TAG, "responseNotifyErrorGetAllDeviceTrends: " + message);
    }

    @Override
    public void responseNotifyErrorGetAllParamHistoryTrends(String message, boolean isResponse) {
        if (message == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            ((TrendsFragment) fragmentVisibling).notifyErrorLineParamChartDeviceTrends(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorGetAllDeviceHistoryAndAlarm(String message, boolean isResponse) {
        if (message == null || message.isEmpty())
            return;
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
            ((HistoryAndAlarmFragment) fragmentVisibling).setNotifyErrorGetAllDeviceSpinner(message, isResponse);
        }
    }

    @Override
    public void responseNotifyErrorGetHistoryAndAlarmRecyclerToday(String message, boolean isResponse, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (message == null || message.isEmpty())
            return;
        if (typeString == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof EventAndAlarmTodayFragment) {
            ((EventAndAlarmTodayFragment) fragmentVisibling).setNotifyErrorGetHistoryEventOrAlarm(message, isResponse, typeString);
        }
    }

    @Override
    public void responseDataGetAllDeviceHistoryAndAlarm(List<DeviceEntity> deviceEntities) {
        if (deviceEntities == null)
            return;
        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
            ((HistoryAndAlarmFragment) fragmentVisibling).setDataSpinnerDevice(deviceEntities);
        }
    }

    @Override
    public void responseNotifyErrorGetHistoryAndAlarmRecycler(String message, boolean isResponse, String typeString) {

    }

    @Override
    public void responseDataGetHistoryAndAlarmRecycler(List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs, String typeString) {
        if (historyAndAlarmEventJSONs == null)
            return;
        if (typeString == null || typeString.isEmpty())
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
            ((HistoryAndAlarmFragment) fragmentVisibling).setDataRecyclerHistoryAndAlarm(historyAndAlarmEventJSONs, typeString);
        }
    }

    @Override
    public void responseDataGetHistoryAndAlarmRecyclerToday(List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (historyAndAlarmEventJSONTodays == null)
            return;
        if (typeString == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof EventAndAlarmTodayFragment) {
            ((EventAndAlarmTodayFragment) fragmentVisibling).setDataRecyclerHistoryAndAlarmToday(historyAndAlarmEventJSONTodays, typeString);
        }
    }

    @Override
    public void refreshEventAndAlarmTodayFragment(String userName) {
        if (userName == null || userName.isEmpty())
            return;

        Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY type = null;
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);

        //nếu không có dữ liệu người dùng chọn thì ta chỉ việc refresh lại fragment
        if (fragmentVisibling instanceof EventAndAlarmTodayFragment) {
            type = ((EventAndAlarmTodayFragment) fragmentVisibling).getType();
            if (type == null)
                return;
            showEventOrAlarmFragment(userName, type);
        }
    }

    @Override
    public void refreshHistoryDetailFragment(String userName) {
        if (userName == null || userName.isEmpty())
            return;

        String dateBegin = "", dateEnd = "";
        int idDevice = 0;

        //Nếu có dữ liệu người dùng chọn thì ta phải refesh với dữ liệu hiện đang có
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryDetailDeviceFragment) {
            dateBegin = ((HistoryDetailDeviceFragment) fragmentVisibling).getDateBegin();
            dateEnd = ((HistoryDetailDeviceFragment) fragmentVisibling).getDateEnd();
            idDevice = ((HistoryDetailDeviceFragment) fragmentVisibling).getmIdDevice();
            if (dateBegin == null || dateBegin.isEmpty())
                return;
            if (dateEnd == null || dateEnd.isEmpty())
                return;
            if (idDevice <= 0)
                return;
            showRecyclerHistoryDetailDevice(idDevice, dateBegin, dateEnd);
        } else {
            showHistoryFragmentFromDetailDevice(idDevice);
        }
    }

    @Override
    public void refreshDeviceDetailFragment(String userName) {
        if (userName == null || userName.isEmpty())
            return;
        DeviceEntity deviceEntity = null;
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);

        //Trường hợp này không cần thiết refresh theo dữ liệu người dùng đã chọn (nếu có)
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            deviceEntity = ((DetailDeviceFragment) fragmentVisibling).getmDevice();
            if (deviceEntity == null)
                return;
            showDeviceDetailFragment(deviceEntity);
        }
    }

    @Override
    public void refreshTrendsFragment(String userName) {
        if (userName == null || userName.isEmpty())
            return;
        //refesh theo dữ liệu người dùng
        int idDevice = 0;
        String nameDevice = "", startDate = "", endDate = "";

        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof TrendsFragment) {
            idDevice = ((TrendsFragment) fragmentVisibling).getIdDeviceChoose();
            nameDevice = ((TrendsFragment) fragmentVisibling).getNameDeviceChoose();
            startDate = ((TrendsFragment) fragmentVisibling).getDateBegin();
            endDate = ((TrendsFragment) fragmentVisibling).getDateEnd();

            if (idDevice <= 0)
                return;
            if (nameDevice == null || nameDevice.isEmpty())
                return;
            if (startDate == null || startDate.isEmpty())
                return;
            if (endDate == null || endDate.isEmpty())
                return;

            showAllLineChartAndRecyclerTrends(idDevice, nameDevice, startDate, endDate);
        } else {
            showTrendsFragment(userName);
        }
    }

    @Override
    public void refreshHistoryAlarmFragment(String userName) {
        if (userName == null || userName.isEmpty())
            return;
        //refesh theo dữ liệu người dùng
        int idDevice = 0;
        String nameDevice = "", typeString ="", startDate = "", endDate = "";

        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
            idDevice = ((HistoryAndAlarmFragment) fragmentVisibling).getIdDeviceChoosen();
            nameDevice = ((HistoryAndAlarmFragment) fragmentVisibling).getNameDeviceChoosen();
            typeString = ((HistoryAndAlarmFragment) fragmentVisibling).getTypeSpinnerHistory();
            startDate = ((HistoryAndAlarmFragment) fragmentVisibling).getDateBegin();
            endDate = ((HistoryAndAlarmFragment) fragmentVisibling).getDateEnd();
            showRecyclerHistoryAndAlarm(idDevice, nameDevice, typeString, startDate, endDate);;
        }else {
            showHistoryAlarmFragment(userName);
        }
    }

    @Override
    public void responseNotifyErrorGetHistoryDetailDevice(String message, boolean isResponse) {

    }

    @Override
    public void responseDataGetHistoryDetailDeviceRecycler(List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs) {
        if (historyDetailDeviceJSONs == null)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryDetailDeviceFragment) {
            ((HistoryDetailDeviceFragment) fragmentVisibling).setDataRecyclerHistoryDetailDevice(historyDetailDeviceJSONs);
        }
    }


    @Override
    public void showPbarConnect() {
        mPbarConnect.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePbarConnect() {
        if (mPbarConnect.getVisibility() == View.VISIBLE)
            mPbarConnect.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTitleFeature(String title) {
        if (title == null || title.isEmpty())
            return;
        mTvTitle.setText(title);
    }

    @Override
    public void showDashboardFragment(String user) {
        if (user == null || user.isEmpty())
            return;

        DashboardFragment dashboardFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof DashboardFragment) {
                //nếu đã có thì refresh nó
                dashboardFragment = (DashboardFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.DASHBOARD_FRAG.name());
                mFragmentManager.beginTransaction().detach(dashboardFragment).attach(dashboardFragment).commit();
            } else {
                dashboardFragment = DashboardFragment.newInstance(user);
                visibleFragment(R.id.ll_content_main_frag, dashboardFragment, true, Define.FRAGMENT_TAG.DASHBOARD_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DASHBOARD_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAllDeviceFragment(String user) {
        if (user == null || user.isEmpty())
            return;

        AllDeviceFragment allDeviceFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof AllDeviceFragment) {
                //nếu đã có thì refresh nó
                allDeviceFragment = (AllDeviceFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.ALL_DEVICE_FRAG.name());
                mFragmentManager.beginTransaction().detach(allDeviceFragment).attach(allDeviceFragment).commit();
            } else {
                allDeviceFragment = AllDeviceFragment.newInstance(mUser);
                visibleFragment(R.id.ll_content_main_frag, allDeviceFragment, true, Define.FRAGMENT_TAG.ALL_DEVICE_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.ALL_DEVICE_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showTrendsFragment(String user) {
        TrendsFragment trendsFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof TrendsFragment) {
                //nếu đã có thì refresh lại dữ liệu từ sever
                trendsFragment = (TrendsFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.TRENDS_FRAG.name());
                trendsFragment.refreshDataFromServer();
//                mFragmentManager.beginTransaction().detach(trendsFragment).attach(trendsFragment).commit();
            } else {
                trendsFragment = TrendsFragment.newInstance();
                visibleFragment(R.id.ll_content_main_frag, trendsFragment, true, Define.FRAGMENT_TAG.TRENDS_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.TRENDS_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showHistoryAlarmFragment(String user) {
        HistoryAndAlarmFragment historyAndAlarmFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
                //nếu đã có thì call server lây lại dữ liệu theo các thông số có sẵn
                historyAndAlarmFragment = (HistoryAndAlarmFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.HISTORY_ALARM_EVENT_FRAG.name());
                historyAndAlarmFragment.refreshDataFromServer();

//                mFragmentManager.beginTransaction().detach(historyAndAlarmFragment).attach(historyAndAlarmFragment).commit();
            } else {
                historyAndAlarmFragment = HistoryAndAlarmFragment.newInstance();
                visibleFragment(R.id.ll_content_main_frag, historyAndAlarmFragment, true, Define.FRAGMENT_TAG.HISTORY_ALARM_EVENT_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.HISTORY_ALARM_EVENT_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showUpdateInfoFragment(String user) {
        UpdateInfoFragment updateInfoFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof UpdateInfoFragment) {
                //nếu đã có thì refresh nó
                updateInfoFragment = (UpdateInfoFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.UPDATE_INFO_FRAG.name());
                mFragmentManager.beginTransaction().detach(updateInfoFragment).attach(updateInfoFragment).commit();
            } else {
                updateInfoFragment = UpdateInfoFragment.newInstance(user);
                visibleFragment(R.id.ll_content_main_frag, updateInfoFragment, true, Define.FRAGMENT_TAG.UPDATE_INFO_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.UPDATE_INFO_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDeviceDetailFragment(DeviceEntity deviceEntity) {
        DetailDeviceFragment detailDeviceFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof DetailDeviceFragment) {
                //nếu đã có thì refresh nó
                detailDeviceFragment = (DetailDeviceFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.DETAIL_DEVICE_FRAG.name());
                mFragmentManager.beginTransaction().detach(detailDeviceFragment).attach(detailDeviceFragment).commit();
            } else {
                detailDeviceFragment = DetailDeviceFragment.newInstance(mUser, deviceEntity);
                visibleFragment(R.id.ll_content_main_frag, detailDeviceFragment, true, Define.FRAGMENT_TAG.DETAIL_DEVICE_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DETAIL_DEVICE_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showHistoryDeviceFragment(int idDevice) {
        if (idDevice <= 0)
            return;

        HistoryDetailDeviceFragment historyDetailDeviceFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof HistoryDetailDeviceFragment) {
                //nếu đã có thì refresh nó
                historyDetailDeviceFragment = (HistoryDetailDeviceFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.HISTORY_DETAIL_DEVICE_FRAG.name());
                mFragmentManager.beginTransaction().detach(historyDetailDeviceFragment).attach(historyDetailDeviceFragment).commit();
            } else {
                historyDetailDeviceFragment = HistoryDetailDeviceFragment.newInstance(idDevice);
                visibleFragment(R.id.ll_content_main_frag, historyDetailDeviceFragment, true, Define.FRAGMENT_TAG.HISTORY_DETAIL_DEVICE_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.HISTORY_DETAIL_DEVICE_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showUpdatePassFragment(String user) {
        UpdatePassFragment updatePassFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof UpdatePassFragment) {
                //nếu đã có thì refresh nó
                updatePassFragment = (UpdatePassFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.UPDATE_PASS_FRAG.name());
                mFragmentManager.beginTransaction().detach(updatePassFragment).attach(updatePassFragment).commit();
            } else {
                updatePassFragment = UpdatePassFragment.newInstance(user);
                visibleFragment(R.id.ll_content_main_frag, updatePassFragment, true, Define.FRAGMENT_TAG.UPDATE_PASS_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.UPDATE_PASS_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showConnectedFragment(String user) {
        if (user == null || user.isEmpty())
            return;

        ConnectedFragment connectedFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof ConnectedFragment) {
                //nếu đã có thì refresh nó
                connectedFragment = (ConnectedFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.CONNECTED_FRAG.name());
                mFragmentManager.beginTransaction().detach(connectedFragment).attach(connectedFragment).commit();
            } else {
                connectedFragment = ConnectedFragment.newInstance(user);
                visibleFragment(R.id.ll_content_main_frag, connectedFragment, true, Define.FRAGMENT_TAG.CONNECTED_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.CONNECTED_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDisconnectedFragment(String user) {
        if (user == null || user.isEmpty())
            return;

        DisconnectedFragment disconnectedFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof DisconnectedFragment) {
                //nếu đã có thì refresh nó
                disconnectedFragment = (DisconnectedFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.DISCONNECTED_FRAG.name());
                mFragmentManager.beginTransaction().detach(disconnectedFragment).attach(disconnectedFragment).commit();
            } else {
                disconnectedFragment = DisconnectedFragment.newInstance(mUser);
                visibleFragment(R.id.ll_content_main_frag, disconnectedFragment, true, Define.FRAGMENT_TAG.DISCONNECTED_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DISCONNECTED_FRAG;
            showTitleFeature(mTagFragIsVisibling.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showEventOrAlarmFragment(String user, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY event) {
        if (user == null || user.isEmpty())
            return;

        EventAndAlarmTodayFragment eventAndAlarmTodayFragment;
        try {
            //kiểm tra fragment tại vị trí content_main_ll_fragment
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof EventAndAlarmTodayFragment) {
                //nếu đã có thì refresh nó
                eventAndAlarmTodayFragment = (EventAndAlarmTodayFragment) mFragmentManager.findFragmentByTag(Define.FRAGMENT_TAG.ALARM_OR_EVENT_VALUE_FRAG.name());
                eventAndAlarmTodayFragment.refreshData();
//                mFragmentManager.beginTransaction().detach(eventAndAlarmTodayFragment).attach(eventAndAlarmTodayFragment).commit();
            } else {
                eventAndAlarmTodayFragment = EventAndAlarmTodayFragment.newInstance(event);
                visibleFragment(R.id.ll_content_main_frag, eventAndAlarmTodayFragment, true, Define.FRAGMENT_TAG.ALARM_OR_EVENT_VALUE_FRAG);
            }

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.ALARM_OR_EVENT_VALUE_FRAG;
            showTitleFeature(event.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region private
    private void visibleFragment(int layout, Fragment fragment, boolean addToBackStack, Define.FRAGMENT_TAG tag) throws Exception {
        if (fragment == null)
            throw new Exception("fragment null!");
        if (layout == 0)
            throw new Exception(fragment.getClass().getName().toString() + " must define a layout!");
        if (tag == null)
            throw new Exception(fragment.getClass().getName().toString() + " fragment must define a tag!");

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        int backStackSize = mFragmentManager.getBackStackEntryCount();
        if (addToBackStack) {
            //check frag exists in backstack
            if (backStackSize == 0) {
                mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.addToBackStack(tag.name());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(layout, fragment, tag.name()).commit();
                //call executePendingTransactions because commit only schedule.....so getBackStackEntryCount return null
                mFragmentManager.executePendingTransactions();
            } else {
                //check position
                int positionFragInBackStack = -1;
                for (int entryFrag = 0; entryFrag < backStackSize; entryFrag++) {
                    String fragmentTag = mFragmentManager.getBackStackEntryAt(entryFrag).getName();
                    if (fragmentTag.equals(tag.name())) {
                        positionFragInBackStack = entryFrag;
                    }
                }
                //then pop stack that
                if (positionFragInBackStack >= 0) {
//                    mFragmentManager.popBackStackImmediate(positionFragInBackStack+ 1, 0);
                    for (int i = backStackSize; i > positionFragInBackStack + 1; i--) {
                        mFragmentManager.popBackStackImmediate();
                    }
                } else {
                    fragmentTransaction.addToBackStack(tag.name());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(layout, fragment, tag.name()).commit();
                    mFragmentManager.executePendingTransactions();
                }
            }
        } else {
            //nếu không muốn add back stack ta clear tất cả backstack
            for (int entryFrag = 0; entryFrag < backStackSize; entryFrag++) {
                mFragmentManager.popBackStackImmediate();
            }

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(layout, fragment, tag.name()).commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    private void refreshFragmentVisibling() {
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        String tagFragment = "";
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }
        tagFragment = fragmentVisibling.getTag();
        mIMainPresenter.callRefreshFragment(tagFragment);
    }
    //endregion

    //region DashboardFragment.OnDashboardFragmentListener
    @Override
    public void showConnectedFragmentFromDashboard(String user) {
        if (user == null || user.isEmpty())
            return;
        showConnectedFragment(user);
    }

    @Override
    public void showDisconnectedFragmentFromDashboard(String user) {
        if (user == null || user.isEmpty())
            return;
        showDisconnectedFragment(user);
    }

    @Override
    public void showEventValueFragmentFromDashboard(String user, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY type) {
        if (user == null || user.isEmpty())
            return;
        if (type == null)
            return;
        showEventOrAlarmFragment(user, type);
    }

    @Override
    public void getJSONMobileCountDevice() {
        mIMainPresenter.callJSONMobileCountDevice();
    }
    //endregion

    //region AllDeviceFragment.OnAllDeviceFragmentListener
    @Override
    public void showDeviceRecycler() {
        try {
            mIMainPresenter.callJSONAllDevice(mUser);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }
    //endregion

    //region ConnectedFragment.OnConnectedFragmentListener
    @Override
    public void showConnectRecycler(String user) {
        try {
            mIMainPresenter.callJSONDeviceOnOff(user, Define.STATE_GET_DEVICE.CONNECTED);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }
    //endregion

    //region UpdateInfoFragment.OnUpdateInfoFragmentListener
    @Override
    public void GetInfo(String user) {
        mIMainPresenter.callGetInfo(user);
    }

    @Override
    public void UpdateInfo(String user, String fullName, String phone, String email) {
        mIMainPresenter.callCommitInfoUpdate(user, fullName, phone, email);
    }

    @Override
    public void showDialogUpdateInfo(DialogEntity dialogEntity) {
        this.showDialogMessage(dialogEntity);
    }
    //endregion

    //region UpdatePassFragment.OnUpdatePassFragmentListener
    @Override
    public void updatePass(String user, String currentPass, String newPass, String retypeNewPass) {
        mIMainPresenter.callCommitPassUpdate(user, currentPass, newPass, retypeNewPass);
    }

    @Override
    public void showDialogUpdatePass(DialogEntity dialogEntity) {
        this.showDialogMessage(dialogEntity);
    }
    //endregion

    //region DisconnectedFragment.OnDisconnectedFragmentListener
    @Override
    public void showDisconnectRecycler(String user) {
        try {
            mIMainPresenter.callJSONDeviceOnOff(user, Define.STATE_GET_DEVICE.DISCONNECTED);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }

    //endregion

    //region DeviceAdapter.CallBackDeviceApdater
    @Override
    public void clickRowDevice(DeviceEntity deviceEntity) {
        if (deviceEntity == null)
            return;
        this.showDeviceDetailFragment(deviceEntity);
    }
    //endregion

    //region DetailDeviceFragment.OnDetailDeviceListener
    @Override
    public void showInfoDetailDevice(int idDevice) {
        if (idDevice <= 0) return;
        try {
            mIMainPresenter.callJSONInfoDetailDevice(mUser, idDevice);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }

    }

    @Override
    public void showInfoSpiner(int idDevice) {
        if (idDevice <= 0) return;
        try {
            mIMainPresenter.callJSONSpinnerParamDevice(mUser, idDevice);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }

    @Override
    public void showInfoRecyclerParam(int idDevice) {
        if (idDevice <= 0) return;
        try {
            mIMainPresenter.callJSONRecyclerParamDevice(mUser, idDevice);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }

    @Override
    public void showInfoRecyclerEvent(int idDevice) {
        if (idDevice <= 0) return;
        try {
            mIMainPresenter.callJSONRecyclerEventDevice(mUser, idDevice);
        } catch (Exception e) {
            DialogEntity dialogError = new DialogEntity.DialogBuilder(this, "Thông báo", e.getMessage()).build();
            showDialogMessage(dialogError);
        }
    }

    @Override
    public void callActionOnSpinnerOpened(Spinner spinner, int idDevice) {
        ((SpinnerDetailDeviceAdapter) spinner.getAdapter()).setPrompt(Define.PROMT_SPINER_PARAM_OPEN);
    }

    @Override
    public void showHistoryFragmentFromDetailDevice(int idDevice) {
        if (idDevice <= 0) return;

        showHistoryDeviceFragment(idDevice);
    }

    @Override
    public void callActionOnSpinnerClosed(final Spinner spinner, int idDevice) {
        if (spinner == null)
            return;
        if (idDevice <= 0)
            return;

        mIMainPresenter.callJSONParamHistoryToDayChart(spinner, mUser, idDevice);
    }


    //endregion

    //region SpinnerDetailDeviceAdapter.CallBackSpinnerDetailDeviceAdapter
  /*  @Override
    public void addLineChar(List<ObjectSpinnerParamJSONEntity> entityList, int posLineChart) {
        if (posLineChart < 0)
            return;
        if (entityList == null)
            return;

        //TODO gọi dữ liệu test trực tiếp
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            List<ParamHistoryEntity> paramHistoryEntity = ((DetailDeviceFragment) fragmentVisibling).getParamHistory(entityList.get(posLineChart).getTagNameOPCData());
            String param = entityList.get(posLineChart).getParamDescription();
            LineDataSet lineDataSet = mIMainPresenter.setDataLine(param, paramHistoryEntity);
            ((DetailDeviceFragment) fragmentVisibling).addDataLineChart(lineDataSet, paramHistoryEntity, param);
        }

    }

    @Override
    public void removeLineChar(List<ObjectSpinnerParamJSONEntity> entityList, int posLineChart) {
        if (posLineChart < 0)
            return;
        if (entityList == null)
            return;
        //TODO gọi dữ liệu test trực tiếp
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            String param = entityList.get(posLineChart).getParamDescription();
//            mIMainPresenter.processOnPostExecuteRecyclerEventDevice(paramHistoryEntity, param, timeStart, timeEnd);
            ((DetailDeviceFragment) fragmentVisibling).removeDataLineChart(param);
        }
    }

    @Override
    public void refreshLineChart(List<ObjectSpinnerParamJSONEntity> entityList, int posLineChart) {
        if (posLineChart < 0)
            return;
        if (entityList == null)
            return;

        //TODO gọi dữ liệu test trực tiếp
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof DetailDeviceFragment) {
            List<ParamHistoryEntity> paramHistoryEntity = ((DetailDeviceFragment) fragmentVisibling).getParamHistory(entityList.get(posLineChart).getTagNameOPCData());
            String param = entityList.get(posLineChart).getParamDescription();
            LineDataSet lineDataSet = mIMainPresenter.setDataLine(param, paramHistoryEntity);
            ((DetailDeviceFragment) fragmentVisibling).refreshDataLineChart(lineDataSet, paramHistoryEntity, param);
        }

    }
*/
    @Override
    public void refreshLineChart(int idDevice, List<ObjectSpinnerParamEntity> entityList, int posLineChart, boolean isShow) {
        if (idDevice < 1)
            return;
        if (entityList == null)
            return;
        if (posLineChart < 0 || posLineChart > entityList.size() - 1)
            return;

        //nếu cho phép show
        if (isShow)
//            mIMainPresenter.callJSONParamHistoryToDayChart(mUser, idDevice, entityList.get(posLineChart).getTagNameOPCData());
            mIMainPresenter.callGetParamSpinner(entityList, posLineChart);
//            mIMainPresenter.callJSONParamHistoryToDayChart(mUser, idDevice, entityList.get(posLineChart).getParamDescription());
        else {
            Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
            if (fragmentVisibling instanceof DetailDeviceFragment) {
                String param = entityList.get(posLineChart).getParamDescription();
                ((DetailDeviceFragment) fragmentVisibling).removeDataLineChart(param);
            }
        }
    }


    //endregion

    //region TrendsFragment.OnTrendsFragmentListener
    @Override
    public void showSpinnerDeviceTrends() {
        try {
            mIMainPresenter.callJSONAllDeviceTrends(mUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDateTimePickerFragmentFromTrendsFragment(int idTextView) {
        if (idTextView <= 0)
            return;

        DateTimePickerCustomFragment dateTimePickerCustomFragment;
        try {
            dateTimePickerCustomFragment = DateTimePickerCustomFragment.newInstance();

            DateTimePickerCustomFragment.DateTimePickerCustormListenerImp listenerImp = new DateTimePickerCustomFragment.DateTimePickerCustormListenerImp() {
                @Override
                public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
                    //TODO formatedMonth start from index 0
                    responseDataChooseDateTimeTrends(formatedYeah, formatedMonth, formatedDate);
                }
            };
            dateTimePickerCustomFragment.setmListener(listenerImp);

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG;

            dateTimePickerCustomFragment.show(getSupportFragmentManager(), Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showAllLineChartAndRecyclerTrends(int idDevice, String nameDevice, String startDate, String endDate) {
        if (idDevice <= 0)
            return;
        if (nameDevice == null || nameDevice.isEmpty())
            return;
        if (startDate == null || startDate.isEmpty())
            return;
        if (endDate == null || endDate.isEmpty())
            return;

        mIMainPresenter.callJSONAllParamHistoryChartTrends(idDevice, nameDevice, startDate, endDate);
    }

    @Override
    public boolean isCheckHasOrientedChange() {
        int statusScreenNow = getResources().getConfiguration().orientation;
        if (statusScreenNow == statusScreenPreviewRotate) {
            return false;
        }
        return true;
    }

    //endregion

    //region CallbackDeviceOnOffAdapter
    @Override
    public void clickRowDeviceOnOff(DeviceOnOffEntity deviceOnOffEntity) {
        if (deviceOnOffEntity == null)
            return;

        DeviceEntity deviceConvert = new DeviceEntity.DeviceBuilder(
                deviceOnOffEntity.getIpAddress(),
                deviceOnOffEntity.getName(),
                deviceOnOffEntity.getID(),
                deviceOnOffEntity.isStatus()).build();
        this.showDeviceDetailFragment(deviceConvert);
    }
    //endregion

    //region HistoryAndAlarmFragment.OnHistoryAndAlarmFragmentListener
    @Override
    public void showSpinnerDeviceHistoryAndAlarm() {
        try {
            mIMainPresenter.callJSONAllDeviceHistoryAlarm(mUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDateTimePickerFragmentFromHistoryAndAlarm(int idTextView) {
        if (idTextView <= 0)
            return;

        DateTimePickerCustomFragment dateTimePickerCustomFragment;
        try {
            dateTimePickerCustomFragment = DateTimePickerCustomFragment.newInstance();

            DateTimePickerCustomFragment.DateTimePickerCustormListenerImp listenerImp = new DateTimePickerCustomFragment.DateTimePickerCustormListenerImp() {
                @Override
                public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
                    //TODO formatedMonth start from index 0
                    responseDataChooseDateTimeHistoryAndAlarm(formatedYeah, formatedMonth, formatedDate);
                }
            };
            dateTimePickerCustomFragment.setmListener(listenerImp);

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG;

            dateTimePickerCustomFragment.show(getSupportFragmentManager(), Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void responseDataChooseDateTimeHistoryAndAlarm(int year, int month, int date) {
        if (month <= 0 || month > 12)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryAndAlarmFragment) {
            ((HistoryAndAlarmFragment) fragmentVisibling).setTextDateTimePickerHistoryAndAlarm(year, month, date);
        }
    }

    @Override
    public void showRecyclerHistoryAndAlarm(int idDevice, String nameDevice, String typeString, String beginDate, String endDate) {
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

        mIMainPresenter.callJSONHistoryAndAlarm(idDevice, nameDevice, typeString, beginDate, endDate);
    }

   /* @Override
    public void showDateTimePickerFragmentFromEventOrAlarmFragment(int idTextView) {
        if (idTextView <= 0)
            return;

        DateTimePickerCustomFragment dateTimePickerCustomFragment;
        try {
            dateTimePickerCustomFragment = DateTimePickerCustomFragment.newInstance();

            DateTimePickerCustomFragment.DateTimePickerCustormListenerImp listenerImp = new DateTimePickerCustomFragment.DateTimePickerCustormListenerImp() {
                @Override
                public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
                    //TODO formatedMonth start from index 0
                    responseDataChooseDateTimeOverValue(formatedYeah, formatedMonth, formatedDate);
                }
            };
            dateTimePickerCustomFragment.setmListener(listenerImp);

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG;

            dateTimePickerCustomFragment.show(getSupportFragmentManager(), Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public void showRecyclerFromHistoryEventOrAlarmTodayFragment(Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
        if (typeString == null)
            return;

        mIMainPresenter.callJSONHistoryEventAndAlarmToday(typeString);
    }

    /*private void responseDataChooseDateTimeOverValue(int year, int month, int date) {
        if (month <= 0 || month > 12)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof EventAndAlarmTodayFragment) {
            ((EventAndAlarmTodayFragment) fragmentVisibling).setTextDateTimePicker(year, month, date);
        }
    }*/

    //endregion

    //region HistoryDetailDeviceFragment.OnHistoryDetailDeviceListener
    @Override
    public void showDateTimePickerFragmentFromHistoryDetailDevice(int sIdTextViewChooseDate) {
        if (sIdTextViewChooseDate <= 0)
            return;

        DateTimePickerCustomFragment dateTimePickerCustomFragment;
        try {
            dateTimePickerCustomFragment = DateTimePickerCustomFragment.newInstance();

            DateTimePickerCustomFragment.DateTimePickerCustormListenerImp listenerImp = new DateTimePickerCustomFragment.DateTimePickerCustormListenerImp() {
                @Override
                public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
                    //TODO formatedMonth start from index 0
                    responseDataChooseDateTimeHistoryDetailDevice(formatedYeah, formatedMonth, formatedDate);
                }
            };
            dateTimePickerCustomFragment.setmListener(listenerImp);

            //đánh dấu tag
            mTagFragIsVisibling = Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG;

            dateTimePickerCustomFragment.show(getSupportFragmentManager(), Define.FRAGMENT_TAG.DATE_TIME_PICKED_FRAG.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showRecyclerHistoryDetailDevice(int idDevice, String beginDate, String endDate) {
        if (idDevice <= 0)
            return;
        if (beginDate == null || beginDate.isEmpty())
            return;
        if (endDate == null || endDate.isEmpty())
            return;

        beginDate = CommonMethod.convertDateToDate(beginDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);
        endDate = CommonMethod.convertDateToDate(endDate, Define.TYPE_DATE_TIME_DD_MM_YYYY, Define.TYPE_DATE_TIME_YYYY_MM_DD);

        mIMainPresenter.callJSONHistoryDetailDevice(idDevice, beginDate, endDate);
    }

    private void responseDataChooseDateTimeHistoryDetailDevice(int year, int month, int date) {
        if (month <= 0 || month > 12)
            return;

        //check fragment visibling
        Fragment fragmentVisibling = mFragmentManager.findFragmentById(R.id.ll_content_main_frag);
        if (fragmentVisibling instanceof HistoryDetailDeviceFragment) {
            ((HistoryDetailDeviceFragment) fragmentVisibling).setTextDateTimePicker(year, month, date);
        }

    }
    //endregion
}
