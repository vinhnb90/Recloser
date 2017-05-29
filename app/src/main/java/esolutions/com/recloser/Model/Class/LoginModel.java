package esolutions.com.recloser.Model.Class;

import esolutions.com.recloser.Model.DBConnection;
import esolutions.com.recloser.Model.Inteface.ILoginModel;
import esolutions.com.recloser.Model.SharePrefManager;
import esolutions.com.recloser.Presenter.Interface.ILoginPresenter;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class LoginModel implements ILoginModel{
    private ILoginPresenter mILoginPresenter;

    public LoginModel(ILoginPresenter mILoginPresenter) {
        this.mILoginPresenter = mILoginPresenter;
    }

    @Override
    public DBConnection callSqlite() {
        return DBConnection.getInstance(mILoginPresenter.getContextView());
    }

    @Override
    public SharePrefManager callManagerSharedPref() {
        return SharePrefManager.getInstance(mILoginPresenter.getContextView());
    }
}
