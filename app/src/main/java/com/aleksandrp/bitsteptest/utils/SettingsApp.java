package com.aleksandrp.bitsteptest.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aleksandrp.bitsteptest.App;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class SettingsApp {

    private static final String TAG = SettingsApp.class.getSimpleName();

    /**
     * Instance of SharedPreferences object
     */
    private SharedPreferences sPref;
    /**
     * Editor of SharedPreferences object
     */
    private SharedPreferences.Editor editor;

    private static SettingsApp ourInstance = new SettingsApp();

    /**
     * The constant FILE_NAME.
     */
// Settings xml file name
    public static final String FILE_NAME = "settings";

    /**
     * get instance settingsApp
     *
     * @return
     */
    public static SettingsApp getInstance() {
        return ourInstance;
    }


    /**
     * Construct the instance of the object
     */
    public SettingsApp() {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        editor = sPref.edit();
    }


    // Keys for opening settings from xml file
    private static final String KEY_LOGIN = "KEY_LOGIN";
    private static final String KEY_TOKEN_SERVER= "KEY_TOKEN_SERVER";
    private static final String KEY_EMAIL= "KEY_EMAIL";
    private static final String KEY_PASS= "KEY_PASS";
    private static final String KEY_TOKEN_FCM = "KEY_TOKEN_FCM";
    private static final String KEY_PATH_FOLE = "KEY_PATH_FOLE";


    // Default values of settings
    private static final String DEF_EMPTY_STRING = "";
    private static final String DEF_LANGUAGE = "en";
    private static final boolean DEF_EMPTY_BOOLEAN = false;
    private static final boolean DEF_NOT_EMPTY_BOOLEAN = true;
    private static final int DEF_INT_EMPTY = 1;


    /**
     * CHECK isLogin
     *
     * @return
     */
    public boolean isLogin() {
        Log.d(TAG, "isLogin");
        return sPref.getBoolean(KEY_LOGIN, DEF_EMPTY_BOOLEAN);
    }

    /**
     * SET setLogin
     *
     * @param ip
     */
    public void setLogin(boolean ip) {
        Log.d(TAG, "setLogin");
        editor.putBoolean(KEY_LOGIN, ip).commit();
    }

    /**
     * GET Token server
     *
     * @return
     */
    public String getTokenServer() {
        Log.d(TAG, "getTokenServer");
        return sPref.getString(KEY_TOKEN_SERVER, DEF_EMPTY_STRING);
    }

    /**
     * SET TokenServer
     *
     * @param ip
     */
    public void setTokenServer(String ip) {
        Log.d(TAG, "setTokenServer");
        editor.putString(KEY_TOKEN_SERVER, ip).commit();
    }

    /**
     * GET email
     *
     * @return
     */
    public String getEmail() {
        Log.d(TAG, "getEmail");
        return sPref.getString(KEY_EMAIL, DEF_EMPTY_STRING);
    }

    /**
     * SET email
     *
     * @param ip
     */
    public void setEmail(String ip) {
        Log.d(TAG, "setEmail");
        editor.putString(KEY_EMAIL, ip).commit();
    }

    /**
     * GET Pass
     *
     * @return
     */
    public String getPass() {
        Log.d(TAG, "getPass");
        return sPref.getString(KEY_PASS, DEF_EMPTY_STRING);
    }

    /**
     * SET Pass
     *
     * @param ip
     */
    public void setPass(String ip) {
        Log.d(TAG, "setPass");
        editor.putString(KEY_PASS, ip).commit();
    }



    public String getTokenFCM() {
        Log.d(TAG, "getTokenFCM");
        return sPref.getString(KEY_TOKEN_FCM, "");
    }


    public void setTokenFCM(String ip) {
        Log.d(TAG, "setTokenFCM " + ip);
        editor.putString(KEY_TOKEN_FCM, ip).commit();
    }


    public String getPathIcon() {
        Log.d(TAG, "getPathIcon");
        return sPref.getString(KEY_PATH_FOLE, "");
    }


    public void savePathIcon(String ip) {
        Log.d(TAG, "savePathIcon " + ip);
        editor.putString(KEY_PATH_FOLE, ip).commit();
    }

}
