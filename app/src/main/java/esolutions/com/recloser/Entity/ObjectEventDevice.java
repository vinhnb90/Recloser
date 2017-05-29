package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/22/2017.
 */

public class ObjectEventDevice {
    private String TagNameOPCEvent;
    private String TimestampEvent;
    private Boolean ValueEvent;
    private String DescriptionEvent;

    public String getTagNameOPCEvent() {
        return TagNameOPCEvent;
    }

    public void setTagNameOPCEvent(String tagNameOPCEvent) {
        TagNameOPCEvent = tagNameOPCEvent;
    }

    public String getTimestampEvent() {
        return TimestampEvent;
    }

    public void setTimestampEvent(String timestampEvent) {
        TimestampEvent = timestampEvent;
    }

    public Boolean getValueEvent() {
        return ValueEvent;
    }

    public void setValueEvent(Boolean valueEvent) {
        ValueEvent = valueEvent;
    }

    public String getDescriptionEvent() {
        return DescriptionEvent;
    }

    public void setDescriptionEvent(String descriptionEvent) {
        DescriptionEvent = descriptionEvent;
    }
}
