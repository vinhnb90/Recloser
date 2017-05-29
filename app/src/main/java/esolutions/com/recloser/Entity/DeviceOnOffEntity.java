package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/10/2017.
 */

public class DeviceOnOffEntity {
    private final int ID;
    private final String Name;
    private final int Type;
    private final String IpAddress;
    private final boolean Status;
    private final String Timestamp;

    public DeviceOnOffEntity(DeviceConnectedBuilder deviceConnectedBuilder) {
        this.ID = deviceConnectedBuilder.ID;
        this.Name = deviceConnectedBuilder.Name;
        this.Type = deviceConnectedBuilder.Type;
        this.IpAddress = deviceConnectedBuilder.IpAddress;
        this.Status = deviceConnectedBuilder.Status;
        this.Timestamp = deviceConnectedBuilder.Timestamp;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public int getType() {
        return Type;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    private static class DeviceConnectedBuilder {
        private final int ID;
        private final String Name;
        private final int Type;
        private final String IpAddress;
        private final boolean Status;
        private final String Timestamp;

        public DeviceConnectedBuilder(int ID, String name, int type, String ipAddress, boolean status, String timestamp) {
            this.ID = ID;
            Name = name;
            Type = type;
            IpAddress = ipAddress;
            Status = status;
            Timestamp = timestamp;
        }

        public DeviceOnOffEntity build() {
            return new DeviceOnOffEntity(this);
        }
    }

}
