package com.example.go_rafa_go_game;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import static java.lang.Integer.parseInt;

public class MainActivity extends Activity {

    public static boolean isMute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        SharedPreferences settings = getSharedPreferences("settings", 0);
        int sound = settings.getInt("sound", 1);

        setContentView(R.layout.activity_main);

//        ImageView rafa = (ImageView) findViewById(R.id.imageView25);
//        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_anim));
//        AnimationDrawable rafaWalk = (AnimationDrawable) rafa.getDrawable();
//        rafaWalk.start();

        findViewById(R.id.button).setBackgroundDrawable(getResources().getDrawable(R.drawable.state_start_btn));
        findViewById(R.id.button2).setBackgroundDrawable(getResources().getDrawable(R.drawable.state_help_btn));
        findViewById(R.id.button4).setBackgroundDrawable(getResources().getDrawable(R.drawable.state_quit_btn));

        final ImageView volumeCtrl = findViewById(R.id.imageView2);

        Intent svc = new Intent(this, BackgroundMusicService.class);
        startService(svc); //OR stopService(svc);

        if (sound == 1) {

        }
        else {
            send_status(0);
            isMute = !isMute;
            SharedPreferences.Editor mEditor = settings.edit();
            mEditor.putInt("sound", 0).commit();
            volumeCtrl.setImageResource(R.drawable.sound_off_button);
        }

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("settings", 0);
                isMute = !isMute;
                if (isMute) {
                    SharedPreferences.Editor mEditor = settings.edit();
                    mEditor.putInt("sound", 0).commit();
                    volumeCtrl.setImageResource(R.drawable.sound_off_button);
                    send_status(0);
                }
                else {
                    SharedPreferences.Editor mEditor = settings.edit();
                    mEditor.putInt("sound", 1).commit();
                    volumeCtrl.setImageResource(R.drawable.sound_on_button);
                    send_status(1);
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(CheckVolumeState, new IntentFilter("check"));

        final Button playGame = findViewById(R.id.button);

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slideinleft, R.anim.anim_slideoutleft);
            }
        });

        final Button quitGame = findViewById(R.id.button4);

        quitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, popupActivity.class);
                startActivity(intent);

            }
        });

        final Button help = findViewById(R.id.button2);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slideinleft, R.anim.anim_slideoutleft);
            }
        });
    }

    private void send_status(int status_counter) {
        Intent intent = new Intent("status");
        intent.putExtra("status", String.valueOf(status_counter));
        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, popupActivity.class);
        startActivity(intent);
    }

    private BroadcastReceiver CheckVolumeState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String check = intent.getStringExtra("check");
            if (!isMute) {
                if (parseInt(String.valueOf(check)) == 0) {
                    send_status(0);
                } else if (parseInt(String.valueOf(check)) == 1) {
                    send_status(1);
                }
            }
        }

        private void send_status(int status_counter) {
            Intent intent = new Intent("status");
            intent.putExtra("status", String.valueOf(status_counter));
            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
        }
    };
}