package esolutions.com.recloser.View.Activity.Interface;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.Inteface.ICommonView;

/**
 * Created by VinhNB on 2/15/2017.
 *
 * Nội dung: set các hiển thị trên view Login
 */

public interface ILoginView extends ICommonView {
    //user et và pass et
    void showErorUserInput(String message);
    void showErrorPassInput(String message);

    //progress bar
    void showPbarProcess();
    void hidePbarProcess();

    void openMainActivty(String userName);
    void openConfigActivity();

    //set edit text shared Pref
    void setInfoResumeLogin(String userName, String pass);

    //show dialogLogin
    void showDialogLogin(DialogEntity dialogEntity);

    void responseNotifyErrorLogin(String message, boolean isResponse);
}
