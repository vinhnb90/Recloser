package esolutions.com.recloser.Utils.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class CommonMethod {

    public static String getJSONDataFromURL(String urlString) {
        if (urlString == null || urlString.isEmpty())
            return null;
        // lấy chuỗi JSON từ server
        BufferedReader reader = null;
        StringBuffer buffer = null;
        InputStreamReader isr;
        URL url = null;
        try {
            url = new URL(urlString);
            isr = new InputStreamReader(url.openStream());
            reader = new BufferedReader(isr);

            buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public static boolean isConnectingWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return false;
        } else return true;
    }

    public static double findMax(double... vars) {
        double max = Double.NaN;
        for (double var :
                vars) {
            if (var > max)
                max = var;
        }
        return max;
    }

    public static String convertLongToDate(long time, String format) {
        if (time < 0)
            return null;
        SimpleDateFormat df2 = new SimpleDateFormat(format);
        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(time);
        return df2.format(date);
    }

    public static long convertDateToLong(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateParse;
        try {
            dateParse = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return dateParse.getTime();
    }

    public static String getDateNow(String formatDate) {
        SimpleDateFormat df = new SimpleDateFormat(formatDate);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(Calendar.getInstance().getTime());
    }

    public static String getBeginTimeOfDay(String date, String formatDateInput) {
        long longDate = CommonMethod.convertDateToLong(date, formatDateInput);
        String dateByDefaultType = CommonMethod.convertLongToDate(longDate, Define.TYPE_DATE_TIME_FULL);
        //2017-03-28T21:00:00 to 2017-03-28T00:00:00
        //28/03/2017 to 2017-03-28T00:00:00
        String[] pivotXSetT = dateByDefaultType.split("T");
        String result = pivotXSetT[0] + "T" + "00:00:00";
        return result;
    }

    public static String convertDateToDate(String date, String formatDateFirst, String formatDateResult) {
        long longDate = CommonMethod.convertDateToLong(date, formatDateFirst);
        String dateByDefaultType = CommonMethod.convertLongToDate(longDate, formatDateResult);
        return dateByDefaultType;
    }

    public static Bitmap convertByte64ToBitmap(String byteStringAvatar) {
        if (byteStringAvatar == null || byteStringAvatar.isEmpty())
            return null;
        byte[] decodedString = Base64.decode(byteStringAvatar, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }

    public static void saveBitmap(String imageName, Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory() + Define.PROGRAM_PHOTOS_PATH;
        File programDirectory = new File(root);
        if (!programDirectory.exists()) {
            programDirectory.mkdirs();
        }

        File file = new File(root, imageName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getAvatar(String pathAvatar) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pathAvatar, options);
        return bitmap;
    }

    public static long[] merge2SortedAndRemoveDublicates(long[] a, long[] b) {
        long[] answer = new long[a.length + b.length];
        int i = 0, j = 0, k = 0;
        long tmp;
        while (i < a.length && j < b.length) {
            tmp = a[i] < b[j] ? a[i++] : b[j++];
            for (; i < a.length && a[i] == tmp; i++)
                ;
            for (; j < b.length && b[j] == tmp; j++)
                ;
            answer[k++] = tmp;
        }
        while (i < a.length) {
            tmp = a[i++];
            for (; i < a.length && a[i] == tmp; i++)
                ;
            answer[k++] = tmp;
        }
        while (j < b.length) {
            tmp = b[j++];
            for (; j < b.length && b[j] == tmp; j++)
                ;
            answer[k++] = tmp;
        }
        long[] result = Arrays.copyOf(answer, k);
        return result;
    }

    public static long[] merge2SortedBasicNotRemoveDuplicate(long[] a, long[] b) {

        long[] answer = new long[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length) {
            answer[k++] = a[i] < b[j] ? a[i++] : b[j++];
        }

        while (i < a.length)
            answer[k++] = a[i++];

        while (j < b.length)
            answer[k++] = b[j++];

        return answer;
    }

    public static String convertDateToPivotX(String pivotX) {
        if (pivotX == null)
            return "";
        //2017-03-28T21:00:00 to 21h00
        String[] pivotXSetT = pivotX.split("T");
        String[] pivotXSetHours = pivotXSetT[1].split(":");
        return pivotXSetHours[0] + "h" + pivotXSetHours[1];
    }

    public static String removeSpace(String valueData) {
        if (valueData == null)
            return null;
        return valueData.replaceAll(" ", "");
    }

}
