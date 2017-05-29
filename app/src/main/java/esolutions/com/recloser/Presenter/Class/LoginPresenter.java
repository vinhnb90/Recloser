package esolutions.com.recloser.Presenter.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import esolutions.com.recloser.Entity.ObjectPublishAsyntask;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.Model.Class.LoginModel;
import esolutions.com.recloser.Model.Inteface.ILoginModel;
import esolutions.com.recloser.Presenter.Interface.ILoginPresenter;
import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;
import esolutions.com.recloser.View.Activity.Class.LoginActivity;
import esolutions.com.recloser.View.Activity.Interface.ILoginView;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class LoginPresenter implements ILoginPresenter {
    private WeakReference<ILoginView> mILoginViewWeakReference;
    private ILoginModel mILoginModel;
    private Thread mLoginThread;

    public LoginPresenter(ILoginView iLoginView) {
        mILoginViewWeakReference = new WeakReference<>(iLoginView);
        mILoginModel = new LoginModel(this);
        mILoginModel.callManagerSharedPref().addSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE);
    }

    @Override
    public void validateUserPass(final String userName, final String pass) {

        if (userName == null || userName.isEmpty() || userName.trim().equals("")) {
            mILoginViewWeakReference.get().showErorUserInput("Vui lòng không để trống.");
            return;
        }

        if (pass == null || pass.isEmpty() || pass.trim().equals("")) {
            mILoginViewWeakReference.get().showErrorPassInput("Vui lòng không nhập rỗng.");
            return;
        }

        //check wifi and login offline
        if (!CommonMethod.isConnectingWifi(mILoginViewWeakReference.get().getContextView())) {
//                if (canLoginOffline(userName, userName)) {
//                    dialogEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Wifi không khả dụng. Tài khoản có thể đăng nhập offline.").build();
//                } else {
//                    dialogEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Vui lòng kiểm tra wifi kết nối internet.").build();
//                }
//                mILoginViewWeakReference.get().showDialogMessage(dialogEntity);
            DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Đăng nhập thất bại!\nNội dung: Wifi chưa cài đặt mạng internet.").build();
            mILoginViewWeakReference.get().showDialogMessage(dialogMessageEntity);
            return;
        }

        if (mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE) == null) {
            DialogEntity dialogMessageEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Vui lòng cài đặt cấu hình địa chỉ IP.").build();
            mILoginViewWeakReference.get().showDialogMessage(dialogMessageEntity);
            return;
        }

        final String stringIP = mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_CONFIG, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_CONFIG_IP, "");
        if (stringIP.isEmpty()) {
            DialogEntity dialogEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Vui lòng config địa chỉ IP máy chủ.").build();
            mILoginViewWeakReference.get().showDialogMessage(dialogEntity);
            return;
        }

        ConnectServer.AsyncCallLogin.ProcessAsyncLogin processAsyncLogin = new ConnectServer.AsyncCallLogin.ProcessAsyncLogin() {
            @Override
            public void processOnPreExecuteLogin(final ConnectServer.AsyncCallLogin asyncCallLogin) {
                mILoginViewWeakReference.get().showPbarProcess();

                if (!CommonMethod.isConnectingWifi(mILoginViewWeakReference.get().getContextView())) {
                    mILoginViewWeakReference.get().hidePbarProcess();
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra wifi.");
                    objectPublishAsyntask.setResponse(true);
                    mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    asyncCallLogin.cancel(true);
                    asyncCallLogin.setmHasReveicedResponseServer(true);
                }
            }

            @Override
            public void processOnProgressUpdateLogin(ObjectPublishAsyntask... values) {
                //TODO set UI
                mILoginViewWeakReference.get().hidePbarProcess();
                ObjectPublishAsyntask objectPublishAsyntask = values[0];
                String userName = mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "");
                String passWord = mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_LOGIN_PASS, "");
