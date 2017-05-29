package esolutions.com.recloser.Utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;

import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.Application.ApplicationDefault;

/**
 * Created by VinhNB on 3/23/2017.
 */

public class FontsManager {
    private static FontsManager instanse;

    private FontsManager() {

    }

    public static synchronized FontsManager getInstanse() {
        if (instanse == null)
            instanse = new FontsManager();
        return instanse;
    }

    public Typeface getTypeFace(Context context, Define.TYPE_FONT typeFont) {
        return Typeface.createFromAsset(context.getAssets(), typeFont.getPathFont());
    }

}
