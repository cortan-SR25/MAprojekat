package com.example.slagalica_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameThreeActivity extends AppCompatActivity {

    private String mouse = "🐹";
    private String square = "🟪";
    private String circle = "🟣";
    private String heart = "💜";
    private String triangle = "📐";
    private String star = "⭐";

    private int numberOfTries = 6;
    private int numberOfClicks = 0;

    private String currentLetter = "a";

    private TextView mouseButton;
    private TextView squareButton;
    private TextView circleButton;
    private TextView heartButton;
    private TextView triangleButton;
    private TextView starButton;

    private Button okButton;
    private Button deleteButton;

    private TextView timerText;
    private TextView player1Points;

    private ArrayList<TextView> rows = new ArrayList<>();
    private ArrayList<TextView> symbols;

    private boolean isCorrect = false;

    private CountDownTimer countDownTimer;

    HashMap<Integer, String> correctCombo = new HashMap<>();

    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);

        correctCombo.put(1, heart);
        correctCombo.put(2, heart);
        correctCombo.put(3, circle);
        correctCombo.put(4, star);

        mouseButton = findViewById(R.id.skocko);
        squareButton = findViewById(R.id.kvadrat);
        circleButton = findViewById(R.id.krug);
        heartButton = findViewById(R.id.srce);
        triangleButton = findViewById(R.id.trougao);
        starButton = findViewById(R.id.zvezda);

        okButton = findViewById(R.id.ok_button);
        deleteButton = findViewById(R.id.delete_button);

        symbols = new ArrayList<>();

        symbols.add(mouseButton);
        symbols.add(squareButton);
        symbols.add(circleButton);
        symbols.add(heartButton);
        symbols.add(triangleButton);
        symbols.add(starButton);

        setListeners();
        setSymbolListeners();

        startTimer(30000);
    }

    private void setListeners(){

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < symbols.size(); i++) {
                    symbols.get(i).setEnabled(true);
                }

                String letter = "a";

                if (numberOfTries == 5){
                    letter = "b";
                } else if (numberOfTries == 4){
                    letter = "c";
                } else if (numberOfTries == 3){
                    letter = "d";
                } else if (numberOfTries == 2){
                    letter = "e";
                } else if (numberOfTries == 1){
                    letter = "f";
                }

                Resources res = getResources();
                int id = res.getIdentifier(letter + "1", "id", getPackageName());
                TextView textView1 = findViewById(id);
                id = res.getIdentifier(letter + "2", "id", getPackageName());
                TextView textView2 = findViewById(id);
                id = res.getIdentifier(letter + "3", "id", getPackageName());
                TextView textView3 = findViewById(id);
                id = res.getIdentifier(letter + "4", "id", getPackageName());
                TextView textView4 = findViewById(id);

                textView1.setText("");
                textView2.setText("");
                textView3.setText("");
                textView4.setText("");

                numberOfClicks = 0;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numberOfClicks == 4) {

                    checkCombination();

                    numberOfTries = numberOfTries - 1;

                    if (isCorrect || numberOfTries == 0) {
                        showCorrectCombination();
                        for (int i = 0; i < symbols.size(); i++) {
                            symbols.get(i).setEnabled(false);
                        }
                        countDownTimer.cancel();
                        restartTimer();
                        finishGame();
                    }
                    changeLetter();
                    numberOfClicks = 0;
                }
            }
        });
    }

    private void setSymbolListeners(){
        mouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4) {
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(mouse);
                }
            }
        });

        squareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4) {
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(square);
                }
            }
        });

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4) {
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(circle);
                }
            }
        });

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4){
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(heart);
                }
            }
        });

        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4) {
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(triangle);
                }
            }
        });

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks < 4) {
                    numberOfClicks = numberOfClicks + 1;
                    Resources res = getResources();
                    int id = res.getIdentifier(currentLetter + numberOfClicks, "id", getPackageName());
                    TextView textView = findViewById(id);
                    textView.setText(star);
                }
            }
        });
    }

    private void showCorrectCombination(){
        int counter = 5;
        Resources res = getResources();
        for (int i = 1; i < 5; i++){
            int id = res.getIdentifier("h" + i, "id", getPackageName());
            TextView textView = findViewById(id);
            String val = correctCombo.get(i);
            textView.setText(val);
            int id1 = res.getIdentifier("h" + counter, "id", getPackageName());
            TextView textView1 = findViewById(id1);
            textView1.setBackground(getDrawable(R.drawable.correct_circle));
            counter = counter + 1;
        }

    }

    private void checkCombination(){
        ArrayList<String> colors = new ArrayList<>();
        ArrayList<String> purples = new ArrayList<>();
        ArrayList<String> darkPurples = new ArrayList<>();
        ArrayList<String> wrongPlaced = new ArrayList<>();
        int counter = 0;

        Resources res = getResources();
        HashMap<Integer, String> comboToCheck = new HashMap<>();
        comboToCheck.putAll(correctCombo);

        for (int i = 1; i < 5; i++){
            int id = res.getIdentifier(currentLetter + i, "id", getPackageName());
            TextView textView = findViewById(id);
            String val = textView.getText().toString();

            if (comboToCheck.get(i).equals(val)) {
                darkPurples.add("dark purple");
                counter = counter + 1;
                comboToCheck.remove(i);
            } else if (comboToCheck.containsValue(val)) {
                wrongPlaced.add(val);
            }
        }

        for (int i = 0; i < wrongPlaced.size(); i++){

            if (comboToCheck.containsValue(wrongPlaced.get(i))) {
                purples.add("purple");
            }

            for(Map.Entry<Integer, String> entry: comboToCheck.entrySet()) {
                if(entry.getValue() == wrongPlaced.get(i)) {
                    comboToCheck.remove(entry.getKey());
                    break;
                }
            }
        }

        colors.addAll(darkPurples);
        colors.addAll(purples);

        int circleCounter = 5;

        for (int i = 0; i < colors.size(); i++){
            int id = res.getIdentifier(currentLetter + circleCounter, "id", getPackageName());
            TextView textView = findViewById(id);
            circleCounter = circleCounter + 1;

            if (colors.get(i).equals("dark purple")){
                textView.setBackground(getDrawable(R.drawable.correct_circle));
            } else {
                textView.setBackground(getDrawable(R.drawable.partially_correct_circle));
            }
        }

        if (counter == 4){
            isCorrect = true;
        }
    }

    private void changeLetter(){
        if (numberOfTries == 5){
            currentLetter = "b";
        } else if (numberOfTries == 4){
            currentLetter = "c";
        } else if (numberOfTries == 3){
            currentLetter = "d";
        } else if (numberOfTries == 2){
            currentLetter = "e";
        } else if (numberOfTries == 1){
            currentLetter = "f";
        }
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
                finishGame();
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

    private void showTimerEndDialog(){
        ConstraintLayout timerEndConstraintLayout = findViewById(R.id.timerEndConstraintLayout);
        View view = LayoutInflater.from(GameThreeActivity.this).inflate(R.layout.timer_end_dialog, timerEndConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameThreeActivity.this);
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

    private void finishGame(){
        if ((numberOfTries == 5 || numberOfTries == 4) && isCorrect == true){
            Toast.makeText(this, "20 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("20 points");
        } else if ((numberOfTries == 3 || numberOfTries == 2) && isCorrect == true){
            Toast.makeText(this, "15 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("15 points");
        } else if ((numberOfTries == 1 || numberOfTries == 0) && isCorrect == true){
            Toast.makeText(this, "10 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("10 points");
        } else {
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("0 points");
        }

        for (int i = 0; i < symbols.size(); i++) {
            symbols.get(i).setEnabled(false);
        }
        okButton.setEnabled(false);
        deleteButton.setEnabled(false);

        showCorrectCombination();
    }
}