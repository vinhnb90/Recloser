package esolutions.com.recloser.Utils.DialogHelper.Imp;

import android.app.AlertDialog;

import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;

/**
 * lọ hoa không có mặc định một loại hoa
 * Created by VinhNB on 2/28/2017.
 */
public class BasicImpDialog extends DialogImp {

    public BasicImpDialog(DialogEntity dialogEntity) {
//        mDialogEntity = new DialogEntity.DialogBuilder(ApplicationDefault.getContext(), TITLE_DEFAULT, MESAGE_DEFAULT).build();
        mDialogEntity = dialogEntity;
    }

    @Override
    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mDialogEntity.getmContext());
        builder.setTitle(mDialogEntity.getmTitle());
        builder.setMessage(mDialogEntity.getmMessage());
        builder.setNeutralButton(Define.STRING_DIALOG_HELPER.OK.toString(), null);
        builder.show();
    }
}