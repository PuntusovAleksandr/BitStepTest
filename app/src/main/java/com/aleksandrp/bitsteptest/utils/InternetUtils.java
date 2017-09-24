package com.aleksandrp.bitsteptest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aleksandrp.bitsteptest.App;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class InternetUtils {

    /**
     * Function check internet connection
     *
     * @return true if connection available,else if not.
     */
    public static boolean checkInternetConnection() {
        Context context = App.getContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }
}
