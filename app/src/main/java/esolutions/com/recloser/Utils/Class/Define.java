package esolutions.com.recloser.Utils.Class;

import android.graphics.Color;

/**
 * Created by VinhNB on 2/15/2017.
 */

public final class Define {

    //region config variable
    public static String TAG = "TAG";
    public static int TIME_LIMIT = 5000;
    public static int TIME_LIMIT_SHOW_DIALOG = 3000;
    public static int TIME_LIMIT_TOAST = 2000;
    public static final String[] HEADER_NAVI_MENU = {"Dashboard", "All device", "Report"};

    public static final String[] TYPE_SPINNER_HISTORY = {"Event", "Alarm"};
    public static int NUMBER_COLLUMN_DASHBOARD = 2;
    public static final String ERROR = "Không có dữ liệu";
    //endregion

    //region url
    public static final String URL_PREFIX = "http://";
    public static final String URL_SUFFIX = "?";
    public static final String URL_MIDDLE = "&";
    public static final String SYMBOL_PARAM_URL = ",";
    public static final String SYMBOL_PARAM_SPACE = " ";

    public enum URL_METHOD {
        URL_CHECK_LOGIN,
        URL_MOBILE_COUNT_DEVICE,
        URL_GET_INFO,
        URL_UPDATE_INFO,
        URL_UPDATE_PASS,
        URL_GET_DEVICE,
        URL_GET_ALL_DEVICE,
        URL_GET_INFO_DETAIL_DEVICE,
        URL_GET_PARAM_HISTORY_TODAY,
        URL_GET_SPINNER_PARAM,
        URL_GET_RECYCLER_PARAM,
        URL_GET_RECYCLER_EVENT,
        URL_GET_ALL_PARAM_HISTORY_DEVICE_TRENDS,
        URL_GET_HISTORY_ALARM,
        URL_GET_HISTORY_ALARM_TODAY,
        URL_GET_HISTORY_DETAIL_DEVICE;

        @Override
        public String toString() {
            if (this == URL_CHECK_LOGIN)
                return "/Api/Login/MobileLogin";

            if (this == URL_MOBILE_COUNT_DEVICE)
                return "/Api/Devices/MobileCountDeviceByState";

            if (this == URL_GET_INFO)
                return "/Api/User/MobileGetUserProfile";

            if (this == URL_UPDATE_INFO)
                return "/Api/User/MobileSetUserProfile";

            if (this == URL_UPDATE_PASS)
                return "/Api/User/MobileSetPasswordUser";

            if (this == URL_GET_DEVICE)
                return "/Api/Devices/MobileGetDeviceByState";

            if (this == URL_GET_ALL_DEVICE)
                return "/Api/Devices/MobileGetAllDeviceByUser";

            if (this == URL_GET_INFO_DETAIL_DEVICE)
                return "/Api/Devices/MobileGetInfoDevice";

            if (this == URL_GET_PARAM_HISTORY_TODAY)
                return "/Api/Devices/MobileGetParametersByDescription";

            if (this == URL_GET_RECYCLER_PARAM)
                return "/Api/Devices/MobileGetParametersDetail";

            if (this == URL_GET_RECYCLER_EVENT)
                return "/Api/Devices/MobileGetEventDetailDevice";

            if (this == URL_GET_SPINNER_PARAM)
                return "/Api/Devices/MobileGetDescription";

            if (this == URL_GET_ALL_PARAM_HISTORY_DEVICE_TRENDS)
                return "/Api/Devices/MobileGetParametersByHistoricalData";

            if (this == URL_GET_HISTORY_ALARM)
                return "/Api/Devices/MobileGetParametersByHistoricalEvent";

            if (this == URL_GET_HISTORY_ALARM_TODAY)
                return "/Api/Devices/MobileGetValueDeviceByType";

            if (this == URL_GET_HISTORY_DETAIL_DEVICE)
                return "/Api/Devices/MobileGetDeviceHistory";

            return "";
        }
    }

