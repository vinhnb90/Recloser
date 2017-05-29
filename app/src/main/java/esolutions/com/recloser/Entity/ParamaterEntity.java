package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class ParamaterEntity {
    private final String mTitleParamater;
    private final boolean isStatus;
    private boolean statusOnOff;
    private double mValueParamater;
    private String mUnitParamater;

    private ParamaterEntity(ParamaterBuilder paramaterBuilder) {
        this.mTitleParamater = paramaterBuilder.mParamaterTitle;
        this.isStatus = paramaterBuilder.isStatus;
        this.statusOnOff = paramaterBuilder.statusOnOff;
        this.mValueParamater = paramaterBuilder.mValueParamater;
        this.mUnitParamater = paramaterBuilder.mUnitParamater;
    }

    public String getmTitleParamater() {
        return mTitleParamater;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public boolean isStatusOnOff() {
        return statusOnOff;
    }

    public double getmValueParamater() {
        return mValueParamater;
    }

    public String getmUnitParamater() {
        return mUnitParamater;
    }

    public static class ParamaterBuilder {
        private final String mParamaterTitle;
        private final boolean isStatus;
        private boolean statusOnOff;
        private double mValueParamater;
        private String mUnitParamater;

        public ParamaterBuilder(String mParamaterTitle, boolean isStatus) {
            this.mParamaterTitle = mParamaterTitle;
            this.isStatus = isStatus;
        }

        public ParamaterBuilder setStatusOnOff(boolean statusOnOff) throws Exception {
            if (isStatus == false)
                throw new RuntimeException(mParamaterTitle + " must set isEvents is true");
            this.statusOnOff = statusOnOff;
            return this;
        }

        public ParamaterBuilder setValueAndUnit(double mValueParamater, String mUnitParamater) {
            this.mValueParamater = mValueParamater;
            this.mUnitParamater = mUnitParamater;
            return this;
        }

        public ParamaterEntity build() {
            return new ParamaterEntity(this);
        }

    }

}
