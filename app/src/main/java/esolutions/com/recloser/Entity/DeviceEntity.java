package esolutions.com.recloser.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class DeviceEntity implements Parcelable{
    private final String IpAddress;
    private final String Name;
    private int Left;
    private int Top;
    private int Width;
    private int Height;
    private final int ID;
    private final boolean Status;


    public DeviceEntity(DeviceBuilder deviceBuilder) {
        IpAddress = deviceBuilder.IpAddress;
        Name = deviceBuilder.Name;
        Left = deviceBuilder.Left;
        Top = deviceBuilder.Top;
        Width = deviceBuilder.Width;
        Height = deviceBuilder.Height;
        ID = deviceBuilder.ID;
        Status = deviceBuilder.STATUS;

    }

    protected DeviceEntity(Parcel in) {
        IpAddress = in.readString();
        Name = in.readString();
        Left = in.readInt();
        Top = in.readInt();
        Width = in.readInt();
        Height = in.readInt();
        ID = in.readInt();
        Status = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IpAddress);
        dest.writeString(Name);
        dest.writeInt(Left);
        dest.writeInt(Top);
        dest.writeInt(Width);
        dest.writeInt(Height);
        dest.writeInt(ID);
        dest.writeByte((byte) (Status ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceEntity> CREATOR = new Creator<DeviceEntity>() {
        @Override
        public DeviceEntity createFromParcel(Parcel in) {
            return new DeviceEntity(in);
        }

        @Override
        public DeviceEntity[] newArray(int size) {
            return new DeviceEntity[size];
        }
    };

    public String getIpAddress() {
        return IpAddress;
    }

    public String getName() {
        return Name;
    }

    public int getLeft() {
        return Left;
    }

    public int getTop() {
        return Top;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int getID() {
        return ID;
    }

    public boolean isStatus() {
        return Status;
    }

    public static class DeviceBuilder {
        private final String IpAddress;
        private final String Name;
        private final int Left;
        private final int Top;
        private final int Width;
        private final int Height;
        private final int ID;
        private final boolean STATUS;

        public DeviceBuilder(String ipAddress, String name, int ID, boolean STATUS) {
            IpAddress = ipAddress;
            Name = name;
            this.ID = ID;
            this.STATUS = STATUS;
            Left = 0;
            Top = 0;
            Width = 0;
            Height = 0;
        }

        public DeviceEntity build() {
            return new DeviceEntity(this);
        }

    }
}
