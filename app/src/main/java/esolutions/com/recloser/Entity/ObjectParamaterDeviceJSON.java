package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/3/2017.
 */

public class ObjectParamaterDeviceJSON {
    private String TagNameOPCData;
    private String ValueData;
    private String TimestampData;
    private String Unit;
    private String DescriptionData;

    public ObjectParamaterDeviceJSON(String tagNameOPCData, String valueData, String timestampData, String unit, String descriptionData) {
        TagNameOPCData = tagNameOPCData;
        ValueData = valueData;
        TimestampData = timestampData;
        Unit = unit;
        DescriptionData = descriptionData;
    }

    public String getTagNameOPCData() {
        return TagNameOPCData;
    }

    public void setTagNameOPCData(String tagNameOPCData) {
        TagNameOPCData = tagNameOPCData;
    }

    public String getValueData() {
        return ValueData;
    }

    public void setValueData(String valueData) {
        ValueData = valueData;
    }

    public String getTimestampData() {
        return TimestampData;
    }

    public void setTimestampData(String timestampData) {
        TimestampData = timestampData;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDescriptionData() {
        return DescriptionData;
    }

    public void setDescriptionData(String descriptionData) {
        DescriptionData = descriptionData;
    }
}
