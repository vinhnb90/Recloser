package esolutions.com.recloser.Utils.DialogHelper.Helper;

import android.app.Dialog;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Imp.BasicImpDialog;
import esolutions.com.recloser.Utils.DialogHelper.Imp.DialogImp;

/**
 * Created by VinhNB on 2/28/2017.
 */

public class BasicDialogHelper extends DialogHelper{
    private DialogEntity mDialogEntity;

    public BasicDialogHelper(DialogEntity mDialogEntity) {
        if (mDialogEntity == null) {
            return;
        }
        this.mDialogEntity = mDialogEntity;
    }

    @Override
    public DialogImp build() {
        return new BasicImpDialog(this.mDialogEntity);
    }
}
