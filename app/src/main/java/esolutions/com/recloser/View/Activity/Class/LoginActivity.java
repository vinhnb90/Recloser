package esolutions.com.recloser.View.Activity.Class;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import esolutions.com.recloser.Model.ImageBlurHolderModer;
import esolutions.com.recloser.Presenter.Class.LoginPresenter;
import esolutions.com.recloser.Presenter.Interface.ILoginPresenter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.Utils.DialogHelper.Helper.DialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Helper.LayoutOKActionDialogHelper;
import esolutions.com.recloser.Utils.DialogHelper.Inteface.IActionClickYesDialog;
import esolutions.com.recloser.View.Activity.Interface.ILoginView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends BaseActivityAppCompat implements ILoginView, View.OnClickListener {
    private RelativeLayout mRlLogin;
    private EditText mEtUser, mEtPass;
    private Button mBtnLogin, mBtnConfig;
    private ProgressBar mPbarProcess;

    private ILoginPresenter mILoginPresenter;
    private ImageBlurHolderModer mBlurHolderModer;
    private Bitmap mBackgroundBlur;

    //region method LoginActivity


    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(newBase);
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_login);
        initView();
        initSource();
        setAction(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mILoginPresenter.callShowSharedPrefLogin();
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_view_push));
        switch (v.getId()) {
            case R.id.activity_login_btn_login:
                mILoginPresenter.validateUserPass(mEtUser.getText().toString(), mEtPass.getText().toString());
                break;
            case R.id.activity_login_btn_config:
                mILoginPresenter.callConfig();
                break;
        }
    }

    public Context getContextParent() {
        return super.getContextParent();
    }
    //endregion

    //region override method abstract class BaseActivityAppCompat
    @Override
    public void initView() {
        mRlLogin = (RelativeLayout) findViewById(R.id.rl_activity_login);
        mEtUser = (EditText) findViewById(R.id.activity_login_et_user);
        mEtPass = (EditText) findViewById(R.id.activity_login_et_pass);
        mBtnConfig = (Button) findViewById(R.id.activity_login_btn_config);
        mBtnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mPbarProcess = (ProgressBar) findViewById(R.id.activity_login_pbar_process);
    }

    @Override
    public void initSource() {
        mILoginPresenter = new LoginPresenter(this);

        //set img_background_dashboard blur
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.img_background_dashboard);
        try {
            mBlurHolderModer = ImageBlurHolderModer.getInstance(this);
            mBlurHolderModer.setsBackgroundBlur(this, background, Define.BLUR_RADIUS.BACKGROUND_LOGIN_RADIUS.getValues());
            if (mBackgroundBlur != null)
                mBackgroundBlur.recycle();
            mBackgroundBlur = mBlurHolderModer.getsBackgroundBlur();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setAction(Bundle savedInstanceState) {
        mBtnConfig.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        //get img_background_dashboard blur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRlLogin.setBackground(new BitmapDrawable(this.getResources(), mBackgroundBlur));
        } else {
            mRlLogin.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundBlur));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mILoginPresenter.resetValueOnDestroy();

    }
    //endregion

    //region override method inteface ILoginView
    @Override
    public void showErorUserInput(String message) {
        if (message == null || message.isEmpty())
            return;
        mEtUser.setError(message);
    }

    @Override
    public void showErrorPassInput(String message) {
        if (message == null || message.isEmpty())
            return;
        mEtPass.setError(message);
    }

    @Override
    public void showPbarProcess() {
        if (mPbarProcess.getVisibility() == View.INVISIBLE)
            mPbarProcess.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePbarProcess() {
        if (mPbarProcess.getVisibility() == View.VISIBLE)
            mPbarProcess.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openMainActivty(String userName) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Define.PARAM_NAME_USER, userName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openConfigActivity() {
        Intent intent = new Intent(LoginActivity.this, ConfigActivity.class);
        startActivity(intent);
    }

    @Override
    public void setInfoResumeLogin(String userName, String pass) {
        if (userName.equals("") || userName.isEmpty()) {
            return;
        }
        if (pass.equals("") || pass.isEmpty()) {
            return;
        }

        mEtUser.setText(userName);
        mEtPass.setText(pass);
    }

    @Override
    public void showDialogLogin(DialogEntity dialogEntity) {
        if (dialogEntity == null)
            return;

        DialogHelper dialogHelper = new LayoutOKActionDialogHelper(dialogEntity, new LoginActivity.DialogProcessLogin());
        dialogHelper.build().show();
    }

    @Override
    public void responseNotifyErrorLogin(String message, boolean isResponse) {
        if (message == null || message.isEmpty())
            return;
        DialogEntity dialogEntity = new DialogEntity.DialogBuilder(this, Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), message).build();
        this.showDialogMessage(dialogEntity);
    }

    @Override
    public void hideActionBar() {
        super.hideActionBarParent();
    }

    @Override
    public void showToast(String message) {
        super.showToastParent(message);
    }

    @Override
    public void showToast(String message, long time) {
        super.showToastParent(message, time);
    }

    @Override
    public void showDialogMessage(DialogEntity dialogEntity) {
        if (dialogEntity == null)
            return;
        super.showDialogMessageParent(dialogEntity);
    }

    @Override
    public Context getContextView() {
        return super.getContextParent();
    }

    class DialogProcessLogin implements IActionClickYesDialog {

        @Override
        public void doClickYes() {
            LoginActivity.this.openMainActivty(LoginActivity.this.mEtUser.getText().toString());
        }
    }

    //endregion


}
