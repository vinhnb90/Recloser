package esolutions.com.recloser.Presenter.Interface;

import android.os.AsyncTask;

/**
 * interface generics phục vụ việc implement method Asyntask
 * tham số IMethodAsyntask<T, U, V> tương đương Asyntask<T, U, V>
 * <p>
 * Created by VinhNB on 4/1/2017.
 */

public interface IMethodAsyntask<T, U, V> {
    void processOnPreExecute();

    V doInBackground(T... argDoInBackground);

    void processOnProgressUpdate(U... argUpdate);

    void processOnPostExecute(V... argPost);

    void processOnCountdown(final Class<? extends AsyncTask<T, U, V>> argClassAsyntask);
}
