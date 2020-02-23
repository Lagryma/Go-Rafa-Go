package com.example.go_rafa_go_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_levels);

//        ImageView[] levels = {
//                findViewById(R.id.level1), // MAIN
//                findViewById(R.id.level2),
//                findViewById(R.id.level3),
//                findViewById(R.id.level4),
//                findViewById(R.id.level5), // CEA
//                findViewById(R.id.level6),
//                findViewById(R.id.level7),
//                findViewById(R.id.level8),
//                findViewById(R.id.level9), // PUREZA
//                findViewById(R.id.level10),
//                findViewById(R.id.level11),
//                findViewById(R.id.level12),
//                findViewById(R.id.level13), // LRT
//                findViewById(R.id.level14),
//                findViewById(R.id.level15),
//                findViewById(R.id.level16)
//        };
//
//        for (int i = 0;i < levels.length;i++) {
//            levels[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    switch(view.getId()) {
//                        case R.id.level1:
//                            Intent intent = new Intent(LevelsActivity.this, LoadingActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.anim_slideinleft, R.anim.anim_slideoutleft);
//                            break;
//                    }
//                }
//            });
//        }

        final ImageView goBack = findViewById(R.id.imageView5);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slideinright, R.anim.anim_slideoutright);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LevelsActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slideinright, R.anim.anim_slideoutright);
    }
}
