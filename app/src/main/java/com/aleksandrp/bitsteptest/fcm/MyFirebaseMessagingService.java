package com.aleksandrp.bitsteptest.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.actovoty.RegisterActivity;
import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "TAG_FCM";

    private final int ID_SMALL_NOTIFICATION_ALL = 10;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private static Vibrator v;

    private static AudioManager mAudioManager;
    private static MediaPlayer mMediaPlayer;

    public static final String CLOSE = "CLOSE";
    public static final String CLOSE_BODY = "CLOSE_BODY";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        if user log out do not
        if (SettingsApp.getInstance().isLogin()) {
            return;
        }

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> dataMap = remoteMessage.getData();
        String text = dataMap.toString().toLowerCase();

        // Check if message contains a data payload.
        if (dataMap.size() > 0) {
            Log.i(TAG, "Message data payload: " + dataMap);
        }/*{body={"push_type":"profile_changes","task_owner_id":null,"suggestion_id":null,"task_id":null,"title":null,"message":"08-15-2017   05:52am"}}*/

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String body = dataMap.get("body");
        if (body==null) body = "";
        Log.i(TAG, "Message body body  " + body);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        notification(body);


    }

    /**
     * Method shw notification by incoming messages from GCM
     *
     * @param
     * @param mBody
     */
    protected void notification(String mBody) {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP, "bbbb");
        wl.acquire(1000);

        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(CLOSE_BODY, mBody);
        intent.putExtra(CLOSE, true);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + R.raw.blop_my_thumps);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getApplication().getString(R.string.app_name))
                .setContentTitle(mBody.isEmpty() ? getApplication().getString(R.string.app_name) : mBody)
                .setContentInfo(getApplication().getString(R.string.app_name))
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH);

            playSound(alarmSound);
            playVibro();

        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID_SMALL_NOTIFICATION_ALL, mBuilder.build());
        final PowerManager.WakeLock screenOn = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
        screenOn.acquire();
    }

    private void playSound(Uri ringtone) {
        Context context = App.getContext();

        mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

        try {
            mMediaPlayer.setDataSource(context, ringtone);
        } catch (IOException mE) {
            mE.printStackTrace();
        }

        try {
            mMediaPlayer.prepare();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
                }
            });
        }
    }


    public static void playVibro() {
        stopVibro();
        Context context = App.getContext();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
//            v.vibrate(1500);
// This example will cause the phone to vibrate "SOS" in Morse Code
// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
// There are pauses to separate dots/dashes, letters, and words
// The following numbers represent millisecond lengths
        int dot = 100;      // Length of a Morse Code "dot" in milliseconds
        int dash = 250;     // Length of a Morse Code "dash" in milliseconds
        int short_gap = 100;    // Length of Gap Between dots/dashes
        int medium_gap = 250;   // Length of Gap Between Letters
        int long_gap = 500;    // Length of Gap Between Words
        long[] pattern = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot,    // s
                medium_gap,
                dash, short_gap, dash, short_gap, dash, // o
                medium_gap,
                dot, short_gap, dot, short_gap, dot,    // s
                long_gap
        };
        v.vibrate(pattern, -1);
    }

    public static void stopVibro() {
        if (v != null)
            v.cancel();
    }
}
