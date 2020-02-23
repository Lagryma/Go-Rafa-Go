package com.example.go_rafa_go_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_loading);

        ImageView rafa = (ImageView) findViewById(R.id.imageView25);
        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_anim));
        AnimationDrawable rafaWalk = (AnimationDrawable) rafa.getDrawable();
        rafaWalk.start();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LevelOne.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                finish();
            }
        }, 3000);
    }
}
