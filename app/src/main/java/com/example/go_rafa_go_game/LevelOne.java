package com.example.go_rafa_go_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.system.Int64Ref;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class LevelOne extends Activity implements View.OnTouchListener {

    GestureDetector gestureDetector;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private long preTime = 0;
    private long currTime = 0;
    private int floor = 1;
    private int[] rnd_floor = {0, 1, 2};
    private int maxCoins = 10;
    private int currCoins = 10;
    private int currLevel = 1;

    private FrameLayout gameField;

    private float[][][] coin_spawnpoints;

    private ImageView[] floors;
    private ImageView coin1, preGame, rafa;
    private TextView levelCount, coinsLeft;

    private AnimationDrawable rafaWalk;

    // rafa variables
    float rafaX, rafaY;
    boolean rafaGoR = true, moved = false, rafaGoU = false, rafaGoD = false, isDamaged = false;
    private long dmgWindow = 3000;
    private long winOver = 0;
    private float limitY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_level_one);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Declare views
        gameField = findViewById(R.id.gameField);

        // Initialize floors array
        floors = new ImageView[] {
                findViewById(R.id.flr1), findViewById(R.id.flr2), findViewById(R.id.flr3)
        };

        coin1 = findViewById(R.id.coin_spawn1);
        preGame = findViewById(R.id.preGame);
        rafa = findViewById(R.id.rafa);
        coinsLeft = findViewById(R.id.coinsLeft);
        levelCount = findViewById(R.id.levelCount);

        AnimationDrawable coin_spin;
        coin1.setImageDrawable(getResources().getDrawable(R.drawable.coin_anim));
        coin_spin = (AnimationDrawable) coin1.getDrawable();
        coin_spin.start();

        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_anim));
        rafaWalk = (AnimationDrawable) rafa.getDrawable();
        rafaWalk.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (preTime <= 100) {
                            // Set coin spawn points
                            coin_spawnpoints = new float[][][] {
                                    {
                                            { (gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[0].getY() + (floors[0].getHeight() / 4.0f) },
                                            { (gameField.getWidth() / 2.0f) - (coin1.getWidth() / 2.0f), floors[0].getY() + (floors[0].getHeight() / 4.0f) },
                                            { (gameField.getWidth() - gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[0].getY() + (floors[0].getHeight() / 4.0f) }
                                    },
                                    {
                                            { (gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[1].getY() + (floors[1].getHeight() / 4.0f) },
                                            { (gameField.getWidth() / 2.0f) - (coin1.getWidth() / 2.0f), floors[1].getY() + (floors[1].getHeight() / 4.0f) },
                                            { (gameField.getWidth() - gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[1].getY() + (floors[1].getHeight() / 4.0f) }
                                    },
                                    {
                                            { (gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[2].getY() + (floors[2].getHeight() / 4.0f) },
                                            { (gameField.getWidth() / 2.0f) - (coin1.getWidth() / 2.0f), floors[2].getY() + (floors[2].getHeight() / 4.0f) },
                                            { (gameField.getWidth() - gameField.getWidth() / 6.0f) - (coin1.getWidth() / 2.0f), floors[2].getY() + (floors[2].getHeight() / 4.0f) }
                                    }
                            };

                            rafa.setX((gameField.getWidth() / 2) - (rafa.getWidth() / 2));

                            int n = genRandom(0, 2);
                            int temp;

                            for (int i = 0;i < rnd_floor.length;i++) {
                                if (n == rnd_floor[i]) {
                                    temp = rnd_floor[2];
                                    rnd_floor[2] = n;
                                    rnd_floor[i] = temp;
                                    break;
                                }
                            }

                            coinSpawn();

                            preTime += 20;
                        }
                        else if (preTime <= 3500){
                            if (preTime >= 0 && preTime <= 999) {
                                preGame.setImageResource(R.drawable.go_count);
                            }
                            else if (preTime >= 1000 && preTime <= 1999) {
                                preGame.setImageResource(R.drawable.rafa_count);
                            }
                            else if (preTime >= 2000 && preTime <= 2999) {
                                preGame.setImageResource(R.drawable.go_count);
                            }
                            else if (preTime >= 3000 && preTime <= 3499) {
                                preGame.setImageResource(R.drawable.empty_count);
                            }
                            preTime += 20;
                        }
                        else {
                            changePos();

                            if (isDamaged) {
                                if (currTime > winOver) {
                                    isDamaged = false;

                                    if (rafaGoR) {
                                        rafaWalk.stop();
                                        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_anim));
                                        rafaWalk = (AnimationDrawable) rafa.getDrawable();
                                        rafaWalk.start();
                                    }
                                    else {
                                        rafaWalk.stop();
                                        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_rev));
                                        rafaWalk = (AnimationDrawable) rafa.getDrawable();
                                        rafaWalk.start();
                                    }
                                }
                                if (rafaGoR && moved) {
                                    rafaWalk.stop();
                                    rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_dmged_anim));
                                    rafaWalk = (AnimationDrawable) rafa.getDrawable();
                                    rafaWalk.start();
                                    moved = false;
                                } else if (moved) {
                                    rafaWalk.stop();
                                    rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_dmged_rev));
                                    rafaWalk = (AnimationDrawable) rafa.getDrawable();
                                    rafaWalk.start();
                                    moved = false;
                                }
                            }

                            currTime += 20;
                        }
                    }
                });
            }
        }, 0, 20);

        gestureDetector = new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (!rafaGoU && !rafaGoD) {
                    if (direction == Direction.up) {
//                    Toast.makeText(getApplicationContext(), "Up", Toast.LENGTH_SHORT).show();
                        if (floor < 3 && !rafaGoD) {
                            rafaGoU = true;
                            limitY = rafa.getY() - (floors[0].getY() - floors[1].getY());
                            floor++;
                        }
                    }

                    if (direction == Direction.down) {
//                    Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_SHORT).show();
                        if (floor > 1 && !rafaGoU) {
                            rafaGoD = true;
                            limitY = rafa.getY() + (floors[0].getY() - floors[1].getY());
                            floor--;
                        }
                    }
                }

                if (direction == Direction.right) {
//                    Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT).show();
                    if (!isDamaged) {
                        rafaWalk.stop();
                        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_anim));
                        rafaWalk = (AnimationDrawable) rafa.getDrawable();
                        rafaWalk.start();
                    }
                    moved = true;
                    rafaGoR = true;
                }

                if (direction == Direction.left) {
//                    Toast.makeText(getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
                    if (!isDamaged) {
                        rafaWalk.stop();
                        rafa.setImageDrawable(getResources().getDrawable(R.drawable.char_classic_walk_rev));
                        rafaWalk = (AnimationDrawable) rafa.getDrawable();
                        rafaWalk.start();
                    }
                    moved = true;
                    rafaGoR = false;
                }
                return true;
            }
        });

        gameField.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void changePos() {
        rafaX = rafa.getX();
        rafaY = rafa.getY();

        if (rafaGoU || rafaGoD) {
            if (rafaGoU) {
                rafaY -= 20;
                if (rafaY <= limitY) {
                    rafaGoU = false;
                    rafaY = limitY;
                }
            }
            else {
                rafaY += 20;
                if (rafaY >= limitY) {
                    rafaGoD = false;
                    rafaY = limitY;
                }
            }
        }
        else {
            if (rafaGoR) rafaX += 10;
            else rafaX -= 10;
        }

        if (rafaX > gameField.getWidth() - rafa.getWidth()) {
            rafaX = gameField.getWidth() - rafa.getWidth();
        }

        if (rafaX < 0) {
            rafaX = 0;
        }

        rafa.setX(rafaX);
        rafa.setY(rafaY);

        // Check collision
        float coinX = coin1.getX();
        float coinY = coin1.getY();
        float xAllowance = 12.0f;

        if (rafaX + rafa.getWidth() - xAllowance >= coinX + xAllowance && rafaX + xAllowance <= coinX + coin1.getWidth() - xAllowance && rafaY + rafa.getHeight() >= coinY && rafaY <= coinY + coin1.getHeight()) {
            coinSpawn();
            currCoins--;

            if (currCoins == 0) {
                maxCoins += 5;
                currLevel += 1;
                currCoins = maxCoins;
                coinsLeft.setText("" + currCoins);
                levelCount.setText("" + currLevel);
            }
            else {
                coinsLeft.setText("" + currCoins);
            }
        }
    }

    public void coinSpawn() {
        int n = genRandom(0, 1);
        int temp;
        int x = genRandom(0, 2);

        coin1.setX(coin_spawnpoints[rnd_floor[n]][x][0]);
        coin1.setY(coin_spawnpoints[rnd_floor[n]][x][1]);

        for (int i = 0;i < 2;i++) {
            if (n == rnd_floor[i]) {
                temp = rnd_floor[2];
                rnd_floor[2] = n;
                rnd_floor[i] = temp;
                break;
            }
        }
    }

    public int genRandom(int min, int max) {
        max++;
        Random r = new Random();
        return r.nextInt(max - min) + min; // 0-2, 0-1-2, 0-2
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LevelOne.this, PauseActivity.class);
        startActivity(intent);
    }
}
