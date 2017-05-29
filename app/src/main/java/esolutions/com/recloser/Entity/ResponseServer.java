package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/7/2017.
 */

public class ResponseServer {
    private Boolean result;

    public ResponseServer(Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
