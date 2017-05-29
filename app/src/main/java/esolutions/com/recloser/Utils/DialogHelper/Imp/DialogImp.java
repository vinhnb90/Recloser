package esolutions.com.recloser.Utils.DialogHelper.Imp;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Inteface.IActionClickYesDialog;

/**
 * Created by VinhNB on 2/28/2017.
 */

public abstract class DialogImp {


    protected DialogEntity mDialogEntity;
    protected IActionClickYesDialog iActionClickYesDialog;
    public abstract void show();
}
