package esolutions.com.recloser.Entity;

/**
 * class này chỉ dùng cho việc lưu trữ biến và vị trí pivot X của line trong class MainPresenter,
 * không đóng vai trò khác.
 * Created by VinhNB on 3/22/2017.
 */

public class ObjectPivotXEntity {
    private String timeStamp;
    private boolean isAddNewPivotX;

    public ObjectPivotXEntity(String timeStamp, boolean isAddNewPivotX) {
        this.timeStamp = timeStamp;
        this.isAddNewPivotX = isAddNewPivotX;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isAddNewPivotX() {
        return isAddNewPivotX;
    }

    public void setAddNewPivotX(boolean addNewPivotX) {
        isAddNewPivotX = addNewPivotX;
    }
}
