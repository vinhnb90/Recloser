package esolutions.com.recloser.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class DetailDeviceEntity {
    private int DeviceID;
    private String DeviceName;
    private String DeviceLocation;
    private String DeviceTypeName;
    private String DeviceDescription;
    private String DeviceAvartar;
    ArrayList<ParamDetailDevice> ParamDetailDevices;
    ArrayList<EventDetailDevice> EventDetailDevices;

    public int getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(int deviceID) {
        DeviceID = deviceID;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceLocation() {
        return DeviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        DeviceLocation = deviceLocation;
    }

    public String getDeviceTypeName() {
        return DeviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        DeviceTypeName = deviceTypeName;
    }

    public String getDeviceDescription() {
        return DeviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        DeviceDescription = deviceDescription;
    }

    public String getDeviceAvartar() {
        return DeviceAvartar;
    }

    public void setDeviceAvartar(String deviceAvartar) {
        DeviceAvartar = deviceAvartar;
    }

    public ArrayList<ParamDetailDevice> getParamDetailDevices() {
        return ParamDetailDevices;
    }

    public void setParamDetailDevices(ArrayList<ParamDetailDevice> paramDetailDevices) {
        ParamDetailDevices = paramDetailDevices;
    }

    public ArrayList<EventDetailDevice> getEventDetailDevices() {
        return EventDetailDevices;
    }

    public void setEventDetailDevices(ArrayList<EventDetailDevice> eventDetailDevices) {
        EventDetailDevices = eventDetailDevices;
    }

    public static class ParamDetailDevice {
        private String TagNameOPCData;
        private String DescriptionData;
        private double ValueData;
        private String TimestampData;
        private String UnitData;
        private String AlarmGroup;
        private boolean ShowOnDashboard;

        public String getTagNameOPCData() {
            return TagNameOPCData;
        }

        public void setTagNameOPCData(String tagNameOPCData) {
            TagNameOPCData = tagNameOPCData;
        }

        public String getDescriptionData() {
            return DescriptionData;
        }

        public void setDescriptionData(String descriptionData) {
            DescriptionData = descriptionData;
        }

        public double getValueData() {
            return ValueData;
        }

        public void setValueData(double valueData) {
            ValueData = valueData;
        }

        public String getTimestampData() {
            return TimestampData;
        }

        public void setTimestampData(String timestampData) {
            TimestampData = timestampData;
        }

        public String getUnitData() {
            return UnitData;
        }

        public void setUnitData(String unitData) {
            UnitData = unitData;
        }

        public String getAlarmGroup() {
            return AlarmGroup;
        }

        public void setAlarmGroup(String alarmGroup) {
            AlarmGroup = alarmGroup;
        }

        public boolean isShowOnDashboard() {
            return ShowOnDashboard;
        }

        public void setShowOnDashboard(boolean showOnDashboard) {
            ShowOnDashboard = showOnDashboard;
        }
    }

    public static class EventDetailDevice {
        private String TagNameOPCEvent;
        private String TimestampEvent;
        private Boolean ValueEvent;
        private String DescriptionEvent;

        public String getTagNameOPCEvent() {
            return TagNameOPCEvent;
        }

        public void setTagNameOPCEvent(String tagNameOPCEvent) {
            TagNameOPCEvent = tagNameOPCEvent;
        }

        public String getTimestampEvent() {
            return TimestampEvent;
        }

        public void setTimestampEvent(String timestampEvent) {
            TimestampEvent = timestampEvent;
        }

        public Boolean getValueEvent() {
            return ValueEvent;
        }

        public void setValueEvent(Boolean valueEvent) {
            ValueEvent = valueEvent;
        }

        public String getDescriptionEvent() {
            return DescriptionEvent;
        }

        public void setDescriptionEvent(String descriptionEvent) {
            DescriptionEvent = descriptionEvent;
        }
    }
}
