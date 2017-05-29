package esolutions.com.recloser.Entity;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/11/2017.
 */

public class HistoryAndAlarmEventJSONToday {
    private String Events;
    private String Timestamp;
    private String Value;
    private String Description;
    private String Name;
    private String DeviceId;

    public String getEvents() {
        return Events;
    }

    public void setEvents(String events) {
        Events = events;
    }

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public static class ComparHistoryAndAlarmEventJSONTodayByTimeStamp implements Comparator<HistoryAndAlarmEventJSONToday> {

        @Override
        public int compare(HistoryAndAlarmEventJSONToday historyAndAlarmEventJSONToday, HistoryAndAlarmEventJSONToday t1) {
            String timeNow = historyAndAlarmEventJSONToday.getTimestamp();
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
