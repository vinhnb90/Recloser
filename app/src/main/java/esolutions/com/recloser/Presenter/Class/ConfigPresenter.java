package esolutions.com.recloser.Presenter.Class;

import android.content.Context;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import esolutions.com.recloser.Model.Class.ConfigModel;
import esolutions.com.recloser.Model.Inteface.IConfigModel;
import esolutions.com.recloser.Presenter.Interface.IConfigPresenter;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.View.Activity.Interface.IConfigView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 3/7/2017.
 */

public class ConfigPresenter implements IConfigPresenter {
    private WeakReference<IConfigView> iConfigViewWeakReference;
    private IConfigModel mIConfigModel;
    public ConfigPresenter(IConfigView iConfigView) {
        iConfigViewWeakReference = new WeakReference<IConfigView>(iConfigView);
        mIConfigModel = new ConfigModel(this);
        mIConfigModel.callManagerSharedPref().addSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE);
    }

    @Override
    public Context getContextView() {
        return iConfigViewWeakReference.get().getContextView();
    }

    @Override
    public void validateIPConfig(String ipURL) {
        if (ipURL == null)
            return;
        if (ipURL.isEmpty()||ipURL.trim().equals("")){
            iConfigViewWeakReference.get().showErrorInputIP("Yêu cầu không nhập rỗng!");
            return;
        }

        mIConfigModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_CONFIG_IP, ipURL).commit();
        iConfigViewWeakReference.get().showSuccessInputIP("Cài đặt IP thành công!");
    }

    @Override
    public void callShowSharedPrefConfig() {
        String stringIP = mIConfigModel.
                callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE).
                getString(Define.SHARE_REF_FILE_CONFIG_IP, "");
        iConfigViewWeakReference.get().setInfoResumeConfig(stringIP);
    }
}
