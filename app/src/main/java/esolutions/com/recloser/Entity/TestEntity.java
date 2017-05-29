package esolutions.com.recloser.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class TestEntity {
    private final String mDeviceName;

    private TestEntity(TestBuilder deviceTestBuilder) {
        this.mDeviceName = deviceTestBuilder.mDevice;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public static class TestBuilder {
        private final String mDevice;

        public TestBuilder(String mDevice) {
            this.mDevice = mDevice;
        }

        public TestEntity build() {
            return new TestEntity(this);
        }

    }
}
