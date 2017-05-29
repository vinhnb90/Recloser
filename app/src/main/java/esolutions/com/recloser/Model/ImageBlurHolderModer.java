package esolutions.com.recloser.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by VinhNB on 3/7/2017.
 */
public class ImageBlurHolderModer {
    private static Context sContext;
    private static ImageBlurHolderModer ourInstance;
    private static Bitmap sBackgroundBlur;

    private ImageBlurHolderModer(Context context) {
        this.sContext = context;
    }

    public synchronized static ImageBlurHolderModer getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ImageBlurHolderModer(context);
        return ourInstance;
    }

    public Bitmap getsBackgroundBlur() {

        return sBackgroundBlur;
    }

    public void setsBackgroundBlur(Context context, Bitmap bitmap, float blurRadius) throws Exception {
        if (bitmap == null)
            throw new Exception("bitmap null");
        ;

        if (blurRadius < 0.0f || blurRadius > 25.0f) {
            throw new Exception("blurRadius must be between 0.0 and 25.0");
        }

        if (ourInstance == null)
            ourInstance = new ImageBlurHolderModer(context);

        if (sBackgroundBlur == null)
            sBackgroundBlur = blur(context, bitmap, blurRadius);

        return;
    }


    private Bitmap blur(Context ctx, Bitmap image, float blurRadius) {
        int width = image.getWidth();
        int height = image.getHeight();

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    private Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
}
