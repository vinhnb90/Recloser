package esolutions.com.recloser.Model.Inteface;

import android.graphics.Bitmap;

import java.util.List;

import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Entity.DeviceTestEntity;
import esolutions.com.recloser.Entity.HeaderNavigationMenuEntity;
import esolutions.com.recloser.Utils.Inteface.ICommonDBSqlite;
import esolutions.com.recloser.Utils.Inteface.ICommonSharedReference;

/**
 * Created by VinhNB on 2/18/2017.
 */

public interface IMainModel extends ICommonDBSqlite, ICommonSharedReference{
    List<HeaderNavigationMenuEntity> getDataNaviMenu();
    Bitmap getAvatarDevice(String imageName);
}