    public static String getUrlLogin(String ip, String userName, String pass) {
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        if (pass == null || pass.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_CHECK_LOGIN.toString() + Define.URL_SUFFIX
                + "username="
                + userName
                + URL_MIDDLE
                + "password="
                + pass;
    }

    public static String getUrlMobileCountDevice(String ip, String userName) {
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_MOBILE_COUNT_DEVICE.toString() + Define.URL_SUFFIX
                + "user="
                + userName;
    }

    public static String getUrlGetInfo(String ip, String userName) {
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_INFO.toString() + Define.URL_SUFFIX
                + "username="
                + userName;
    }

    public static String getUrlUpdateInfo(String ip) {
        if (ip == null || ip.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_UPDATE_INFO.toString();
    }

    public static String getUrlUpdatePass(String ip) {
        if (ip == null || ip.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_UPDATE_PASS.toString();
    }

    public static String getUrlGetConnectOnOffDevice(String ip, String userName, Define.STATE_GET_DEVICE stateGetDevice) {
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        if (stateGetDevice == null)
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_DEVICE.toString() + Define.URL_SUFFIX
                + "user="
                + userName
                + URL_MIDDLE
                + "state="
                + stateGetDevice.value();
    }

    public static String getUrlGetAllDevice(String ip, String userName) {

        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_ALL_DEVICE.toString() + Define.URL_SUFFIX
                + "username="
                + userName;
    }

    public static String getUrlGetInfoDetailDevice(String ip, int deviceID) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_INFO_DETAIL_DEVICE.toString() + Define.URL_SUFFIX
                + "id="
                + deviceID;
    }

    public static String getUrlGetParamHistoryTodayDevice(String ip, String userName, int deviceID, String param) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        if (param == null || param.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_PARAM_HISTORY_TODAY.toString() + Define.URL_SUFFIX
                + "id="
                + deviceID
                + URL_MIDDLE
                + "description="
                + param;
    }

    public static String getUrlGetSpinnerParamDevice(String ip, String userName, int deviceID) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_SPINNER_PARAM.toString() + Define.URL_SUFFIX
                + "username="
                + userName
                + URL_MIDDLE
                + "deviceID="
                + deviceID;
    }

    public static String getUrlGetRecyclerParamDevice(String ip, int deviceID) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_RECYCLER_PARAM.toString() + Define.URL_SUFFIX
                + "id="
                + deviceID;
    }

    public static String getUrlGetRecyclerEventDevice(String ip, String userName, int deviceID) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (userName == null || userName.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_RECYCLER_EVENT.toString() + Define.URL_SUFFIX
                + "device="
                + deviceID;
    }

