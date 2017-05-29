package esolutions.com.recloser.View.Activity.Interface;

import esolutions.com.recloser.Utils.Inteface.ICommonView;

/**
 * Created by VinhNB on 2/15/2017.
 */

public interface IConfigView extends ICommonView{
    void showErrorInputIP(String message);

    void showSuccessInputIP(String message);

    void setInfoResumeConfig(String stringIP);
}
