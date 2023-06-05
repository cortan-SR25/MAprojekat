package com.example.slagalica_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;

public class GameOneActivity extends AppCompatActivity {
    private TextView timerText;
    private TextView player1Points;
    private TextView player1Result;

    private TextView calculation;
    private Button result;

    private Button firstNumButton;
    private Button secondNumButton;
    private Button thirdNumButton;
    private Button fourthNumButton;
    private Button fifthNumButton;
    private Button sixthNumButton;

    private Button firstOpButton;
    private Button secondOpButton;
    private Button thirdOpButton;
    private Button fourthOpButton;
    private Button fifthOpButton;
    private Button sixthOpButton;

    private Button confirmButton;
    private Button deleteButton;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    private long startTime = 60000;

    private String procedure = "";
    private double player1ResultNum;

    private ArrayList<Button> buttons;
    private ArrayList<Integer> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);
        player1Result = findViewById(R.id.player1_result);
        calculation = findViewById(R.id.calculation);

        firstNumButton = findViewById(R.id.num1_button);
        secondNumButton = findViewById(R.id.num2_button);
        thirdNumButton = findViewById(R.id.num3_button);
        fourthNumButton = findViewById(R.id.num4_button);
        fifthNumButton = findViewById(R.id.num5_button);
        sixthNumButton = findViewById(R.id.num6_button);

        firstOpButton = findViewById(R.id.plus_button);
        secondOpButton = findViewById(R.id.minus_button);
        thirdOpButton = findViewById(R.id.multiply_button);
        fourthOpButton = findViewById(R.id.divide_button);
        fifthOpButton = findViewById(R.id.open_prth_button);
        sixthOpButton = findViewById(R.id.close_prth_button);

        confirmButton = findViewById(R.id.confirm_button);
        deleteButton = findViewById(R.id.delete_button);

        result = findViewById(R.id.result);

        buttons = new ArrayList<Button>();
        buttons.add(firstNumButton);
        buttons.add(secondNumButton);
        buttons.add(thirdNumButton);
        buttons.add(fourthNumButton);
        buttons.add(fifthNumButton);
        buttons.add(sixthNumButton);
        buttons.add(result);
        buttons.add(firstOpButton);
        buttons.add(secondOpButton);
        buttons.add(thirdOpButton);
        buttons.add(fourthOpButton);
        buttons.add(fifthOpButton);
        buttons.add(sixthOpButton);
        buttons.add(confirmButton);
        buttons.add(deleteButton);

        numbers = new ArrayList<Integer>();
        numbers.add(3);
        numbers.add(6);
        numbers.add(4);
        numbers.add(4);
        numbers.add(20);
        numbers.add(25);
        numbers.add(473);

        for (int i = 0; i < numbers.size(); i++){
            buttons.get(i).setText(numbers.get(i).toString());
        }

        setListeners();

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
                showTimerEndDialog();
                restartTimer();
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void restartTimer(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(6000, 1000) {
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
    }

    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }

    private void setListeners(){

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmProcedure();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < buttons.size(); i++){
                    buttons.get(i).setEnabled(true);
                }
                procedure = "";
                calculation.setText("");
            }
        });

        for (int i = 0; i < 13; i++){
            if (i == 6){
                continue;
            }
            int finalI = i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    procedure += " " + buttons.get(finalI).getText().toString();
                    calculation.setText(procedure);
                    if (finalI < 6) {
                        buttons.get(finalI).setEnabled(false);
                    }
                }
            });
        }
    }

    private void confirmProcedure(){
        for (int i = 0; i < buttons.size(); i++){
            buttons.get(i).setEnabled(false);
        }

        if (procedure.equals("")){
            procedure = "0";
        }

        Context context = Context.enter();
        context.setOptimizationLevel(-1);
        Scriptable scope = context.initStandardObjects();
        Object evalResult;
        try {
            evalResult = context.evaluateString(scope, procedure, "<cmd>", 1, null);
        } catch(Exception e){
            evalResult = "0.0";
        }

        player1ResultNum = Double.parseDouble(evalResult.toString());

        player1Result.setText(String.valueOf(player1ResultNum));

        double resultNum = Double.parseDouble(result.getText().toString());

        if (resultNum == player1ResultNum){
            Toast.makeText(this, "20 POINTS", Toast.LENGTH_LONG);
            player1Points.setText("20");
        } else {
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_LONG);
            player1Points.setText("0");
        }

        restartTimer();
    }

    private void showTimerEndDialog(){
        ConstraintLayout timerEndConstraintLayout = findViewById(R.id.timerEndConstraintLayout);
        View view = LayoutInflater.from(GameOneActivity.this).inflate(R.layout.timer_end_dialog, timerEndConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameOneActivity.this);
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

