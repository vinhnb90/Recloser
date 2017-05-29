package esolutions.com.recloser.Trash;

import android.os.AsyncTask;

import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esolutions.com.recloser.Entity.ObjectPublishAsyntask;

/**
 * Created by VinhNB on 4/1/2017.
 */

public abstract class AsyntaskImp<T, V> {
    private Class<T> typeDoIn;
    private Class<V> typePostExcute;

//    public AsyntaskImp(T doIn, V postExcute) {
//        this.doIn = doIn;
//        this.postExcute = postExcute;
//    }
//
//    public T getDoIn() {
//        return doIn;
//    }
//
//    public void setDoIn(T doIn) {
//        this.doIn = doIn;
//    }
//
//    public V getPostExcute() {
//        return postExcute;
//    }
//
//    public void setPostExcute(V postExcute) {
//        this.postExcute = postExcute;
//    }


    public AsyntaskImp(Class<T> typeDoIn, Class<V> typePostExcute) {
        this.typeDoIn = typeDoIn;
        this.typePostExcute = typePostExcute;
    }

    public Class<T> getTypeDoIn() {
        return typeDoIn;
    }

    public Class<V> getTypePostExcute() {
        return typePostExcute;
    }

    protected abstract void processOnPreExecute();

    protected abstract void processOnProgressUpdate(ObjectPublishAsyntask... argUpdate);

    protected abstract void processOnPostExecute( Map<String, Object> argPost);

//    protected abstract void processOnCountdown(final ConnectServer.AsyncCallGetAllDevice<T, V> argClassAsyntask);
}
