package esolutions.com.recloser.Entity;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/11/2017.
 */

public class ObjectHistoryAndAlarmTodayInside {
    private String deviceName;
    private String events;
    private String value;
    private String description;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class ComparHistoryAndAlarmEventJSONTodayByDeviceName implements Comparator<ObjectHistoryAndAlarmTodayInside> {

        @Override
        public int compare(ObjectHistoryAndAlarmTodayInside objectHistoryAndAlarmTodayInside, ObjectHistoryAndAlarmTodayInside t1) {
            String nameDevice = objectHistoryAndAlarmTodayInside.getDeviceName();
            String nameDeviceOther = t1.getDeviceName();

            return nameDevice.compareTo(nameDeviceOther);
        }

    }
}
