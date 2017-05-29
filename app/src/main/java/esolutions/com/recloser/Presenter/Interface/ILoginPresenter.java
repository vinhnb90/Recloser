package esolutions.com.recloser.Presenter.Interface;

import esolutions.com.recloser.Presenter.Class.LoginPresenter;
import esolutions.com.recloser.Utils.Inteface.ICommonPresenter;

/**
 * Created by VinhNB on 2/15/2017.
 */

public interface ILoginPresenter extends ICommonPresenter{
    void validateUserPass(String userName, String pass);
    void saveSharedPreference();
    void callConfig();
    boolean canLoginOffline(String userName, String pass) throws Exception;
    void callShowSharedPrefLogin();
    void resetValueOnDestroy();
}
