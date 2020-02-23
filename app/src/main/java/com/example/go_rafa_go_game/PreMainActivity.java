package com.example.go_rafa_go_game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PreMainActivity extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_premain);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent = new Intent(PreMainActivity.this, MainActivity.class);
               startActivity(intent);
               overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
               finish();
           }
        }, 3000);

        Animation preMainAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        findViewById(R.id.premainsplash).startAnimation(preMainAnim);
    }
}
