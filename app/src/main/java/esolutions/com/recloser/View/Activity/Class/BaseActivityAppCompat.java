package esolutions.com.recloser.View.Activity.Class;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Helper.BasicDialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Helper.DialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Helper.LayoutOKActionDialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Helper.LayoutOKBasicDialogHelper;

/**
 * class Base này là class dùng chung cho 1 số class Activity kế thừa nó...
 * class được đặt abstract để dùng mô tả riêng cho một số activity cần dùng.
 * những method trong ICommonView thì không quan trọng đến mức phải đặt private hay protected
 * và cũng không công cộng đến mức đặt trong class static vì nó phụ thuộc vào từng kiểu loại Activity extends.
 * <p>
 * Created by VinhNB on 2/15/2017.
 */

public abstract class BaseActivityAppCompat extends AppCompatActivity {

    //region imp method ICommonView
    protected void hideActionBarParent() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    protected Context getContextParent() {
        return this;
    }


    protected void showToastParent(String message) {
        if (message.equals("") || message.isEmpty())
            return;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    protected void showToastParent(final String message, final long time) {
        if (message.isEmpty() || time < 0)
            return;

        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                toast.cancel();
            }
        }, time);
    }

    protected void showDialogMessageParent(DialogEntity dialogEntity) {
        DialogHelper dialogHelper = new LayoutOKBasicDialogHelper(dialogEntity);
        dialogHelper.build().show();
    }

    //endregion
    protected abstract void initView();

    protected abstract void initSource();

    protected abstract void setAction(@Nullable Bundle savedInstanceState);


}
