package android.stakbrowser.amitrai.andoridlibraryproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by amitrai on 5/1/16.
 */
public class LibraryLogger {

    // Tag to be specified for logging
    private static final String TAG = LibraryLogger.class.getSimpleName();

    // Flag to enable of disable logger.
    public static boolean ENABLE_LOGGER = false;

    /**
     * this methods enables or disables the logging by the library
     * @param error
     */
    public static void log(String error, Context context){
        if (ENABLE_LOGGER)
            Log.e(TAG, error);

        if (hasInternet(context)){
            // todo log this error to anylytics
        }
    }

    private static boolean hasInternet(Context context)
    {
        String permission = "android.permission.INTERNET";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