//            try {
//                //TODO login offline
//                if (canLoginOffline(userName, passWord)) {
//                    dialogLoginOfflineEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), "Thông báo", "Không thể kết nối được server (" + Define.TIME_LIMIT / 1000 + "s), \nTài khoản này có thể đăng nhập offline").build();
//                    mILoginViewWeakReference.get().showDialogLogin(dialogLoginOfflineEntity);
//                } else {
//                    dialogMessageEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), "Thông báo", message).build();
//                    mILoginViewWeakReference.get().showDialogMessage(dialogMessageEntity);
//                }
//            } catch (Exception e) {
//                dialogMessageEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), "Thông báo", e.getMessage()).build();
//                mILoginViewWeakReference.get().showDialogMessage(dialogMessageEntity);
//            }
                //clear shared Ref
                mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "").commit();
                mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_PASS, "").commit();
                mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);

                //stop thread
                if (mLoginThread.isAlive()) {
                    mLoginThread.interrupt();
                }
            }

            @Override
            public void processOnPostExecuteLogin(ResponseServerLoginJSON responseServerLoginJSON, String userName, String passWord) {
                mILoginViewWeakReference.get().hidePbarProcess();

                ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                if (responseServerLoginJSON == null) {
                    objectPublishAsyntask.setMessage("Không nhận được dữ liệu từ máy chủ!");
                    objectPublishAsyntask.setResponse(true);
                    mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }
                if (responseServerLoginJSON.getResult() == false) {
                    //clear share ref
                    mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "").commit();
                    mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_PASS, "").commit();

                    objectPublishAsyntask.setMessage("Vui lòng kiểm tra lại thông tin tài khoản!");
                    objectPublishAsyntask.setResponse(true);
                    mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                    return;
                }

                //TODO Write database
                long rowAffect = 0;
                try {
                    //update shared ref
                    mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, userName).commit();
                    mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_PASS, passWord).commit();
                    boolean userExist = mILoginModel.callSqlite().isRowExistData(Define.queryCheckUser(userName));
                    ContentValues contentValuesUser = new ContentValues();
                    contentValuesUser.put(Define.TABLE_USER_UserName, userName);
                    contentValuesUser.put(Define.TABLE_USER_Password, passWord);
                    contentValuesUser.put(Define.TABLE_USER_FullName, "");
                    contentValuesUser.put(Define.TABLE_USER_Email, "");
                    contentValuesUser.put(Define.TABLE_USER_PhoneNumber, "");
                    contentValuesUser.put(Define.TABLE_USER_Status, "");
                    contentValuesUser.put(Define.TABLE_USER_UsedApp, "");
                    contentValuesUser.put(Define.TABLE_USER_UserType, "");
                    contentValuesUser.put(Define.TABLE_USER_MobileVersion, "");
                    if (userExist) {
                        rowAffect = mILoginModel.callSqlite().updateData(contentValuesUser, Define.TABLE_USER, Define.TABLE_USER_UserName, userName);
                    } else {
                        rowAffect = mILoginModel.callSqlite().insertData(contentValuesUser, Define.TABLE_USER);
                    }

                    if (rowAffect <= 0)
                        throw new Exception("Đăng nhập không thành công. Dữ liệu bị lỗi");

                    //TODO add to sharePref
                    mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                            .edit()
                            .putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, userName)
                            .putString(Define.SHARE_REF_FILE_LOGIN_PASS, passWord)
                            .commit();

                    mILoginViewWeakReference.get().openMainActivty(userName);
                } catch (Exception e) {
                    objectPublishAsyntask.setMessage(e.getMessage());
                    objectPublishAsyntask.setResponse(false);
                    mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                } finally {
                    mILoginModel.callSqlite().close();
                }

                //stop thread
                if (mLoginThread.isAlive()) {
                    mLoginThread.interrupt();
                }
            }

            @Override
            public void processOnCountdownLogin(final ConnectServer.AsyncCallLogin asyncCallLogin) {
//TODO login offline
                try {
//            String userName = mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "");
//            String passWord = mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).getString(Define.SHARE_REF_FILE_LOGIN_PASS, "");
//            boolean isLoginOffline = canLoginOffline(userName, passWord);
//            mILoginViewWeakReference.get().hidePbarProcess();
//            if (isLoginOffline) {
//                asyncCallLogin.cancel(true);
//                //TODO add to sharePref
//                mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
//                        .edit()
//                        .putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, userName)
//                        .putString(Define.SHARE_REF_FILE_LOGIN_PASS, passWord)
//                        .commit();
//                DialogEntity dialogLoginOfflineEntity = new DialogEntity.DialogBuilder(mILoginViewWeakReference.get().getContextView(), "Thông báo", "Không kết nối được server (" + Define.TIME_LIMIT / 1000 + "s), \nTài khoản này có thể đăng nhập offline").build();
//                mILoginViewWeakReference.get().showDialogLogin(dialogLoginOfflineEntity);
//            }
                    asyncCallLogin.cancel(true);

                    //thread call asyntask is running. must call in other thread to update UI
                    ((LoginActivity) mILoginViewWeakReference.get()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mILoginViewWeakReference.get().hidePbarProcess();

                            if (!asyncCallLogin.ismHasReveicedResponseServer()) {
                                //clear shared Ref
                                mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "").commit();
                                mILoginModel.callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).edit().putString(Define.SHARE_REF_FILE_LOGIN_PASS, "").commit();

                                ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                                objectPublishAsyntask.setMessage("Đăng nhập thất bại!\nRất tiếc không thể kết nối được server trong " + Define.TIME_LIMIT / 1000 + "s");
                                objectPublishAsyntask.setResponse(true);
                                mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);

                            }
                        }
                    });

                } catch (Exception e) {
                    ObjectPublishAsyntask objectPublishAsyntask = new ObjectPublishAsyntask();
                    objectPublishAsyntask.setMessage("Đăng nhập thất bại!\nGặp vấn đề khi đăng nhập " + e.getMessage());
                    objectPublishAsyntask.setResponse(true);
                    mILoginViewWeakReference.get().responseNotifyErrorLogin(objectPublishAsyntask.getMessage(), objectPublishAsyntask.isResponse);
                }
            }
        };

        final ConnectServer.AsyncCallLogin asyncCallLogin = new ConnectServer.AsyncCallLogin(processAsyncLogin, userName, pass, stringIP);
        if (asyncCallLogin.getStatus() != AsyncTask.Status.RUNNING) {
            asyncCallLogin.execute();
        }

        //stop thread
        if (mLoginThread != null && mLoginThread.isAlive()) {
            mLoginThread.interrupt();
        }

        mLoginThread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                ResponseServerLoginJSON result = null;
                try {
                    Thread.sleep(Define.TIME_LIMIT);
                    result = asyncCallLogin.get(Define.TIME_LIMIT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: mLoginThread " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(TAG, "run: mLoginThread " + e.getMessage());
                } catch (TimeoutException e) {
                    Log.e(TAG, "run: mLoginThread " + e.getMessage());
                } finally {
                    if (result == null) {
                        asyncCallLogin.onCountdown(asyncCallLogin);
                    }
                }
            }
        });
        mLoginThread.start();
    }

    @Override
    public void saveSharedPreference() {

    }

    @Override
    public void callConfig() {
        mILoginViewWeakReference.get().openConfigActivity();
    }

    @Override
    public boolean canLoginOffline(final String userName, final String pass) throws Exception {
        //TODO login offline
        Cursor cursorUser = null;
        String passWord = "";
        try {
            cursorUser = mILoginModel.callSqlite().runQueryReturnCursor(Define.queryCheckUser(userName));
            if (cursorUser != null) {
                passWord = cursorUser.getString(cursorUser.getColumnIndex(Define.TABLE_USER_Password));
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi chạy câu lệnh database");
        } finally {
            mILoginModel.callSqlite().close();
        }

        return (passWord.equals(pass)) ? true : false;
    }

    @Override
    public void callShowSharedPrefLogin() {
        String userName = mILoginModel.
                callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                getString(Define.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass = mILoginModel.
                callManagerSharedPref().getSharePref(Define.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                getString(Define.SHARE_REF_FILE_LOGIN_PASS, "");
        mILoginViewWeakReference.get().setInfoResumeLogin(userName, pass);
    }

    @Override
    public void resetValueOnDestroy() {
//        mHasReveicedResponseServer = false;
    }

    @Override
    public Context getContextView() {
        return mILoginViewWeakReference.get().getContextView();
    }
}
