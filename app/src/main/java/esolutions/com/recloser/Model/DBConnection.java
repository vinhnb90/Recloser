package esolutions.com.recloser.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

import esolutions.com.recloser.Utils.Class.Define;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class DBConnection extends SQLiteOpenHelper{
    private static DBConnection sInstance;
    private SQLiteDatabase database;
    private static final int DATABASE_VERSION = 1;

    private DBConnection(final Context context){
        //check create config
        super(context, Environment.getExternalStorageDirectory() + Define.  PROGRAM_DB_PATH + Define.DATABASE_NAME, null, DATABASE_VERSION);
        this.checkFileDBExist();
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + Define.PROGRAM_DB_PATH + Define.DATABASE_NAME, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Define.QUERY_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Define.QUERY_DROP_TABLE_USER);
        onCreate(db);
    }

    public static synchronized DBConnection getInstance(Context context)
    {
        if(sInstance ==null)
        {
            sInstance = new DBConnection(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public synchronized void close() {
        super.close();
        if (database != null) {
            database.close();
            database = null;
        }
    }

    //TODO access data return a cursor
    public Cursor runQueryReturnCursor(String queryString) throws Exception{
        if (queryString == null || queryString.isEmpty())
            throw new Exception("Query String null!");
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    //TODO delete table
    public long deleteData(String tableDelete) throws Exception{
        if (tableDelete == null || tableDelete.isEmpty())
            throw new Exception("table Delete null!");
        database = this.getWritableDatabase();
        return database.delete(tableDelete, null, null);
    }

    //TODO insert data
    public long insertData(ContentValues contentValues, String tableInsert) throws Exception{
        if (tableInsert == null || tableInsert.isEmpty())
            throw new Exception("Table Insert null!");
        if (contentValues == null)
            throw new Exception("ContentValues null!");
        database = this.getWritableDatabase();
        if (contentValues == null) {
            return -1;
        }
        long ins = database.insert(tableInsert, null, contentValues);
        database.close();
        return ins;
    }

    //TODO update data
    public long updateData(ContentValues contentValues, String tableUpdate, String idCollumn, String valueColumn) throws Exception{
        if (contentValues == null)
            throw new Exception("ContentValues null!");
        if (tableUpdate == null || tableUpdate.isEmpty())
            throw new Exception("Table Update null!");
        if (idCollumn == null || idCollumn.isEmpty())
            throw new Exception("idCollumn null!");
        if (valueColumn == null || valueColumn.isEmpty())
            throw new Exception("valueColumn null!");
        database = this.getWritableDatabase();
        if (contentValues == null) {
            return -1;
        }
        long update = database.update(tableUpdate, contentValues, idCollumn + "=?", new String[]{valueColumn});
        database.close();
        return update;
    }

    //TODO check exists row
    public boolean isRowExistData(String queryCheckExist) throws Exception{
        if (queryCheckExist == null || queryCheckExist.isEmpty())
            throw new Exception("queryCheckExist null!");
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(queryCheckExist, null);
        int totalRows = cursor.getCount();
        cursor.close();
        if (totalRows > 0) {
            return true;
        } else return false;
    }

    //TODO check file
    //region tạo đường dẫn lưu
    private void checkFileDBExist() {
        File programDirectory = new File(Environment.getExternalStorageDirectory() + Define.PROGRAM_PATH);
        if (!programDirectory.exists()) {
            programDirectory.mkdirs();
        }
        File programDbDirectory = new File(Environment.getExternalStorageDirectory() + Define.PROGRAM_DB_PATH);
        if (!programDbDirectory.exists()) {
            programDbDirectory.mkdirs();
        }
        File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + Define.PROGRAM_PHOTOS_PATH);
        if (!programPhotoDirectory.exists()) {
            programPhotoDirectory.mkdirs();
        }
    }
}
