package esolutions.com.recloser.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class DeviceTestEntity {
    private final String mDeviceName;
    private ArrayList<ObjectParamaterDeviceJSON> mParamaterEntities;

    private DeviceTestEntity(DeviceTestBuilder deviceTestBuilder) {
        this.mDeviceName = deviceTestBuilder.mDevice;
        this.mParamaterEntities = (deviceTestBuilder.paramaterEntities == null) ? new ArrayList<ObjectParamaterDeviceJSON>() : deviceTestBuilder.paramaterEntities;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public List<ObjectParamaterDeviceJSON> getmParamaterEntities() {
        return mParamaterEntities;
    }

    public static class DeviceTestBuilder {
        private final String mDevice;
        private ArrayList<ObjectParamaterDeviceJSON> paramaterEntities;

        public DeviceTestBuilder(String mDevice) {
            this.mDevice = mDevice;
        }

        public DeviceTestBuilder setParamater(ArrayList<ObjectParamaterDeviceJSON> deviceStatus) {
            this.paramaterEntities = deviceStatus;
            return this;
        }

        public DeviceTestEntity build() {
            return new DeviceTestEntity(this);
        }

    }
}
