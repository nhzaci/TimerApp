package com.zack.timerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    SeekBar setTime;
    Button startTimer;
    TextView showTimeLeft;
    Button stopTimer;
    CountDownTimer countDown;
    int lastSetTime;

    public void resetTime() {
        setTime.setProgress(lastSetTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(lastSetTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(lastSetTime) - minutes * 60;
        showTimeLeft.setText(String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds));
        startTimer.setAlpha(1.0f);
        stopTimer.setAlpha(0.0f);
        setTime.setEnabled(true);
        startTimer.setEnabled(true);
        stopTimer.setEnabled(false);
    }

    public void startTimer() {
        countDown = new CountDownTimer(setTime.getProgress(), 1000) {
            @Override
            public void onTick(long millisecondsUntilDone) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsUntilDone) - minutes * 60;
                showTimeLeft.setText(String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds));
                setTime.setProgress((int) millisecondsUntilDone);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Countdown ended!", Toast.LENGTH_SHORT).show();
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.keyboard);
                mediaPlayer.start();
                resetTime();
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTime = findViewById(R.id.seekBar);
        startTimer = findViewById(R.id.timerStartButton);
        stopTimer = findViewById(R.id.timerStopButton);
        showTimeLeft = findViewById(R.id.timeLeftText);

        stopTimer.setEnabled(false); //can't click pause button

        setTime.setMin(1000); //min time is 1 second
        setTime.setMax(3600000); //max time is 1 hr
        setTime.setProgress(60000); //start at 1 minute

        setTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Reflect time set onto the TextView
                long minutes = TimeUnit.MILLISECONDS.toMinutes(progress);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(progress) - minutes * 60;
                showTimeLeft.setText(String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer.setAlpha(0.0f);
                stopTimer.setAlpha(1.0f);
                lastSetTime = setTime.getProgress();
                MainActivity.this.startTimer(); //Run start timer method
                setTime.setEnabled(false);
                startTimer.setEnabled(false);
                stopTimer.setEnabled(true);
            }
        });

        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer.setAlpha(1.0f);
                stopTimer.setAlpha(0.0f);
                setTime.setEnabled(true);
                startTimer.setEnabled(true);
                stopTimer.setEnabled(false);
                MainActivity.this.countDown.cancel();
            }
        });
    }
}
