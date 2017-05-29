package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/7/2017.
 */

public class ResponseServerLoginJSON {
    private Boolean result;

    public ResponseServerLoginJSON(Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
