package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/16/2017.
 */

public class ObjectSpinnerParamEntity extends ObjectSpinnerParamJSONEntity {
    private boolean mSelected;

    public ObjectSpinnerParamEntity() {
    }

    public ObjectSpinnerParamEntity(ObjectSpinnerParamJSONEntity objectSpinnerParamJSONEntity) {
        this.setTagNameOPCData(objectSpinnerParamJSONEntity.getTagNameOPCData());
        this.setParamDescription(objectSpinnerParamJSONEntity.getParamDescription());
        this.setmSelected(false);
    }

    public String getTagNameOPCData() {
        return TagNameOPCData;
    }

    public void setTagNameOPCData(String tagNameOPCData) {
        this.TagNameOPCData = tagNameOPCData;
    }

    public String getParamDescription() {
        return ParamDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.ParamDescription = paramDescription;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
