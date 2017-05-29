package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/10/2017.
 */

public class ParamOverUnderEntity {
    private final int ID;
    private final String Description;
    private final String Timestamp;
    private final String Value;
    private final String Events;
    private final int MinValue;
    private final int MaxValue;
    private final String Name;
    private final String DeviceID;
    private final String UserName;

    public ParamOverUnderEntity(ParamOverUnderBuilder builder) {
        ID = builder.ID;
        Description = builder.Description;
        Timestamp = builder.Timestamp;
        Value = builder.Value;
        Events = builder.Events;
        MinValue = builder.MinValue;
        MaxValue = builder.MaxValue;
        Name = builder.Name;
        DeviceID = builder.DeviceID;
        UserName = builder.UserName;
    }

    public int getID() {
        return ID;
    }

    public String getDescription() {
        return Description;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getValue() {
        return Value;
    }

    public String getEvents() {
        return Events;
    }

    public int getMinValue() {
        return MinValue;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public String getName() {
        return Name;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public String getUserName() {
        return UserName;
    }

    private static class ParamOverUnderBuilder {
        private final int ID;
        private final String Description;
        private final String Timestamp;
        private final String Value;
        private final String Events;
        private final int MinValue;
        private final int MaxValue;
        private final String Name;
        private final String DeviceID;
        private final String UserName;

        public ParamOverUnderBuilder(int ID, String description, String timestamp, String value, String events, int minValue, int maxValue, String name, String deviceID, String userName) {
            this.ID = ID;
            Description = description;
            Timestamp = timestamp;
            Value = value;
            Events = events;
            MinValue = minValue;
            MaxValue = maxValue;
            Name = name;
            DeviceID = deviceID;
            UserName = userName;
        }

        public ParamOverUnderEntity build() {
            return new ParamOverUnderEntity(this);
        }
    }

}
