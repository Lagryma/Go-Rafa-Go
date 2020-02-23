package com.example.go_rafa_go_game;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static java.lang.Integer.parseInt;

public class BackgroundMusicService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.main_bgm);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

        LocalBroadcastManager.getInstance(this).registerReceiver(StateReceiver, new IntentFilter("status"));
    }

    private BroadcastReceiver StateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            if (parseInt(String.valueOf(status)) == 0) {
                player.pause();  //When app is in background and not killed, we just want to pause the player and not want to lose the current state.
            } else if (parseInt(String.valueOf(status)) == 1) {
                if (player != null) {
                    player.start();  // If the player was created and wasn't stopped, it won't be null, and the playback will be resumed from where we left of.
                }
            } else if(player != null){
                player.stop();
                player.release();
            }

        }
    };

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {

    }
    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {

        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}
