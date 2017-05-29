package esolutions.com.recloser.Entity;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/11/2017.
 */

public class HistoryDetailDeviceJSON {
    private String Name;
    private String TagNameESLink;
    private String Description;
    private String Value;
    private String Unit;
    private String Timestamp;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTagNameESLink() {
        return TagNameESLink;
    }

    public void setTagNameESLink(String tagNameESLink) {
        TagNameESLink = tagNameESLink;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public static class ComparHistoryDetailDeviceJSONByTimeStamp implements Comparator<HistoryDetailDeviceJSON> {

        @Override
        public int compare(HistoryDetailDeviceJSON historyDetailDeviceJSON, HistoryDetailDeviceJSON t1) {
            String timeNow = historyDetailDeviceJSON.getTimestamp();
            long timeNowL = CommonMethod.convertDateToLong(timeNow, Define.TYPE_DATE_TIME_FULL);
            String timeDifferent = t1.getTimestamp();
            long timeDifferentL = CommonMethod.convertDateToLong(timeDifferent, Define.TYPE_DATE_TIME_FULL);

            if (timeNowL < timeDifferentL)
                return -1;

            if (timeNowL > timeDifferentL)
                return 1;

            else
                return 0;
        }
    }
}