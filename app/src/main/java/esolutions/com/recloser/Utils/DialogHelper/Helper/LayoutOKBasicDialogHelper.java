package esolutions.com.recloser.Utils.DialogHelper.Helper;

        import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
        import esolutions.com.recloser.Utils.DialogHelper.Imp.LayoutOKBasicImpDialog;
        import esolutions.com.recloser.Utils.DialogHelper.Imp.DialogImp;
        import esolutions.com.recloser.Utils.DialogHelper.Inteface.IActionClickYesDialog;

/**
 * Created by VinhNB on 2/28/2017.
 */

public class LayoutOKBasicDialogHelper extends DialogHelper{
    private DialogEntity mDialogEntity;
    public LayoutOKBasicDialogHelper(DialogEntity dialogEntity) {
        if (dialogEntity == null)
            return;
        this.mDialogEntity = dialogEntity;
    }

    @Override
    public DialogImp build() {
        return new LayoutOKBasicImpDialog(mDialogEntity);
    }
}
