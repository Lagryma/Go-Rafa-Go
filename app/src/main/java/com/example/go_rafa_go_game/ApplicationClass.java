package com.example.go_rafa_go_game;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ApplicationClass extends Application implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;

    private static String currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity.getClass().getSimpleName();
    }
    public static String getCurrentActivity() {
        return currentActivity;
    }
    @Override
    public void onActivityStarted(Activity activity) {
    }
    @Override
    public void onActivityResumed(Activity activity) {
        send_status(1);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        send_status(0);
    }
    private void send_status(int status_counter) {
        Intent intent = new Intent("check");
        intent.putExtra("check", String.valueOf(status_counter));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public void onActivityStopped(Activity activity) {

    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }
    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
