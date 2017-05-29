package esolutions.com.recloser.Entity;

import android.support.annotation.Nullable;

/**
 * Created by VinhNB on 3/16/2017.
 */

public class ItemSpinnerDetailDeviceEntity {
    private String mTagNameOPCData;
    private String mParamDescription;
    private boolean mSelected;

    public String getmTagNameOPCData() {
        return mTagNameOPCData;
    }

    public void setmTagNameOPCData(@Nullable String mTagNameOPCData) {
        this.mTagNameOPCData = mTagNameOPCData;
    }

    public String getmParamDescription() {
        return mParamDescription;
    }

    public void setmParamDescription(String mParamDescription) {
        this.mParamDescription = mParamDescription;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
