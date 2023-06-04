package com.example.slagalica_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameOneActivity extends AppCompatActivity {
    private TextView timerText;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    private long startTime = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        timerText = findViewById(R.id.timer);

        startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        //seconds = seconds % 60;

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }
}

