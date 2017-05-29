package esolutions.com.recloser.Model.Class;

import esolutions.com.recloser.Model.Inteface.IConfigModel;
import esolutions.com.recloser.Model.SharePrefManager;
import esolutions.com.recloser.Presenter.Interface.IConfigPresenter;

/**
 * Created by VinhNB on 3/7/2017.
 */

public class ConfigModel implements IConfigModel{
    private IConfigPresenter mIConfigPresenter;

    public ConfigModel(IConfigPresenter mIConfigPresenter) {
        if(mIConfigPresenter == null)
            return;
        this.mIConfigPresenter = mIConfigPresenter;
    }

    @Override
    public SharePrefManager callManagerSharedPref() {
        return SharePrefManager.getInstance(mIConfigPresenter.getContextView());
    }
}
