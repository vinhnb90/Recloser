package esolutions.com.recloser.Presenter.Interface;

import android.widget.EditText;

import esolutions.com.recloser.Utils.Inteface.ICommonPresenter;

/**
 * Created by VinhNB on 3/7/2017.
 */

public interface IConfigPresenter extends ICommonPresenter{
    void validateIPConfig(String ipURL);

    void callShowSharedPrefConfig();
}
