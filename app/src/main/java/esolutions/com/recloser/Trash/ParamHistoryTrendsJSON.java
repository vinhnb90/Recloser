package esolutions.com.recloser.Trash;

import android.support.annotation.NonNull;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 4/5/2017.
 * {"Name":"REC 973","TagNameESLink":"AI0","Description":"Uab","Value":" 20 535.00","Unit":"V","Timestamp":"2017-04-04T11:00:00"}
 */

public class ParamHistoryTrendsJSON implements Comparable<ParamHistoryTrendsJSON> {
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

    @Override
    public int compareTo(@NonNull ParamHistoryTrendsJSON paramHistoryTrendsJSON) {
        long old = CommonMethod.convertDateToLong(this.getTimestamp(), Define.TYPE_DATE_TIME_FULL);
        long newest = CommonMethod.convertDateToLong(paramHistoryTrendsJSON.getTimestamp(), Define.TYPE_DATE_TIME_FULL);
        int result = (int) (old - newest);
        return result;
    }

    public static Comparator<ParamHistoryTrendsJSON> ParamHistoryTrendsJSONComparator
            = new Comparator<ParamHistoryTrendsJSON>() {

        public int compare(ParamHistoryTrendsJSON fruit1, ParamHistoryTrendsJSON fruit2) {
            //ascending order
            return fruit1.compareTo(fruit2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    };
}