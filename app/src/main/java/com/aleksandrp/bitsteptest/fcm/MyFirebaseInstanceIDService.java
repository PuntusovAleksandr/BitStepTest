package com.aleksandrp.bitsteptest.fcm;

import android.util.Log;

import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "TAG_FCM";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        SettingsApp.getInstance().setTokenFCM((token));
    }

}
