package esolutions.com.recloser.Utils.DialogHelper.Helper;

import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Imp.DialogImp;
import esolutions.com.recloser.Utils.DialogHelper.Imp.LayoutOKActionImpDialog;
import esolutions.com.recloser.Utils.DialogHelper.Inteface.IActionClickYesDialog;

/**
 * Created by VinhNB on 3/6/2017.
 */

public class LayoutOKActionDialogHelper extends DialogHelper {
    private DialogEntity mDialogEntity;
    private IActionClickYesDialog mActionClickYesDialog;
    public LayoutOKActionDialogHelper(DialogEntity dialogEntity, IActionClickYesDialog actionClickYesDialog) {
        if (dialogEntity == null) {
            return;
        }
        if (actionClickYesDialog == null) {
            return;
        }
        this.mDialogEntity = dialogEntity;
        this.mActionClickYesDialog = actionClickYesDialog;
    }

    @Override
    public DialogImp build() {
        return new LayoutOKActionImpDialog(this.mDialogEntity, this.mActionClickYesDialog);
    }
}
