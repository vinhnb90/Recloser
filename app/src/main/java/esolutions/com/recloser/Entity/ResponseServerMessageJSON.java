package esolutions.com.recloser.Entity;

/**
 * class thuộc loại con của ResponseServerJSON
 * Created by VinhNB on 3/23/2017.
 */

public class ResponseServerMessageJSON extends ResponseServerJSON{
    private String Message;

    public ResponseServerMessageJSON(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
