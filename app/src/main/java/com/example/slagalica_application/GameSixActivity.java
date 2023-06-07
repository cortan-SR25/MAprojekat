package com.example.slagalica_application;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class GameSixActivity extends AppCompatActivity {
    private EditText finalResult;
    private EditText aResult;
    private EditText bResult;
    private EditText cResult;
    private EditText dResult;

    private TextView player1Points;
    private TextView timerText;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long startTime = 60000;

    private Button btnConfirm;

    private Button a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4, d1, d2, d3, d4;

    private ArrayList<String> aColumnWords;
    private ArrayList<TextView> aFields;

    private ArrayList<String> bColumnWords;
    private ArrayList<TextView> bFields;

    private ArrayList<String> cColumnWords;
    private ArrayList<TextView> cFields;

    private ArrayList<String> dColumnWords;
    private ArrayList<TextView> dFields;

    private boolean isASolved, isBSolved, isCSolved, isDSolved = false;

    private int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_six);

        btnConfirm = findViewById(R.id.btnConfirm);

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);

        finalResult = findViewById(R.id.finalResult);
        aResult = findViewById(R.id.aResult);
        bResult = findViewById(R.id.bResult);
        cResult = findViewById(R.id.cResult);
        dResult = findViewById(R.id.dResult);

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);

        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);
        d4 = findViewById(R.id.d4);

        //dodavanje polja kolone A
        aFields = new ArrayList<TextView>();
        aFields.add(a1);
        aFields.add(a2);
        aFields.add(a3);
        aFields.add(a4);

        //dodavanje reci/pojmova u a kolonu
        aColumnWords = new ArrayList<String>();
        aColumnWords.add("VATRA");
        aColumnWords.add("CIGARETA");
        aColumnWords.add("GUST");
        aColumnWords.add("SIGNAL");

        //dodavanje polja kolone B
        bFields = new ArrayList<TextView>();
        bFields.add(b1);
        bFields.add(b2);
        bFields.add(b3);
        bFields.add(b4);

        //dodavanje reci/pojmova u B kolonu
        bColumnWords = new ArrayList<String>();
        bColumnWords.add("MASKA");
        bColumnWords.add("STARO");
        bColumnWords.add("METAL");
        bColumnWords.add("PUK");

        //dodavanje polja kolone C
        cFields = new ArrayList<TextView>();
        cFields.add(c1);
        cFields.add(c2);
        cFields.add(c3);
        cFields.add(c4);

        //dodavanje reci/pojmova u C kolonu
        cColumnWords = new ArrayList<String>();
        cColumnWords.add("DJAKUZI");
        cColumnWords.add("KUPKA");
        cColumnWords.add("MALTER");
        cColumnWords.add("KORITO");

        //dodavanje polja kolone D
        dFields = new ArrayList<TextView>();
        dFields.add(d1);
        dFields.add(d2);
        dFields.add(d3);
        dFields.add(d4);

        //dodavanje reci/pojmova u D kolonu
        dColumnWords = new ArrayList<String>();
        dColumnWords.add("SNIZAVANJE");
        dColumnWords.add("PRITISAK");
        dColumnWords.add("RAMPA");
        dColumnWords.add("TOBOGAN");

        btnA1();
        btnA2();
        btnA3();
        btnA4();
        btnB1();
        btnB2();
        btnB3();
        btnB4();
        btnC1();
        btnC2();
        btnC3();
        btnC4();
        btnD1();
        btnD2();
        btnD3();
        btnD4();

        Confirm();

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
                timerText.setText("00");
                isTimerRunning = false;
                showTimerEndDialog();
            }
        };

        countDownTimer.start();
    }
    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }



    private void Confirm(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalResult.getText().toString().trim().equals("Zavesa")){
                    Toast.makeText(GameSixActivity.this, "CORRECT ANSWER", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < aColumnWords.size() && i < bColumnWords.size() && i < cColumnWords.size() && i < dColumnWords.size(); i++) {
                        aFields.get(i).setText(aColumnWords.get(i));
                        bFields.get(i).setText(bColumnWords.get(i));
                        cFields.get(i).setText(cColumnWords.get(i));
                        dFields.get(i).setText(dColumnWords.get(i));

                        aResult.setText("Dim");
                        bResult.setText("Gvozdje");
                        cResult.setText("Kada");
                        dResult.setText("Spustanje");

                        a1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        aResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

                        b1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        bResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

                        c1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        cResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

                        d1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        dResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

                        finalResult.setText("Zavesa");
                        finalResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

                        aResult.setFocusable(false);
                        bResult.setFocusable(false);
                        cResult.setFocusable(false);
                        dResult.setFocusable(false);
                        finalResult.setFocusable(false);

                        countDownTimer.cancel();
                    }
                    points += 20;
                    player1Points.setText(points + " points");

                }else if(aResult.getText().toString().trim().equals("Dim") && !isASolved){
                    for (int i = 0; i < aColumnWords.size() ; i++) {
                        aFields.get(i).setText(aColumnWords.get(i));
                        aResult.setText("Dim");

                        a1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        a4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        aResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        aResult.setFocusable(false);

                        isASolved = true;
                    }
                    points += 5;
                    player1Points.setText(points + " points");

                }else if(bResult.getText().toString().trim().equals("Gvozdje") && !isBSolved){
                    for (int i = 0; i < bColumnWords.size() ; i++) {
                        bFields.get(i).setText(bColumnWords.get(i));
                        bResult.setText("Gvozdje");

                        b1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        b4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        bResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        bResult.setFocusable(false);

                        isBSolved = true;
                    }
                    points += 5;
                    player1Points.setText(points + " points");

                }else if(cResult.getText().toString().trim().equals("Kada") && !isCSolved){
                    for (int i = 0; i < cColumnWords.size() ; i++) {
                        cFields.get(i).setText(cColumnWords.get(i));
                        cResult.setText("Kada");

                        c1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        c4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        cResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        cResult.setFocusable(false);

                        isCSolved = true;
                    }
                    points += 5;
                    player1Points.setText(points + " points");

                }else if(dResult.getText().toString().trim().equals("Spustanje") && !isDSolved){
                    for (int i = 0; i < dColumnWords.size() ; i++) {
                        dFields.get(i).setText(dColumnWords.get(i));
                        dResult.setText("Spustanje");

                        d1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        d4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        dResult.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
                        dResult.setFocusable(false);

                        isDSolved = true;
                    }
                    points += 5;
                    player1Points.setText(points + " points");

                }
                else{
                    Toast.makeText(GameSixActivity.this, "WRONG ANSWER", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void btnA1(){
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i == 0; i++) {
                    aFields.get(i).setText(aColumnWords.get(i));
                }
            }
        });
    }

    private void btnA2(){
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i == 1; i++) {
                    aFields.get(i).setText(aColumnWords.get(i));
                }
            }
        });
    }

    private void btnA3(){
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 2; i == 2; i++) {
                    aFields.get(i).setText(aColumnWords.get(i));
                }
            }
        });
    }

    private void btnA4(){
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 3; i == 3; i++) {
                    aFields.get(i).setText(aColumnWords.get(i));
                }
            }
        });
    }

    private void btnB1(){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i == 0; i++) {
                    bFields.get(i).setText(bColumnWords.get(i));
                }
            }
        });
    }

    private void btnB2(){
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i == 1; i++) {
                    bFields.get(i).setText(bColumnWords.get(i));
                }
            }
        });
    }

    private void btnB3(){
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 2; i == 2; i++) {
                    bFields.get(i).setText(bColumnWords.get(i));
                }
            }
        });
    }

    private void btnB4(){
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 3; i == 3; i++) {
                    bFields.get(i).setText(bColumnWords.get(i));
                }
            }
        });
    }

    private void btnC1(){
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i == 0; i++) {
                    cFields.get(i).setText(cColumnWords.get(i));
                }
            }
        });
    }

    private void btnC2(){
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i == 1; i++) {
                    cFields.get(i).setText(cColumnWords.get(i));
                }
            }
        });
    }

    private void btnC3(){
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 2; i == 2; i++) {
                    cFields.get(i).setText(cColumnWords.get(i));
                }
            }
        });
    }

    private void btnC4(){
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 3; i == 3; i++) {
                    cFields.get(i).setText(cColumnWords.get(i));
                }
            }
        });
    }

    private void btnD1(){
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i == 0; i++) {
                    dFields.get(i).setText(dColumnWords.get(i));
                }
            }
        });
    }

    private void btnD2(){
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i == 1; i++) {
                    dFields.get(i).setText(dColumnWords.get(i));
                }
            }
        });
    }

    private void btnD3(){
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 2; i == 2; i++) {
                    dFields.get(i).setText(dColumnWords.get(i));
                }
            }
        });
    }

    private void btnD4(){
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 3; i == 3; i++) {
                    dFields.get(i).setText(dColumnWords.get(i));
                }
            }
        });
    }

    private void showTimerEndDialog(){
        ConstraintLayout timerEndConstraintLayout = findViewById(R.id.timerEndConstraintLayout);
        View view = LayoutInflater.from(GameSixActivity.this).inflate(R.layout.timer_end_dialog, timerEndConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameSixActivity.this);
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



}