package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/8/2017.
 */

public class MobileCountDeviceJSON {
    private int connected;
    private int disconnected;
    private int overvalue;
    private int undervalue;

    public MobileCountDeviceJSON(int connected, int disconnected, int overvalue, int undervalue) {
        this.connected = connected;
        this.disconnected = disconnected;
        this.overvalue = overvalue;
        this.undervalue = undervalue;
    }

    public int getConnected() {
        return connected;
    }

    public void setConnected(int connected) {
        this.connected = connected;
    }

    public int getDisconnected() {
        return disconnected;
    }

    public void setDisconnected(int disconnected) {
        this.disconnected = disconnected;
    }

    public int getOvervalue() {
        return overvalue;
    }

    public void setOvervalue(int overvalue) {
        this.overvalue = overvalue;
    }

    public int getUndervalue() {
        return undervalue;
    }

    public void setUndervalue(int undervalue) {
        this.undervalue = undervalue;
    }
}
