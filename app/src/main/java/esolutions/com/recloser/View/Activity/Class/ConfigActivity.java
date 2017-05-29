package esolutions.com.recloser.View.Activity.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import esolutions.com.recloser.Model.ImageBlurHolderModer;
import esolutions.com.recloser.Presenter.Class.ConfigPresenter;
import esolutions.com.recloser.Presenter.Interface.IConfigPresenter;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.View.Activity.Interface.IConfigView;

public class ConfigActivity extends BaseActivityAppCompat implements IConfigView, View.OnClickListener {
    private RelativeLayout mRlConfig;
    private Button mBtnSaveIp;
    private EditText mEtIP;

    private IConfigPresenter mIConfigPresenter;
    private ImageBlurHolderModer mBlurHolderModer;
    private Bitmap mBackgroundBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_config);
        initView();
        initSource();
        setAction(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIConfigPresenter.callShowSharedPrefConfig();
    }

    @Override
    protected void initView() {
        mRlConfig = (RelativeLayout) findViewById(R.id.rl_activity_config);
        mBtnSaveIp = (Button) findViewById(R.id.btn_activity_config_save);
        mEtIP = (EditText) findViewById(R.id.et_activity_config_ip);
    }

    @Override
    protected void initSource() {
        mIConfigPresenter = new ConfigPresenter(this);
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
    protected void setAction(Bundle savedInstanceState) {
        //get img_background_dashboard blur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRlConfig.setBackground(new BitmapDrawable(this.getResources(), mBackgroundBlur));
        } else {
            mRlConfig.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundBlur));
        }

        mBtnSaveIp.setOnClickListener(this);
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
        super.showDialogMessageParent(dialogEntity);
    }

    @Override
    public Context getContextView() {
        return super.getContextParent();
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_view_push));
        switch (v.getId()) {
            case R.id.btn_activity_config_save:
                mIConfigPresenter.validateIPConfig(mEtIP.getText().toString());
                break;
        }
    }

    @Override
    public void showErrorInputIP(String message) {
        if (message == null || message.isEmpty())
            return;
        mEtIP.setError(message);
    }

    @Override
    public void showSuccessInputIP(String message) {
        if (message == null || message.isEmpty())
            return;
        super.showToastParent(message, Define.TIME_LIMIT_TOAST);
    }

    @Override
    public void setInfoResumeConfig(String stringIP) {
        if (stringIP == null || stringIP.isEmpty())
            return;
        mEtIP.setText(stringIP);
    }

}
