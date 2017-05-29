package esolutions.com.recloser.Presenter.Class;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.recloser.Entity.DeviceEntity;
import esolutions.com.recloser.Entity.DeviceOnOffEntity;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSON;
import esolutions.com.recloser.Entity.HistoryAndAlarmEventJSONToday;
import esolutions.com.recloser.Entity.HistoryDetailDeviceJSON;
import esolutions.com.recloser.Entity.ObjectDetailInfoDevice;
import esolutions.com.recloser.Entity.ObjectEventDevice;
import esolutions.com.recloser.Entity.ObjectPublishAsyntask;
import esolutions.com.recloser.Entity.ObjectSpinnerParamJSONEntity;
import esolutions.com.recloser.Entity.ParamHistoryEntity;
import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.MobileCountDevice;
import esolutions.com.recloser.Entity.ObjectParamaterDeviceJSON;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.Entity.ResponseServerMessageJSON;
import esolutions.com.recloser.Utils.Class.Define;

import static esolutions.com.recloser.Utils.Class.CommonMethod.getJSONDataFromURL;

/**
 * Created by VinhNB on 3/10/2017.
 */

public class ConnectServer {

    public static class AsyncCallLogin extends AsyncTask<Void, ObjectPublishAsyntask, ResponseServerLoginJSON> {
        private ProcessAsyncLogin listener;
        private String mURL;
        private String userName;
        private String passWord;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallLogin(Object object, String userName, String passWord, String ip) {
            if (object instanceof ProcessAsyncLogin) {
                listener = (ProcessAsyncLogin) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be abstract ProcessAsyncLogin");
            }
            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (passWord == null || passWord.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.userName = userName;
            this.passWord = passWord;
            this.mURL = Define.getUrlLogin(ip, this.userName, this.passWord);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteLogin(this);
        }

        @Override
        protected ResponseServerLoginJSON doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();
            Type type = new TypeToken<ResponseServerLoginJSON>() {
            }.getType();

            ResponseServerLoginJSON responseServerLoginJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                responseServerLoginJSON = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return responseServerLoginJSON;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateLogin(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(ResponseServerLoginJSON responseServerLoginJSON) {
            super.onPostExecute(responseServerLoginJSON);
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteLogin(responseServerLoginJSON, this.userName, this.passWord);
        }

        public void onCountdown(final AsyncCallLogin asyncCallLogin) {
            listener.processOnCountdownLogin(asyncCallLogin);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        static abstract class ProcessAsyncLogin {
            public abstract void processOnPreExecuteLogin(final AsyncCallLogin asyncCallLogin);

            public abstract void processOnProgressUpdateLogin(ObjectPublishAsyntask... values);

            public abstract void processOnPostExecuteLogin(ResponseServerLoginJSON responseServerLoginJSON, String userName, String passWord);

            public abstract void processOnCountdownLogin(final AsyncCallLogin asyncCallLogin);
        }
    }

    public static class AsyncCallMobileCountDevice extends AsyncTask<Void, ObjectPublishAsyntask, MobileCountDevice> {
        private String mUser;
        private String mURL;
        private boolean mHasReveicedResponseServer = false;
        private OnProcessAsyncMobileCountDevice listener;

        public AsyncCallMobileCountDevice(Object object, String ip, String user) {
            if (object instanceof OnProcessAsyncMobileCountDevice) {
                listener = (OnProcessAsyncMobileCountDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncMobileCountDevice");
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (user == null || user.isEmpty()) {
                this.cancel(true);
                return;
            }
            this.mUser = user;
            this.mURL = Define.getUrlMobileCountDevice(ip, this.mUser);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteCountDevice(this);
        }

        @Override
        protected MobileCountDevice doInBackground(Void... params) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<MobileCountDevice>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            MobileCountDevice countDevice = null;
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                countDevice = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            return countDevice;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateCountDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(MobileCountDevice countDevice) {
            super.onPostExecute(countDevice);
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteCountDevice(countDevice);
        }

        public void onCountdown(final AsyncCallMobileCountDevice asyncCallMobileCountDevice) {
            listener.processOnCountdownCountDevice(asyncCallMobileCountDevice);
        }

        interface OnProcessAsyncMobileCountDevice {
            void processOnPreExecuteCountDevice(final AsyncCallMobileCountDevice asyncCallMobileCountDevice);

            void processOnProgressUpdateCountDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteCountDevice(MobileCountDevice mobileCountDeviceList);

            void processOnCountdownCountDevice(final AsyncCallMobileCountDevice asyncCallMobileCountDevice);
        }
    }

    public static class AsyncCallUpdateInfo extends AsyncTask<JSONObject, ObjectPublishAsyntask, ResponseServerLoginJSON> {
        private OnProcessAsyncUpdateInfo listener;
        private String mURL;
        private String mUser;
        private boolean mHasReveicedResponseServer;

        public AsyncCallUpdateInfo(Object object, String ip, String user) {
            if (object instanceof OnProcessAsyncUpdateInfo) {
                listener = (OnProcessAsyncUpdateInfo) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }
            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (user == null || user.isEmpty()) {
                this.cancel(true);
                return;
            }
            this.mUser = user;

            this.mURL = Define.getUrlUpdateInfo(ip);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteUpdateInfo(this);
        }

        @Override
        protected ResponseServerLoginJSON doInBackground(JSONObject... objects) {
            Type typeMessage = new TypeToken<ResponseServerLoginJSON>() {
            }.getType();

            ResponseServerLoginJSON responseServerLoginJSON = null;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            String result = "";
            String mJsonData = objects[0].toString();
            try {
                //tạo connection post
                URL url = new URL(mURL);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setDoOutput(true);
                client.setDoInput(true);

                //Đẩy luồng byte utf-8 dữ liệu JSON lên service
                OutputStream os = client.getOutputStream();
                os.write(mJsonData.getBytes("UTF-8"));
                os.flush();
                os.close();

                // đọc object json gửi về
                InputStream in = new BufferedInputStream(client.getInputStream());
                result = IOUtils.toString(in, "UTF-8");

                //close
                in.close();
                client.disconnect();

                if (result == null && result.isEmpty()) {
                    objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }

                //TODO check data reveice and parse json
                try {
                    responseServerLoginJSON = new Gson().fromJson(result, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return responseServerLoginJSON;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateInfo(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(ResponseServerLoginJSON responseServerLoginJSON) {
            super.onPostExecute(responseServerLoginJSON);
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteUpdateInfo(responseServerLoginJSON, this.mUser);
        }

        public void onCountdown(final AsyncCallUpdateInfo asyncCallLogin, long timeLimit) {
            Handler handlerCountdown = new Handler();
            handlerCountdown.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mHasReveicedResponseServer) {
                            mHasReveicedResponseServer = false;
                            return;
                        }
                        listener.processOnCountdownUpdateInfo(asyncCallLogin);
                    } catch (Exception e) {
                        Log.e(Define.TAG, e.getMessage());
                    }
                }
            }, timeLimit);
        }

        interface OnProcessAsyncUpdateInfo {
            void processOnPreExecuteUpdateInfo(final ConnectServer.AsyncCallUpdateInfo asyncCallUpdateInfo);

            void processOnProgressUpdateInfo(ObjectPublishAsyntask... values);

            void processOnPostExecuteUpdateInfo(ResponseServerLoginJSON responseServerLoginJSON, String userName);

            void processOnCountdownUpdateInfo(final AsyncCallUpdateInfo asyncCallUpdateInfo);
        }
    }

    public static class AsyncCallGetInfo extends AsyncTask<Void, ObjectPublishAsyntask, InfoEntity> {
        private OnProcessAsyncGetInfo listener;
        private String mURL;
        private String userName;
        private boolean mHasReveicedResponseServer;

        public AsyncCallGetInfo(Object object, String ip, String userName) {
            if (object instanceof OnProcessAsyncGetInfo) {
                listener = (OnProcessAsyncGetInfo) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }
            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.userName = userName;
            this.mURL = Define.getUrlGetInfo(ip, this.userName);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetInfo(this, userName);
            //coutdown and login offline if not reveiced any response server
//            onCountdown(this, Define.TIME_LIMIT);
        }

        @Override
        protected InfoEntity doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<InfoEntity>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            InfoEntity info = null;
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                info = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return info;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetInfo(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(InfoEntity infoEntity) {
            super.onPostExecute(infoEntity);
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetInfo(infoEntity, this.userName);
        }

        public void onCountdown(final AsyncCallGetInfo asyncCallGetInfo) {
            listener.processOnCountdownGetInfo(asyncCallGetInfo);
//            Handler handlerCountdown = new Handler();
//            handlerCountdown.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if (mHasReveicedResponseServer) {
//                            mHasReveicedResponseServer = false;
//                            return;
//                        }
//                        listener.processOnCountdownGetInfo(asyncCallGetInfo);
//                    } catch (Exception e) {
//                        Log.e(Define.TAG, e.getMessage());
//                    }
//                }
//            }, timeLimit);
        }

        interface OnProcessAsyncGetInfo {
            void processOnPreExecuteGetInfo(final ConnectServer.AsyncCallGetInfo asyncCallGetInfo, String user);

            void processOnProgressUpdateGetInfo(ObjectPublishAsyntask... values);

            void processOnPostExecuteGetInfo(InfoEntity infoEntity, String userName);

            void processOnCountdownGetInfo(final AsyncCallGetInfo asyncCallGetInfo);
        }
    }

    public static class AsyncCallUpdatePass extends AsyncTask<JSONObject, ObjectPublishAsyntask, ResponseServerLoginJSON> {
        private OnProcessAsyncUpdatePass listener;
        private String mURL;
        private String mUser;
        private boolean mHasReveicedResponseServer;

        public AsyncCallUpdatePass(Object object, String ip, String user) {
            if (object instanceof OnProcessAsyncUpdatePass) {
                listener = (OnProcessAsyncUpdatePass) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }
            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (user == null || user.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.mUser = user;
            this.mURL = Define.getUrlUpdatePass(ip);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteUpdatePass(this);
            //coutdown and login offline if not reveiced any response server
//            onCountdown(this, Define.TIME_LIMIT);
        }

        @Override
        protected ResponseServerLoginJSON doInBackground(JSONObject... objects) {

            Type typeMessage = new TypeToken<ResponseServerLoginJSON>() {
            }.getType();

            ResponseServerLoginJSON responseServerLoginJSON = null;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            String result = "";
            String mJsonData = objects[0].toString();
            try {
                //tạo connection post
                URL url = new URL(mURL);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                client.setDoOutput(true);
                client.setDoInput(true);

                //Đẩy luồng byte utf-8 dữ liệu JSON lên service
                OutputStream os = client.getOutputStream();
                os.write(mJsonData.getBytes("UTF-8"));
                os.flush();
                os.close();

                // đọc object json gửi về
                InputStream in = new BufferedInputStream(client.getInputStream());
                result = IOUtils.toString(in, "UTF-8");

                //close
                in.close();
                client.disconnect();

                if (result == null && result.isEmpty()) {
                    objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }

                //TODO check data reveice and parse json
                try {
                    responseServerLoginJSON = new Gson().fromJson(result, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return responseServerLoginJSON;



//            String mDataJSON = "";
//            InputStreamReader reader = null;
//            Gson gson = new Gson();
//            Type type = new TypeToken<ResponseServerLoginJSON>() {
//            }.getType();
//
//            ResponseServerLoginJSON responseServerLoginJSON;
//            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();
//
//            //TODO get data
//            try {
//                mDataJSON = getJSONDataFromURL(mURL);
//            } catch (Exception e) {
//                objectPublish.setMessage("Không kết nối được với máy chủ.");
//                objectPublish.setResponse(false);
//                publishProgress(objectPublish);
//                return null;
//            }
//            if (mDataJSON == null && mDataJSON.isEmpty()) {
//                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
//                objectPublish.setResponse(false);
//                publishProgress(objectPublish);
//                return null;
//            }
//
//            //TODO check data reveice and parse json
//            try {
//                responseServerLoginJSON = new Gson().fromJson(mDataJSON, type);
//            } catch (JsonSyntaxException e) {
//                objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
//                objectPublish.setResponse(true);
//                publishProgress(objectPublish);
//                return null;
//            }
//            return responseServerLoginJSON;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdatePass(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(ResponseServerLoginJSON responseServerLoginJSON) {
            super.onPostExecute(responseServerLoginJSON);
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteUpdatePass(responseServerLoginJSON, this.mUser);
        }

        public void onCountdown(final AsyncCallUpdatePass asyncCallUpdatePass, long timeLimit) {
            Handler handlerCountdown = new Handler();
            handlerCountdown.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mHasReveicedResponseServer) {
                            mHasReveicedResponseServer = false;
                            return;
                        }
                        listener.processOnCountdownUpdatePass(asyncCallUpdatePass);
                    } catch (Exception e) {
                        Log.e(Define.TAG, e.getMessage());
                    }
                }
            }, timeLimit);
        }

        interface OnProcessAsyncUpdatePass {
            void processOnPreExecuteUpdatePass(final ConnectServer.AsyncCallUpdatePass asyncCallUpdatePass);

            void processOnProgressUpdatePass(ObjectPublishAsyntask... values);

            void processOnPostExecuteUpdatePass(ResponseServerLoginJSON responseServerLoginJSON, String userName);

            void processOnCountdownUpdatePass(final AsyncCallUpdatePass asyncCallUpdatePass);
        }
    }

    public static class AsyncCallGetDeviceOnOff extends AsyncTask<Void, ObjectPublishAsyntask, List<DeviceOnOffEntity>> {
        private OnProcessAsyncGetDevice listener;
        private String mURL;
        private String userName;
        private Define.STATE_GET_DEVICE mState;
        private boolean mHasReveicedResponseServer;

        public AsyncCallGetDeviceOnOff(Object object, String ip, String userName, Define.STATE_GET_DEVICE state) {
            if (object instanceof OnProcessAsyncGetDevice) {
                listener = (OnProcessAsyncGetDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (state == null) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.userName = userName;
            this.mState = state;
            this.mURL = Define.getUrlGetConnectOnOffDevice(ip, this.userName, mState);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteDeviceOnOff(mState, this, userName);
            //coutdown and login offline if not reveiced any response server
//            onCountdown(this, Define.TIME_LIMIT);
        }

        @Override
        protected List<DeviceOnOffEntity> doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<List<DeviceOnOffEntity>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<DeviceOnOffEntity> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateDeviceOnOff(mState, values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<DeviceOnOffEntity> deviceOnOffEntity) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteDeviceOnOff(mState, deviceOnOffEntity, this.userName);
            super.onPostExecute(deviceOnOffEntity);
        }

        public void onCountdown(final AsyncCallGetDeviceOnOff asyncCallGetDeviceOnOff) {
            listener.processOnCountdownDeviceOnOff(mState, asyncCallGetDeviceOnOff);
//            Handler handlerCountdown = new Handler();
//            handlerCountdown.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if (mHasReveicedResponseServer) {
//                            mHasReveicedResponseServer = false;
//                        } else {
//                            listener.processOnCountdownDeviceOverUnder(mState, asyncCallGetDeviceOnOff);
//                        }
//
//                    } catch (Exception e) {
//                        Log.e(Define.TAG, e.getMessage());
//                    }
//                }
//            }, timeLimit);
        }

        interface OnProcessAsyncGetDevice {
            void processOnPreExecuteDeviceOnOff(Define.STATE_GET_DEVICE state, final AsyncCallGetDeviceOnOff deviceConnectedEntity, String user);

            void processOnProgressUpdateDeviceOnOff(Define.STATE_GET_DEVICE state, ObjectPublishAsyntask... values);

            void processOnPostExecuteDeviceOnOff(Define.STATE_GET_DEVICE state, List<DeviceOnOffEntity> deviceOnOffEntity, String userName);

            void processOnCountdownDeviceOnOff(Define.STATE_GET_DEVICE state, final AsyncCallGetDeviceOnOff asyncCallGetDeviceOnOff);
        }
    }

    public static class AsyncCallGetAllDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<DeviceEntity>> {
        private ProcessAsyncGetAllDevice listener;
        private String mURL;
        private String userName;
        private boolean mHasReveicedResponseServer;

        public AsyncCallGetAllDevice(Object object, String ip, String userName) {
            if (object instanceof ProcessAsyncGetAllDevice) {
                listener = (ProcessAsyncGetAllDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement ProcessAsyncGetAllDevice");
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.userName = userName;
            this.mURL = Define.getUrlGetAllDevice(ip, this.userName);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetAllDevice(this, userName);
        }

        @Override
        protected List<DeviceEntity> doInBackground(Void... voids) {
            String mDataJSON = "";

            Type type = new TypeToken<List<DeviceEntity>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<DeviceEntity> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetAllDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<DeviceEntity> deviceEntities) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetAllDevice(deviceEntities, this.userName);
            super.onPostExecute(deviceEntities);
        }

        public void onCountdown(final AsyncCallGetAllDevice asyncCallGetAllDevice) {
            listener.processOnCountdownAllDevice(asyncCallGetAllDevice);
        }

        abstract static class ProcessAsyncGetAllDevice {
            abstract void processOnPreExecuteGetAllDevice(final AsyncCallGetAllDevice asyncCallGetAllDevice, String user);

            abstract void processOnProgressUpdateGetAllDevice(ObjectPublishAsyntask... values);

            abstract void processOnPostExecuteGetAllDevice(List<DeviceEntity> deviceEntities, String userName);

            abstract void processOnCountdownAllDevice(final AsyncCallGetAllDevice asyncCallGetAllDevice);
        }
    }

//    public static class AsyncCallGetAllDevice<T, V> extends AsyncTask<T, ObjectPublishAsyntask, Map<String, Object>> {
//        private AsyntaskImp listener;
//        private String mURL;
//        private String userName;
//        private boolean mHasReveicedResponseServer;
//
//        private Class<T> typeDoIn;
//        private Class<V> typePostExcute;
//
//        private TypeToken<V> typeToken;
//
//        public AsyncCallGetAllDevice(Class<T> typeDoIn, Class<V> typePostExcute, Object object, String ip, String userName) {
//
//            if (object instanceof AsyntaskImp) {
//                listener = (AsyntaskImp) object;
//            } else {
//                this.cancel(true);
//                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
//            }
//
//            if (typeDoIn.isAssignableFrom(listener.getTypeDoIn())) {
//                this.typeDoIn = typeDoIn;
//            } else {
//                this.cancel(true);
//                throw new ClassCastException("object " + typeDoIn.getClass().getName() + " doIn must be " + listener.getTypeDoIn().getClass().getName());
//            }
//
//            if (typePostExcute.isAssignableFrom(listener.getTypePostExcute())) {
//                this.typePostExcute = typePostExcute;
//            } else {
//                this.cancel(true);
//                throw new ClassCastException("object " + typePostExcute.getClass().getName() + " typePostExcute must be " + listener.getTypeDoIn().getClass().getName());
//            }
//
//            if (ip == null || ip.isEmpty()) {
//                this.cancel(true);
//                return;
//            }
//
//            if (userName == null || userName.isEmpty()) {
//                this.cancel(true);
//                return;
//            }
//
//            this.userName = userName;
//            this.mURL = Define.getUrlGetAllDevice(ip, this.userName);
//        }
//
//        public Class<T> getTypeDoIn() {
//            return typeDoIn;
//        }
//
//        public Class<V> getTypePostExcute() {
//            return typePostExcute;
//        }
//
//        public boolean ismHasReveicedResponseServer() {
//            return mHasReveicedResponseServer;
//        }
//
//        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
//            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            listener.processOnPreExecute();
//        }
//
//        @Override
//        protected Map<String, Object> doInBackground(T... ts) {
//            String mDataJSON = "";
//            InputStreamReader reader = null;
//
//            Type type = new TypeToken<ArrayList<V>>(){}.getType();
//            Type typeMessage = new TypeToken<ResponseServerMessageJSON>(){}.getType();
//            Type typeTest = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
//
//            List<V> list = new ArrayList<>();
//            Map<String, Object> objectMap= new HashMap<>();
//            ResponseServerMessageJSON responseServerMessageJSON;
//            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();
//
//            //TODO get data
//            try {
//                mDataJSON = getJSONDataFromURL(mURL);
//            } catch (Exception e) {
//                objectPublish.setMessage("Không kết nối được với máy chủ.");
//                objectPublish.setResponse(false);
//                publishProgress(objectPublish);
//                return null;
//            }
//
//            boolean isObjectParamaterDevice = true;
//
//            if (mDataJSON == null && mDataJSON.isEmpty()) {
//                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
//                objectPublish.setResponse(false);
//                return null;
//            }
//
//            //TODO check data reveice and parse json
//            try {
////                list = new Gson().fromJson(mDataJSON, type);
//                list = new Gson().fromJson(mDataJSON, typeTest);
//            } catch (JsonSyntaxException e) {
//                isObjectParamaterDevice = false;
//            }
//
//            /*for(int i = 0; i<listLinkedTreeMap.size(); i++)
//            {
//                DeviceEntity object = new Gson().fromJson(new Gson().toJson(((LinkedTreeMap<String, Object>) listLinkedTreeMap.get(i))), MyClass .class)
//            }*/
//
//            if (!isObjectParamaterDevice) {
//                try {
//                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
//                } catch (JsonSyntaxException e) {
//                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
//                    objectPublish.setResponse(false);
//                    publishProgress(objectPublish);
//                    return null;
//                }
//                objectPublish.setMessage(responseServerMessageJSON.getMessage());
//                objectPublish.setResponse(true);
//                publishProgress(objectPublish);
//                return null;
//            }else {
//            }
//            return objectMap;
//        }
//
//        @Override
//        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
//            super.onProgressUpdate(values);
//            ObjectPublishAsyntask objectPublishAsyntask = values[0];
//            mHasReveicedResponseServer = objectPublishAsyntask.isResponse();
//            listener.processOnProgressUpdate(values);
//            this.cancel(true);
//        }
//
//        @Override
//        protected void onPostExecute(Map<String, Object> result) {
//            mHasReveicedResponseServer = true;
//            listener.processOnPostExecute(result);
//            super.onPostExecute(result);
//        }
//
//        public void onCountdown(final AsyncCallGetAllDevice<T, V> asyncCallGetAllDeviceTest) {
//            listener.processOnCountdown(asyncCallGetAllDeviceTest);
//        }
//        public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
//            Object [] array = (Object[])java.lang.reflect.Array.newInstance(clazz, 1);
//            array = new Gson().fromJson(json, array.getClass());
//            List<T> list = new ArrayList<T>();
//            for (int i=0 ; i<array.length ; i++)
//                list.add((T)array[i]);
//            return list;
//        }
//        public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
//            T[] arr = new Gson().fromJson(s, clazz);
//            return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
//        }
//
//    }

    public static class AsyncCallGetInfoDetailDevice extends AsyncTask<Void, ObjectPublishAsyntask, ObjectDetailInfoDevice> {
        private OnProcessAsyncGetInfoDetailDevice listener;
        private String mURL;
        private String userName;
        private int deviceID;
        private boolean mHasReveicedResponseServer;

        public AsyncCallGetInfoDetailDevice(Object object, String ip, String userName, int deviceID) {
            if (object instanceof OnProcessAsyncGetInfoDetailDevice) {
                listener = (OnProcessAsyncGetInfoDetailDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.userName = userName;
            this.mURL = Define.getUrlGetInfoDetailDevice(ip, this.deviceID);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetInfoDetailDevice(this, userName);
        }

        @Override
        protected ObjectDetailInfoDevice doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<ObjectDetailInfoDevice>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            ObjectDetailInfoDevice objectDetailInfoDeviceList = null;
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                objectDetailInfoDeviceList = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }
            return objectDetailInfoDeviceList;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetInfoDetailDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(ObjectDetailInfoDevice result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteInfoDetailDevice(result, this.userName);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice) {
            listener.processOnCountdownInfoDetailDevice(asyncCallGetInfoDetailDevice);
        }

        interface OnProcessAsyncGetInfoDetailDevice {
            void processOnPreExecuteGetInfoDetailDevice(final AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice, String user);

            void processOnProgressUpdateGetInfoDetailDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteInfoDetailDevice(ObjectDetailInfoDevice objectDetailInfoDevice, String userName);

            void processOnCountdownInfoDetailDevice(final AsyncCallGetInfoDetailDevice asyncCallGetInfoDetailDevice);
        }
    }

    public static class AsyncCallGetChartParamHistoryToDayDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<ParamHistoryEntity>> {
        private OnProcessAsyncGetChartParamHistoryDevice listener;
        private String mURL;
        private String userName;
        private int deviceID;
        private String param;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallGetChartParamHistoryToDayDevice(Object object, String ip, String userName, int deviceID, String param) {
            if (object instanceof OnProcessAsyncGetChartParamHistoryDevice) {
                listener = (OnProcessAsyncGetChartParamHistoryDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (param == null || param.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.userName = userName;
            this.param = param;
            this.mURL = Define.getUrlGetParamHistoryTodayDevice(ip, this.userName, this.deviceID, this.param);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetLineChartParamHistoryDevice();
        }

        @Override
        protected List<ParamHistoryEntity> doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<List<ParamHistoryEntity>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<ParamHistoryEntity> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetLineChartParamHistoryDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<ParamHistoryEntity> paramHistoryEntity) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteLineChartParamDeviceToday(paramHistoryEntity, param);
            super.onPostExecute(paramHistoryEntity);
        }

        public void onCountdown(final AsyncCallGetChartParamHistoryToDayDevice asyncCallGetChartParamHistoryToDayDevice) {
            listener.processOnCountdownLineChartParamHistoryDevice(asyncCallGetChartParamHistoryToDayDevice);
        }

        interface OnProcessAsyncGetChartParamHistoryDevice {
            void processOnPreExecuteGetLineChartParamHistoryDevice();

            void processOnProgressUpdateGetLineChartParamHistoryDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteLineChartParamDeviceToday(List<ParamHistoryEntity> paramHistoryEntity, String param);

            void processOnCountdownLineChartParamHistoryDevice(final AsyncCallGetChartParamHistoryToDayDevice asyncCallGetChartParamHistoryToDayDevice);
        }
    }

    public static class AsyncCallGetChartParamHistoryDeviceTrends extends AsyncTask<Void, ObjectPublishAsyntask, List<ParamHistoryEntity>> {
        private ProcessAsyncGetChartParamHistoryDevice listener;
        private String mURL;
        private int deviceID;
        private String nameDevice;
        private String startDate;
        private String endDate;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallGetChartParamHistoryDeviceTrends(Object object, String ip, int deviceID, String nameDevice, String startDate, String endDate) {
            if (object instanceof ProcessAsyncGetChartParamHistoryDevice) {
                listener = (ProcessAsyncGetChartParamHistoryDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (nameDevice == null || nameDevice.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (startDate == null || startDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (endDate == null || endDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.nameDevice = nameDevice;
            this.deviceID = deviceID;
            this.startDate = startDate;
            this.endDate = endDate;
            this.mURL = Define.getUrlGetParamHistoryDeviceTrends(ip, this.deviceID, this.startDate, this.endDate);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetLineChartParamHistoryDevice(this);
        }

        @Override
        protected List<ParamHistoryEntity> doInBackground(Void... voids) {
            String mDataJSON = "";

            Type type = new TypeToken<List<ParamHistoryEntity>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<ParamHistoryEntity> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetLineChartParamHistoryDevice(objectPublishAsyntask);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<ParamHistoryEntity> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteLineChartParamHistoryDeviceTrends(result, deviceID, nameDevice, startDate, endDate);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDeviceTrends) {
            listener.processOnCountdownLineChartParamHistoryDevice(asyncCallGetChartParamHistoryDeviceTrends);
        }

        static abstract class ProcessAsyncGetChartParamHistoryDevice {
            abstract void processOnPreExecuteGetLineChartParamHistoryDevice(final AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDeviceTrends);

            abstract void processOnProgressUpdateGetLineChartParamHistoryDevice(ObjectPublishAsyntask... values);

            abstract void processOnPostExecuteLineChartParamHistoryDeviceTrends(List<ParamHistoryEntity> paramHistoryEntityList, int idDevice, String nameDevice, String dateBegin, String dateEnd);

            abstract void processOnCountdownLineChartParamHistoryDevice(final AsyncCallGetChartParamHistoryDeviceTrends asyncCallGetChartParamHistoryDeviceTrends);
        }
    }

    public static class AsyncCallGetSpinnerParamDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<ObjectSpinnerParamJSONEntity>> {
        private OnProcessAsyncGetSpinnerParamDevice listener;
        private String mURL;
        private String userName;
        private int deviceID;
        private boolean mHasReveicedResponseServer = false;

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        public AsyncCallGetSpinnerParamDevice(Object object, String ip, String userName, int deviceID) {
            if (object instanceof OnProcessAsyncGetSpinnerParamDevice) {
                listener = (OnProcessAsyncGetSpinnerParamDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.userName = userName;
            this.mURL = Define.getUrlGetSpinnerParamDevice(ip, this.userName, this.deviceID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetSpinnerParamDevice();
            //coutdown and login offline if not reveiced any response server
//            onCountdown(this, Define.TIME_LIMIT);
        }

        @Override
        protected List<ObjectSpinnerParamJSONEntity> doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<List<ObjectSpinnerParamJSONEntity>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<ObjectSpinnerParamJSONEntity> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetSpinnerParamDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<ObjectSpinnerParamJSONEntity> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetSpinnerParamDevice(result);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetSpinnerParamDevice asyncCallGetSpinnerParamDevice) {
            listener.processOnCountdownSpinnerParamDevice(asyncCallGetSpinnerParamDevice);
//            Handler handlerCountdown = new Handler();
//            handlerCountdown.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if (mHasReveicedResponseServer) {
//                            mHasReveicedResponseServer = false;
//                        } else {
//                            listener.processOnCountdownDeviceOverUnder(mState, asyncCallGetDeviceOnOff);
//                        }
//
//                    } catch (Exception e) {
//                        Log.e(Define.TAG, e.getMessage());
//                    }
//                }
//            }, timeLimit);
        }

        interface OnProcessAsyncGetSpinnerParamDevice {
            void processOnPreExecuteGetSpinnerParamDevice();

            void processOnProgressUpdateGetSpinnerParamDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteGetSpinnerParamDevice(List<ObjectSpinnerParamJSONEntity> entityList);

            void processOnCountdownSpinnerParamDevice(final AsyncCallGetSpinnerParamDevice asyncCallGetSpinnerParamDevice);
        }
    }

    public static class AsyncCallGetRecyclerParamDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<ObjectParamaterDeviceJSON>> {
        private OnProcessAsyncGetRecyclerParamDevice listener;
        private String mURL;
        private String userName;
        private int deviceID;
        private boolean mHasReveicedResponseServer = false;

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        public AsyncCallGetRecyclerParamDevice(Object object, String ip, String userName, int deviceID) {
            if (object instanceof OnProcessAsyncGetRecyclerParamDevice) {
                listener = (OnProcessAsyncGetRecyclerParamDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.userName = userName;
            this.mURL = Define.getUrlGetRecyclerParamDevice(ip, this.deviceID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetRecyclerParamDevice();
            //coutdown and login offline if not reveiced any response server
//            onCountdown(this, Define.TIME_LIMIT);
        }

        @Override
        protected List<ObjectParamaterDeviceJSON> doInBackground(Void... voids) {

            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<List<ObjectParamaterDeviceJSON>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<ObjectParamaterDeviceJSON> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = false;
            listener.processOnProgressUpdateGetRecyclerParamDevice(values);
            this.cancel(true);
        }


        @Override
        protected void onPostExecute(List<ObjectParamaterDeviceJSON> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteRecyclerParamDevice(result);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetRecyclerParamDevice asyncCallGetRecyclerParamDevice) {
            listener.processOnCountdownRecyclerParamDevice(asyncCallGetRecyclerParamDevice);
//            Handler handlerCountdown = new Handler();
//            handlerCountdown.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if (mHasReveicedResponseServer) {
//                            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
//                        } else {
//                            listener.processOnCountdownDeviceOverUnder(mState, asyncCallGetDeviceOnOff);
//                        }
//
//                    } catch (Exception e) {
//                        Log.e(Define.TAG, e.getMessage());
//                    }
//                }
//            }, timeLimit);
        }

        interface OnProcessAsyncGetRecyclerParamDevice {
            void processOnPreExecuteGetRecyclerParamDevice();

            void processOnProgressUpdateGetRecyclerParamDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteRecyclerParamDevice(List<ObjectParamaterDeviceJSON> entityList);

            void processOnCountdownRecyclerParamDevice(final AsyncCallGetRecyclerParamDevice asyncCallGetRecyclerParamDevice);
        }

        public static JSONObject convertJSONInfoUpdate(String user, String fullName, String phone, String email) throws Exception {
            if (user == null || user.isEmpty())
                throw new Exception("convertJSONInfoUpdate với user null!");
            if (fullName == null || fullName.isEmpty())
                throw new Exception("convertJSONInfoUpdate với fullName null!");
            if (phone == null || phone.isEmpty())
                throw new Exception("convertJSONInfoUpdate với phone null!");
            if (email == null || email.isEmpty())
                throw new Exception("convertJSONInfoUpdate với email null!");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("username", user);
                jsonObject.accumulate("fullName", fullName);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("phone", phone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        public static JSONObject convertJSONPassUpdate(String user, String currentPass, String newPass, String retypeNewPass) throws Exception {
            if (user == null || user.isEmpty())
                throw new Exception("convertJSONPassUpdate với user null!");
            if (currentPass == null || currentPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với currentPass null!");
            if (newPass == null || newPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với newPass null!");
            if (retypeNewPass == null || retypeNewPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với retypeNewPass null!");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("username", user);
                jsonObject.accumulate("OldPass", currentPass);
                jsonObject.accumulate("NewPass", newPass);
                jsonObject.accumulate("ConfigPass", retypeNewPass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }


    }

    public static class AsyncCallGetRecyclerEventDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<ObjectEventDevice>> {
        private OnProcessAsyncGetRecyclerEventDevice listener;
        private String mURL;
        private String userName;
        private int deviceID;
        private boolean mHasReveicedResponseServer = false;

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        public AsyncCallGetRecyclerEventDevice(Object object, String ip, String userName, int deviceID) {
            if (object instanceof OnProcessAsyncGetRecyclerEventDevice) {
                listener = (OnProcessAsyncGetRecyclerEventDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement OnProcessAsyncGetDisconnectedDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (userName == null || userName.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.userName = userName;
            this.mURL = Define.getUrlGetRecyclerEventDevice(ip, this.userName, this.deviceID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteRecyclerEventDevice();
        }

        @Override
        protected List<ObjectEventDevice> doInBackground(Void... voids) {
            String mDataJSON = "";
            InputStreamReader reader = null;
            Gson gson = new Gson();

            Type type = new TypeToken<List<ObjectEventDevice>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<ObjectEventDevice> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateRecyclerEventDevice(values);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<ObjectEventDevice> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteRecyclerEventDevice(result);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetRecyclerEventDevice asyncCallGetRecyclerEventDevice) {
            listener.processOnCountdownRecyclerEventDevice(asyncCallGetRecyclerEventDevice);
        }

        interface OnProcessAsyncGetRecyclerEventDevice {
            void processOnPreExecuteRecyclerEventDevice();

            void processOnProgressUpdateRecyclerEventDevice(ObjectPublishAsyntask... values);

            void processOnPostExecuteRecyclerEventDevice(List<ObjectEventDevice> entityList);

            void processOnCountdownRecyclerEventDevice(final AsyncCallGetRecyclerEventDevice asyncCallGetRecyclerEventDevice);
        }

        public static JSONObject convertJSONInfoUpdate(String user, String fullName, String phone, String email) throws Exception {
            if (user != null || user.isEmpty())
                throw new Exception("convertJSONInfoUpdate với user null!");
            if (fullName != null || fullName.isEmpty())
                throw new Exception("convertJSONInfoUpdate với fullName null!");
            if (phone != null || phone.isEmpty())
                throw new Exception("convertJSONInfoUpdate với phone null!");
            if (email != null || email.isEmpty())
                throw new Exception("convertJSONInfoUpdate với email null!");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("username", user);
                jsonObject.accumulate("fullName", fullName);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("phone", phone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        public static JSONObject convertJSONPassUpdate(String user, String currentPass, String newPass, String retypeNewPass) throws Exception {
            if (user == null || user.isEmpty())
                throw new Exception("convertJSONPassUpdate với user null!");
            if (currentPass == null || currentPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với currentPass null!");
            if (newPass == null || newPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với newPass null!");
            if (retypeNewPass == null || retypeNewPass.isEmpty())
                throw new Exception("convertJSONPassUpdate với retypeNewPass null!");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("username", user);
                jsonObject.accumulate("OldPass", currentPass);
                jsonObject.accumulate("NewPass", newPass);
                jsonObject.accumulate("ConfigPass", retypeNewPass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

    }

    public static class AsyncCallGetHistoryAndAlarmEvent extends AsyncTask<Void, ObjectPublishAsyntask, List<HistoryAndAlarmEventJSON>> {
        private ProcessAsyncGetHistoryAlarm listener;
        private String mURL;
        private int deviceID;
        private String nameDevice;
        private String startDate;
        private String endDate;
        private String typeString;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallGetHistoryAndAlarmEvent(Object object, String ip, int deviceID, String nameDevice, String typeString, String startDate, String endDate) {
            if (object instanceof ProcessAsyncGetHistoryAlarm) {
                listener = (ProcessAsyncGetHistoryAlarm) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement ProcessAsyncCallGetHistoryAndAlarmEventToday");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (nameDevice == null || nameDevice.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (typeString == null || typeString.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (startDate == null || startDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (endDate == null || endDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.nameDevice = nameDevice;
            this.deviceID = deviceID;
            this.startDate = startDate;
            this.endDate = endDate;
            this.typeString = typeString;
            this.mURL = Define.getUrlGetHistoryAndAlarm(ip, this.deviceID, this.typeString, this.startDate, this.endDate);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetHistoryAlarm(this, typeString);
        }

        @Override
        protected List<HistoryAndAlarmEventJSON> doInBackground(Void... voids) {
            String mDataJSON = "";

            Type type = new TypeToken<List<HistoryAndAlarmEventJSON>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<HistoryAndAlarmEventJSON> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetHistoryAlarm(typeString, objectPublishAsyntask);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<HistoryAndAlarmEventJSON> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetHistoryAlarm(result, deviceID, nameDevice, typeString, startDate, endDate);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent) {
            listener.processOnCountdownGetHistoryAlarm(asyncCallGetHistoryAndAlarmEvent, typeString);
        }

        static abstract class ProcessAsyncGetHistoryAlarm {
            abstract void processOnPreExecuteGetHistoryAlarm(final AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent, String typeString);

            abstract void processOnProgressUpdateGetHistoryAlarm(String typeString, ObjectPublishAsyntask... values);

            abstract void processOnPostExecuteGetHistoryAlarm(List<HistoryAndAlarmEventJSON> historyAndAlarmEventJSONs, int idDevice, String nameDevice, String typeString, String dateBegin, String dateEnd);

            abstract void processOnCountdownGetHistoryAlarm(final AsyncCallGetHistoryAndAlarmEvent asyncCallGetHistoryAndAlarmEvent, String typeString);
        }
    }

    public static class AsyncCallGetHistoryAndAlarmEventToday extends AsyncTask<Void, ObjectPublishAsyntask, List<HistoryAndAlarmEventJSONToday>> {
        private ProcessAsyncCallGetHistoryAndAlarmEventToday listener;
        private String mURL;
        private String user;
        private Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallGetHistoryAndAlarmEventToday(Object object, String user, String ip, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString) {
            if (object instanceof ProcessAsyncCallGetHistoryAndAlarmEventToday) {
                listener = (ProcessAsyncCallGetHistoryAndAlarmEventToday) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement ProcessAsyncCallGetHistoryAndAlarmEventToday");
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (user == null || user.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (typeString == null) {
                this.cancel(true);
                return;
            }
            this.user = user;
            this.typeString = typeString;
            this.mURL = Define.getUrlGetHistoryAndAlarmToday(ip, this.user, this.typeString);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetHistoryAlarm(this, typeString);
        }

        @Override
        protected List<HistoryAndAlarmEventJSONToday> doInBackground(Void... voids) {
            String mDataJSON = "";

            Type type = new TypeToken<List<HistoryAndAlarmEventJSONToday>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<HistoryAndAlarmEventJSONToday> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetHistoryAlarm(typeString, objectPublishAsyntask);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<HistoryAndAlarmEventJSONToday> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetHistoryAlarm(result, typeString);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday) {
            listener.processOnCountdownGetHistoryAlarm(asyncCallGetHistoryAndAlarmEventToday, typeString);
        }

        static abstract class ProcessAsyncCallGetHistoryAndAlarmEventToday {
            abstract void processOnPreExecuteGetHistoryAlarm(final AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);

            abstract void processOnProgressUpdateGetHistoryAlarm(Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString, ObjectPublishAsyntask... values);

            abstract void processOnPostExecuteGetHistoryAlarm(List<HistoryAndAlarmEventJSONToday> historyAndAlarmEventJSONTodays, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);

            abstract void processOnCountdownGetHistoryAlarm(final AsyncCallGetHistoryAndAlarmEventToday asyncCallGetHistoryAndAlarmEventToday, Define.STATE_GET_HISTORY_ALARM_EVENT_TODAY typeString);
        }
    }

    public static class AsyncCallGetHistoryDetailDevice extends AsyncTask<Void, ObjectPublishAsyntask, List<HistoryDetailDeviceJSON>> {
        private ProcessAsyncCallGetHistoryDetailDevice listener;
        private String mURL;
        private int deviceID;
        private String startDate;
        private String endDate;
        private boolean mHasReveicedResponseServer = false;

        public AsyncCallGetHistoryDetailDevice(Object object, String ip, int deviceID, String startDate, String endDate) {
            if (object instanceof ProcessAsyncCallGetHistoryDetailDevice) {
                listener = (ProcessAsyncCallGetHistoryDetailDevice) object;
            } else {
                this.cancel(true);
                throw new ClassCastException("class must be implement ProcessAsyncCallGetHistoryDetailDevice");
            }

            if (deviceID <= 0) {
                this.cancel(true);
                return;
            }

            if (ip == null || ip.isEmpty()) {
                this.cancel(true);
                return;
            }
            if (startDate == null || startDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            if (endDate == null || endDate.isEmpty()) {
                this.cancel(true);
                return;
            }

            this.deviceID = deviceID;
            this.startDate = startDate;
            this.endDate = endDate;
            this.mURL = Define.getUrlGetHistoryDetailDevice(ip, this.deviceID, this.startDate, this.endDate);
        }

        public boolean ismHasReveicedResponseServer() {
            return mHasReveicedResponseServer;
        }

        public void setmHasReveicedResponseServer(boolean mHasReveicedResponseServer) {
            this.mHasReveicedResponseServer = mHasReveicedResponseServer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.processOnPreExecuteGetHistoryDetailDevice(this);
        }

        @Override
        protected List<HistoryDetailDeviceJSON> doInBackground(Void... voids) {
            String mDataJSON = "";

            Type type = new TypeToken<List<HistoryDetailDeviceJSON>>() {
            }.getType();
            Type typeMessage = new TypeToken<ResponseServerMessageJSON>() {
            }.getType();

            List<HistoryDetailDeviceJSON> list = new ArrayList<>();
            ResponseServerMessageJSON responseServerMessageJSON;
            ObjectPublishAsyntask objectPublish = new ObjectPublishAsyntask();

            //TODO get data
            try {
                mDataJSON = getJSONDataFromURL(mURL);
            } catch (Exception e) {
                objectPublish.setMessage("Không kết nối được với máy chủ.");
                objectPublish.setResponse(false);
                publishProgress(objectPublish);
                return null;
            }

            boolean isObjectParamaterDevice = true;

            if (mDataJSON == null && mDataJSON.isEmpty()) {
                objectPublish.setMessage("Không nhận được dữ liệu từ máy chủ.");
                objectPublish.setResponse(false);
                return null;
            }

            //TODO check data reveice and parse json
            try {
                list = new Gson().fromJson(mDataJSON, type);
            } catch (JsonSyntaxException e) {
                isObjectParamaterDevice = false;
            }

            if (!isObjectParamaterDevice) {
                try {
                    responseServerMessageJSON = new Gson().fromJson(mDataJSON, typeMessage);
                } catch (JsonSyntaxException e) {
                    objectPublish.setMessage("Gặp vấn đề khi lấy dữ liệu.\nNội dung: " + e.getMessage());
                    objectPublish.setResponse(false);
                    publishProgress(objectPublish);
                    return null;
                }
                objectPublish.setMessage(responseServerMessageJSON.getMessage());
                objectPublish.setResponse(true);
                publishProgress(objectPublish);
                return null;
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(ObjectPublishAsyntask... values) {
            super.onProgressUpdate(values);
            ObjectPublishAsyntask objectPublishAsyntask = values[0];
            mHasReveicedResponseServer = objectPublishAsyntask.isResponse;
            listener.processOnProgressUpdateGetHistoryDetailDevice(objectPublishAsyntask);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(List<HistoryDetailDeviceJSON> result) {
            mHasReveicedResponseServer = true;
            listener.processOnPostExecuteGetHistoryDetailDevice(result, deviceID, startDate, endDate);
            super.onPostExecute(result);
        }

        public void onCountdown(final AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice) {
            listener.processOnCountdownGetHistoryDetailDevice(asyncCallGetHistoryDetailDevice);
        }

        static abstract class ProcessAsyncCallGetHistoryDetailDevice {
            abstract void processOnPreExecuteGetHistoryDetailDevice(final AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice);

            abstract void processOnProgressUpdateGetHistoryDetailDevice(ObjectPublishAsyntask... values);

            abstract void processOnPostExecuteGetHistoryDetailDevice(List<HistoryDetailDeviceJSON> historyDetailDeviceJSONs, int idDevice, String dateBegin, String dateEnd);

            abstract void processOnCountdownGetHistoryDetailDevice(final AsyncCallGetHistoryDetailDevice asyncCallGetHistoryDetailDevice);
        }
    }

}
