package com.aleksandrp.bitsteptest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = App.class.getSimpleName();

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = this.getApplicationContext();
        registerActivityLifecycleCallbacks(this);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onActivityCreated(Activity mActivity, Bundle mBundle) {
    }

    @Override
    public void onActivityStarted(Activity mActivity) {

    }

    @Override
    public void onActivityResumed(Activity mActivity) {
    }

    @Override
    public void onActivityPaused(Activity mActivity) {
    }

    @Override
    public void onActivityStopped(Activity mActivity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity mActivity, Bundle mBundle) {
    }

    @Override
    public void onActivityDestroyed(Activity mActivity) {
    }
}
