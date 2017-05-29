package esolutions.com.recloser.Utils.Inteface;

import android.content.Context;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;

/**
 * Interface dùng chung các method cơ bản của 1 activity
 * Created by VinhNB on 2/15/2017.
 */

public interface ICommonView extends ICommonContext{
    public void hideActionBar();

    //với toast thì chỉ giới hạn 2 cách cố định là hiện message với thời gian mặc định hoặc 1 thời gian tùy chỉnh
    public void showToast(String message);
    public void showToast(String message, long time);
    //show dialog
    public void showDialogMessage(DialogEntity dialogEntity);
}
