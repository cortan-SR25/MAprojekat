package com.example.slagalica_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameTwoActivity extends AppCompatActivity {
    private TextView timerText;
    private TextView player1Points;

    private TextView hint1, hint2, hint3, hint4, hint5, hint6, hint7;   // polja sa objasnjenjem pojma
    private EditText playerAnswer;

    private Button btnConfirm;

    private TextView rightAnswer;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long startTime = 10000;

//    //lista fraza
    private ArrayList<String> phrases;

    private ArrayList<TextView> hints;

    private int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);

        hint1 = findViewById(R.id.hint1);
        hint2 = findViewById(R.id.hint2);
        hint3 = findViewById(R.id.hint3);
        hint4 = findViewById(R.id.hint4);
        hint5 = findViewById(R.id.hint5);
        hint6 = findViewById(R.id.hint6);
        hint7 = findViewById(R.id.hint7);

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);
        playerAnswer = findViewById(R.id.playerAnswer);

        btnConfirm = findViewById(R.id.btnConfirm);

        hints = new ArrayList<TextView>();
        hints.add(hint1);
        hints.add(hint2);
        hints.add(hint3);
        hints.add(hint4);
        hints.add(hint5);
        hints.add(hint6);
        hints.add(hint7);

        //dodavanje fraza u listu
        phrases = new ArrayList<String>();
        phrases.add("IMA VEZE SA SAVOM I DUNAVOM");
        phrases.add("SVIH DEVET SLOVA OVE REČI JE RAZLIČITO");
        phrases.add("IMA VEZE SA ZDRAVLJEM");
        phrases.add("MOŽE SE ODNOSITI NA ŽIVOT");
        phrases.add("IMA SVOG AGENTA I SVOJU PREMIJU");
        phrases.add("ZA VOZILO JE OBAVEZNO");
        phrases.add("POSTOJI KASKO");
//        phrases.add("OSIGURANJE");      //TACAN ODGOVOR

        for (int i = 0; i < phrases.size(); i++) {
            hints.get(i).setText(phrases.get(i));
        }
        Bundle bundle = getIntent().getExtras();
        totalPoints = bundle.getInt("points");
        player1Points.setText(String.valueOf(totalPoints) + " points");

//        phrase = Phrases[Phrases.length];

        checkAnswer();
        startTimer(startTime);
    }


    private void startTimer(long time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint2.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer();
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void restartTimer(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint3.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer2();
            }
        };

        countDownTimer.start();
    }

    private void restartTimer2(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint4.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer3();
            }
        };

        countDownTimer.start();
    }

    private void restartTimer3(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint5.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer4();
            }
        };

        countDownTimer.start();
    }

    private void restartTimer4(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint6.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer5();
            }
        };

        countDownTimer.start();
    }

    private void restartTimer5(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                hint7.setVisibility(View.VISIBLE);
                timerText.setText("00");
                isTimerRunning = false;
                restartTimer6();
            }
        };

        countDownTimer.start();
    }

    private void restartTimer6(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;
                showTimerEndDialog();
                nextGame();
            }
        };

        countDownTimer.start();
    }







    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }



    private void checkAnswer(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerAnswer.getText().toString().trim().equals("Osiguranje")){
                    Toast.makeText(GameTwoActivity.this, "CORRECT ANSWER", Toast.LENGTH_SHORT).show();
                    totalPoints = totalPoints + 20;
                    player1Points.setText(totalPoints + " points");
                    isTimerRunning = false;
                    countDownTimer.cancel();
                    hint2.setVisibility(View.VISIBLE);
                    hint3.setVisibility(View.VISIBLE);
                    hint4.setVisibility(View.VISIBLE);
                    hint5.setVisibility(View.VISIBLE);
                    hint6.setVisibility(View.VISIBLE);
                    hint7.setVisibility(View.VISIBLE);
                    nextGame();
                } else{
                    Toast.makeText(GameTwoActivity.this, "WRONG ANSWER", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showTimerEndDialog(){
        ConstraintLayout timerEndConstraintLayout = findViewById(R.id.timerEndConstraintLayout);
        View view = LayoutInflater.from(GameTwoActivity.this).inflate(R.layout.timer_end_dialog, timerEndConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameTwoActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        alertDone.findViewById(R.id.alertDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    //otvara sledecu igru
    private void nextGame(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {

                Bundle bundle = new Bundle();
                bundle.putInt(
                        "points",
                        totalPoints
                );

                Intent intent = new Intent(GameTwoActivity.this, GameThreeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }


}