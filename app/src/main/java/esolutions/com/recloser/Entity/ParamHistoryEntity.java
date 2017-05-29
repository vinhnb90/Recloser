package esolutions.com.recloser.Entity;

import android.support.annotation.NonNull;

import java.util.Comparator;

import esolutions.com.recloser.Utils.Class.CommonMethod;
import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 3/17/2017.
 */

public class ParamHistoryEntity implements Comparable<ParamHistoryEntity> {
//        private String TagNameOPCData;
    private String Value;
    private String Timestamp;
    private String Unit;
    private String Description;

//    public String getTagNameOPCData() {
//        return TagNameOPCData;
//    }
//
//    public void setTagNameOPCData(String tagNameOPCData) {
//        TagNameOPCData = tagNameOPCData;
//    }

    public String getValue() {
        Value = CommonMethod.removeSpace(this.Value);
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    /**
     * remove các khoảng trắng, do trường getValue trên database sql định dang string
     *
     * @param valueData
     * @return
     */
    private String removeSpace(String valueData) {
        return valueData.replaceAll(" ", "");
    }

    @Override
    public int compareTo(@NonNull ParamHistoryEntity paramHistoryEntity) {
        long old = CommonMethod.convertDateToLong(this.getTimestamp(), Define.TYPE_DATE_TIME_FULL);
        long newest = CommonMethod.convertDateToLong(paramHistoryEntity.getTimestamp(), Define.TYPE_DATE_TIME_FULL);
        int result = (int) (old - newest);
        return result;
    }


    public static Comparator<ParamHistoryEntity> ParamHistoryEntityComparator
            = new Comparator<ParamHistoryEntity>() {

        public int compare(ParamHistoryEntity fruit1, ParamHistoryEntity fruit2) {
            //ascending order
            return fruit1.compareTo(fruit2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    };
}
