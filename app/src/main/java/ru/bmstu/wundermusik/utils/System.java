package ru.bmstu.wundermusik.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by max on 18.04.16.
 */
public class System {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showMessage (View messageParentView, String message) {
        Snackbar snackbar = Snackbar
                .make(messageParentView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
