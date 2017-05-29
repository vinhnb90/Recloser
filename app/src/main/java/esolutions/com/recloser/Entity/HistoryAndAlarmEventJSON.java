package esolutions.com.recloser.Entity;

import android.support.annotation.NonNull;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/8/2017.
 */

public class HistoryAndAlarmEventJSON {
    private String Timestamp;
    private String Value;
    private String DescriptionInfo;
    private String DescriptionData;

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDescriptionInfo() {
        return DescriptionInfo;
    }

    public void setDescriptionInfo(String descriptionInfo) {
        DescriptionInfo = descriptionInfo;
    }

    public String getDescriptionData() {
        return DescriptionData;
    }

    public void setDescriptionData(String descriptionData) {
        DescriptionData = descriptionData;
    }


    public static class ComparHistoryAndAlarmEventJSONByTimeStamp implements Comparator<HistoryAndAlarmEventJSON> {

        @Override
        public int compare(HistoryAndAlarmEventJSON historyAndAlarmEventJSON, HistoryAndAlarmEventJSON t1) {
            String timeNow = historyAndAlarmEventJSON.getTimestamp();
            long timeNowL = CommonMethod.convertDateToLong(timeNow, Define.TYPE_DATE_TIME_FULL);
            String timeDifferent = t1.getTimestamp();
            long timeDifferentL = CommonMethod.convertDateToLong(timeDifferent, Define.TYPE_DATE_TIME_FULL);

            if (timeNowL < timeDifferentL)
                return -1;

            if (timeNowL > timeDifferentL)
                return 1;

            else
                return 0;
        }

    }
}
