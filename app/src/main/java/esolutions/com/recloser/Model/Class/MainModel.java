package esolutions.com.recloser.Model.Class;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.HeaderNavigationMenuEntity;
import esolutions.com.recloser.Model.DBConnection;
import esolutions.com.recloser.Model.Inteface.IMainModel;
import esolutions.com.recloser.Model.SharePrefManager;
import esolutions.com.recloser.Presenter.Interface.IMainPresenter;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 2/18/2017.
 */

public class MainModel implements IMainModel {
    private IMainPresenter mIMainPresenter;
    private DBConnection mDBConnection;
    private List<HeaderNavigationMenuEntity> mHeaderNavigationMenuEntities;

    public MainModel(IMainPresenter mIMainPresenter) {
        this.mIMainPresenter = mIMainPresenter;
        mDBConnection = DBConnection.getInstance(this.mIMainPresenter.getContextView());
    }

    @Override
    public List<HeaderNavigationMenuEntity> getDataNaviMenu() {
        setDataNaviMenu();
        return mHeaderNavigationMenuEntities;
    }

    @Override
    public Bitmap getAvatarDevice(String imageName) {
        Bitmap bitmap = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + Define.PROGRAM_PHOTOS_PATH);
        if (!myDir.exists())
            myDir.mkdirs();
        File file = new File(myDir, imageName);
        if (file.exists()) {
            bitmap = CommonMethod.getAvatar(root + Define.PROGRAM_PHOTOS_PATH + imageName);
        }
        return bitmap;
    }

    private void setDataNaviMenu() {
        if (mHeaderNavigationMenuEntities == null)
            mHeaderNavigationMenuEntities = new ArrayList<>();
        HeaderNavigationMenuEntity main = new HeaderNavigationMenuEntity.HeaderMenuBuilder(Define.ORDER_MENU_NAVIGATION.MENU_LV1_MAIN.toString(), "Main").
                addElementMenuLv2(Define.ORDER_MENU_NAVIGATION.MENU_LV2_DASHBOARD.toString(), "Dashboard").
                addElementMenuLv2(Define.ORDER_MENU_NAVIGATION.MENU_LV2_ALL_DEVICE.toString(), "All Device").
                build();

        HeaderNavigationMenuEntity report = new HeaderNavigationMenuEntity.HeaderMenuBuilder(Define.ORDER_MENU_NAVIGATION.MENU_LV1_REPORT.toString(), "Report").
                addElementMenuLv2(Define.ORDER_MENU_NAVIGATION.MENU_LV2_TREND.toString(), "Trend").
                addElementMenuLv2(Define.ORDER_MENU_NAVIGATION.MENU_LV2_HISTORY_ALARM_EVENT.toString(), "History alarm and Event").
                build();

        mHeaderNavigationMenuEntities.add(main);
        mHeaderNavigationMenuEntities.add(report);
    }


    @Override
    public DBConnection callSqlite() {
        return mDBConnection;
    }

    @Override
    public SharePrefManager callManagerSharedPref() {
        return SharePrefManager.getInstance(mIMainPresenter.getContextView());
    }
}