    public static String getUrlGetParamHistoryDeviceTrends(String ip, int deviceID, String startDate, String endDate) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (startDate == null || startDate.isEmpty())
            return null;
        if (endDate == null || endDate.isEmpty())
            return null;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_ALL_PARAM_HISTORY_DEVICE_TRENDS.toString()
                + Define.URL_SUFFIX
                + "device="
                + deviceID
                + Define.URL_MIDDLE
                + "StartDate="
                + startDate
                + Define.URL_MIDDLE
                + "EndDate="
                + endDate;
    }

    public static String getUrlGetHistoryAndAlarm(String ip, int deviceID, String typeString, String startDate, String endDate) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (typeString == null || typeString.isEmpty())
            return null;
        if (startDate == null || startDate.isEmpty())
            return null;
        if (endDate == null || endDate.isEmpty())
            return null;
        int indexType = 0;
        for (; indexType < Define.TYPE_SPINNER_HISTORY.length; indexType++) {
            if (typeString.equals(Define.TYPE_SPINNER_HISTORY[indexType])) {
                break;
            }
        }

        //type string = 2 là param, = 1 là event
        indexType += 1;
        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_HISTORY_ALARM.toString()
                + Define.URL_SUFFIX
                + "device="
                + deviceID
                + Define.URL_MIDDLE
                + "StartDate="
                + startDate
                + Define.URL_MIDDLE
                + "EndDate="
                + endDate
                + Define.URL_MIDDLE
                + "Type="
                + indexType;
    }

    public static String getUrlGetHistoryAndAlarmToday(String ip, String user, STATE_GET_HISTORY_ALARM_EVENT_TODAY type) {
        if (ip == null || ip.isEmpty())
            return null;
        if (user == null || user.isEmpty())
            return null;
        if (type == null)
            return null;

        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_HISTORY_ALARM_TODAY.toString()
                + Define.URL_SUFFIX
                + "user="
                + user
                + Define.URL_MIDDLE
                + "type="
                + type.getValue();
    }

    public static String getUrlGetHistoryDetailDevice(String ip, int deviceID, String startDate, String endDate) {
        if (deviceID <= 0)
            return null;
        if (ip == null || ip.isEmpty())
            return null;
        if (startDate == null || startDate.isEmpty())
            return null;
        if (endDate == null || endDate.isEmpty())
            return null;

        return Define.URL_PREFIX + ip + URL_METHOD.URL_GET_HISTORY_DETAIL_DEVICE.toString()
                + Define.URL_SUFFIX
                + "Id="
                + deviceID
                + Define.URL_MIDDLE
                + "StartDate="
                + startDate
                + Define.URL_MIDDLE
                + "EndDate="
                + endDate;
    }

    //endregion

    //region line chart
    public static final float VALUE_ENTRY_NULL = 0;
    public static final float LINE_CHART_WIDTH = 2f;
    public static final float LINE_CHART_CRICLE_RADIUS = 4f;
    public static final float LINE_CHART_CRICLE_HOLE_RADIUS = 2f;
    public static final int ANIMATE_X = 500;
    public static final int ANIMATE_Y = 500;
    public static final float SIZE_TEXT_NO_DATA = 24.0f;
    public static final String TEXT_NO_DATA = "Hiện tại chưa có dữ liệu";
    public static final int[] COLORS_LINE_CHART = new int[]{
            Color.BLUE,
            Color.BLACK,
            Color.RED
    };
    public static final String PROMT_SPINER_PARAM_OPEN = "Select Values";
    public static final String PROMT_SPINER_TRENDS_ALL_DEVICE_OPEN = "Choose device";
    //cấu hình 1p(60000ms) * số phút = một tọa độ
    public static final long TIME_BETWEEN_PIVOT_CHART_TRENDS = 60000 * 60 * 6;
    public static final long TIME_BETWEEN_PIVOT_CHART_DETAIL_DEVICE = 60000 * 30;
    //endregion

    //region config share references
    public static String SHARE_REF_FILE_LOGIN = "sharePrefLogin";
    public static String SHARE_REF_FILE_LOGIN_USER_NAME = "userName";
    public static String SHARE_REF_FILE_LOGIN_PASS = "pass";

    public static String SHARE_REF_FILE_CONFIG = "sharePrefIP";
    public static String SHARE_REF_FILE_CONFIG_IP = "IP";
    //endregion

    //region define enum

    public enum ORDER_MENU_NAVIGATION {
        MENU_LV1_MAIN,
        MENU_LV2_DASHBOARD,
        MENU_LV2_ALL_DEVICE,
        MENU_LV1_REPORT,
        MENU_LV2_TREND,
        MENU_LV2_HISTORY_ALARM_EVENT;

        @Override
        public String toString() {
            if (this == MENU_LV1_MAIN)
                return FRAGMENT_TAG.DASHBOARD_FRAG.name();
            if (this == MENU_LV2_DASHBOARD)
                return FRAGMENT_TAG.DASHBOARD_FRAG.name();
            if (this == MENU_LV2_ALL_DEVICE)
                return FRAGMENT_TAG.ALL_DEVICE_FRAG.name();
            if (this == MENU_LV1_REPORT)
                return FRAGMENT_TAG.TRENDS_FRAG.name();
            if (this == MENU_LV2_TREND)
                return FRAGMENT_TAG.TRENDS_FRAG.name();
            if (this == MENU_LV2_HISTORY_ALARM_EVENT)
                return FRAGMENT_TAG.HISTORY_ALARM_EVENT_FRAG.name();
            return super.toString();
        }
    }

    public enum FRAGMENT_TAG {
        DASHBOARD_FRAG,
        ALL_DEVICE_FRAG,
        CONNECTED_FRAG,
        DISCONNECTED_FRAG,
        ALARM_OR_EVENT_VALUE_FRAG,
        TRENDS_FRAG,
        HISTORY_ALARM_EVENT_FRAG,
        UPDATE_INFO_FRAG,
        UPDATE_PASS_FRAG,
        DETAIL_DEVICE_FRAG,
        DATE_TIME_PICKED_FRAG,
        HISTORY_DETAIL_DEVICE_FRAG;

        @Override
        public String toString() {
            if(this == DASHBOARD_FRAG)
                return "Dashboad";

            if(this == ALL_DEVICE_FRAG)
                return "All Device";

            if(this == CONNECTED_FRAG)
                return "Connected Devices";

            if(this == DISCONNECTED_FRAG)
                return "Disconnected Devices";

            if(this == ALARM_OR_EVENT_VALUE_FRAG)
                return "Alarm or Event";

            if(this == TRENDS_FRAG)
                return "Trend";

            if(this == HISTORY_ALARM_EVENT_FRAG)
                return "History Alarm and Event";

            if(this == UPDATE_INFO_FRAG)
                return "Update Info";


            if(this == UPDATE_PASS_FRAG)
                return "Update pass";


            if(this == DETAIL_DEVICE_FRAG)
                return "Detail Device";

            if(this == DATE_TIME_PICKED_FRAG)
                return "Date time picker";

            if(this == HISTORY_DETAIL_DEVICE_FRAG)
                return "History Device";

            return super.toString();
        }
    }

    public enum DATA_RECYLER {
        ALL_DEVICE_RV,
        CONNECTED_RV,
        DISCONNECTED_RV,
        OVER_VALUE_RV,
        UNDER_VALUE_RV
    }

    public enum BLUR_RADIUS {
        MIN_RADIUS,
        BACKGROUND_LOGIN_RADIUS,
        MAX_RADIUS;

        public float getValues() {
            if (this == MIN_RADIUS)
                return 0.0f;
            if (this == BACKGROUND_LOGIN_RADIUS)
                return 15.0f;
            if (this == MAX_RADIUS)
                return 25.0f;
            return MIN_RADIUS.getValues();
        }
    }

    public enum STRING_DIALOG_HELPER {
        OK,
        CANCLE,
        TITLE_DEFAULT,
        MESAGE_DEFAULT,
        MESAGE_CLICK_YES_DEFAULT;

        @Override
        public String toString() {
            if (this == OK)
                return "Chấp nhận";
            if (this == CANCLE)
                return "Không";
            if (this == TITLE_DEFAULT)
                return "Thông báo";
            if (this == MESAGE_DEFAULT)
                return "Chưa có nội dung";
            if (this == MESAGE_CLICK_YES_DEFAULT)
                return "Bạn đã chấp nhận";
            return "";
        }
    }

    public enum STATE_GET_DEVICE {
        CONNECTED,
        DISCONNECTED;

        public int value() {
            if (this == CONNECTED)
                return 1;
            if (this == DISCONNECTED)
                return 0;
            return -1;
        }
    }

    public enum STATE_GET_HISTORY_ALARM_EVENT_TODAY {
        EVENT,
        ALARM;

        public int getValue() {
            if (this == EVENT)
                return 1;
            if (this == ALARM)
                return 2;
            return -1;
        }

        @Override
        public String toString() {
            if (this == EVENT)
                return "Event";

            if (this == ALARM)
                return "Alarm";

            return super.toString();
        }
    }

    public enum TYPE_FONT {
        IOS_THIN,
        IOS_THIN_COND,
        IOS_THIN_COND_OBL,
        IOS_THIN_EXT,
        IOS_THIN_EXT_OBL,
        IOS_THIN_ITALIC,
        ANDROID_OPENSANS_LIGHT,
        ANDROID_OPENSANS_REGULAR,
        IOS_VNI,
        IOS_VNI_THIN,
        IOS_VNI_BOLD;

        public String getPathFont() {
            if (this == IOS_THIN)
                return "fonts/HelveticaNeue-Thin.ttf";
            if (this == IOS_THIN_COND)
                return "fonts/HelveticaNeue-ThinCond.ttf";
            if (this == IOS_THIN_COND_OBL)
                return "fonts/HelveticaNeue-ThinCondObl.ttf";
            if (this == IOS_THIN_EXT)
                return "fonts/HelveticaNeue-ThinExt.ttf";
            if (this == IOS_THIN_EXT_OBL)
                return "fonts/HelveticaNeue-ThinExtObl.ttf";
            if (this == IOS_THIN_ITALIC)
                return "fonts/HelveticaNeue-ThinItalic.ttf";
            if (this == ANDROID_OPENSANS_LIGHT)
                return "fonts/OpenSans-Light.ttf";
            if (this == ANDROID_OPENSANS_REGULAR)
                return "fonts/OpenSans-Regular.ttf";
            if (this == IOS_VNI)
                return "fonts/iOS/helveticaneuelight.ttf";
            if (this == IOS_VNI_THIN)
                return "fonts/iOS/helveticaneueultralight.ttf";
            if (this == IOS_VNI_BOLD)
                return "fonts/iOS/helveticaneuebold.ttf";

            return super.toString();
        }
    }

    //endregion

    //region type date time
    public static final String TYPE_DATE_TIME_FULL = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String TYPE_DATE_TIME_FULL_TYPE_2 = "HH:mm:ss dd/MM/yyyy";
    public static final String TYPE_DATE_TIME_FULL_TYPE_3 = "HH'h'mm dd/MM/yyyy";
    public static final String TYPE_DATE_TIME_HH_MM = "HH'h'mm'p'";
    public static final String TYPE_DATE_TIME_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String TYPE_DATE_TIME_YYYY_MM_DD = "yyyy/MM/dd";
    //endregion

    //region manager name variable bundle
    public static final String PARAM_NAME_USER = "USER";
    public static final String PARAM_DEVICE_DETAIL = "DEVICE_DETAIL";
    public static final String PARAM_ID_TEXT_DATE_TIME_PICKER = "PARAM_ID_TEXT_DATE_TIME_PICKER";
    public static final String PARAM_CALLBACK_DATE_TIME_PICKER = "PARAM_CALLBACK_DATE_TIME_PICKER";
    //endregion

    //region config db
    public static final String PROGRAM_DB_PATH = "/RECLOSER/DB/";
    public static final String DATABASE_NAME = "RECLOSER.s3db";
    public static final String PROGRAM_PATH = "/RECLOSER/";
    public static final String PROGRAM_PHOTOS_PATH = "/RECLOSER/PHOTOS/";
    //endregion

    //region query db table UserEntity
    public static final String TABLE_USER = "UserEntity";
    public static final String TABLE_USER_UserName = "UserName";
    public static final String TABLE_USER_Password = "Password";
    public static final String TABLE_USER_FullName = "FullName";
    public static final String TABLE_USER_Email = "Email";
    public static final String TABLE_USER_PhoneNumber = "PhoneNumber";
    public static final String TABLE_USER_Status = "Status";
    public static final String TABLE_USER_UsedApp = "UsedApp";
    public static final String TABLE_USER_UserType = "UserType";
    public static final String TABLE_USER_MobileVersion = "MobileVersion";

    public static final String QUERY_CREATE_TABLE_USER = "CREATE TABLE " +
            TABLE_USER +
            "(" +
            TABLE_USER_UserName +
            " TEXT PRIMARY KEY NOT NULL," +
            TABLE_USER_Password +
            " TEXT," +
            TABLE_USER_FullName +
            " TEXT," +
            TABLE_USER_Email +
            " TEXT," +
            TABLE_USER_PhoneNumber +
            " TEXT," +
            TABLE_USER_Status +
            " INTEGER," +
            TABLE_USER_UsedApp +
            " TEXT," +
            TABLE_USER_UserType +
            " INTEGER," +
            TABLE_USER_MobileVersion +
            " INTEGER" +
            ")";
    public static final String QUERY_DROP_TABLE_USER = "DROP TABLE IF EXISTS" +
            TABLE_USER;

    //TODO check user
    public static String queryCheckUser(String UserName) {
        return "SELECT * FROM " + TABLE_USER + " WHERE UserName = '" + UserName + "'";
    }
    //endregion
}
