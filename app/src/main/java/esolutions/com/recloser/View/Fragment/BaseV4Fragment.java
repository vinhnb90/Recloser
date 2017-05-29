package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import esolutions.com.recloser.Entity.DetailDeviceEntity;
import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Helper.BasicDialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Helper.DialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Imp.BasicImpDialog;
import esolutions.com.recloser.Utils.DialogHelper.Imp.DialogImp;

public abstract class BaseV4Fragment extends Fragment {

    protected abstract void initViewV4Fragment(final View view);

    protected abstract void initSoureV4Fragment();

    protected abstract void setActionV4Fragment(@Nullable Bundle savedInstanceState);



}
