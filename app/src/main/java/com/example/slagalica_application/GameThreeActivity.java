package com.example.slagalica_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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

    private ArrayList<String> stringSymbols;

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
    private TextView player2Points;


    private ArrayList<TextView> rows = new ArrayList<>();
    private ArrayList<TextView> symbols;

    private boolean isCorrect = false;

    private CountDownTimer countDownTimer;

    HashMap<Integer, String> correctCombo = new HashMap<>();

    private boolean isTimerRunning = false;

    private int totalPoints;

    private String id;
    private String opponentId;

    private ArrayList<String> colorsList;
    private String priority;
    private String p1PointsText;
    private String p2PointsText;

    private static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);

        id = PreferenceManager.getDefaultSharedPreferences(this).
                getString("ID", null);
        opponentId = PreferenceManager.getDefaultSharedPreferences(this).
                getString("OPPONENT_ID", null);

        priority = PreferenceManager.getDefaultSharedPreferences(this).
                getString("PRIORITY", null);

        p1PointsText = PreferenceManager.getDefaultSharedPreferences(this).
                getString("POINTS", null);

        p2PointsText = PreferenceManager.getDefaultSharedPreferences(this).
                getString("OPPONENT_POINTS", null);

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);
        player2Points = findViewById(R.id.playerTwo_points);

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

        stringSymbols = new ArrayList<>();
        stringSymbols.add(mouse);
        stringSymbols.add(square);
        stringSymbols.add(circle);
        stringSymbols.add(heart);
        stringSymbols.add(triangle);
        stringSymbols.add(star);

        Bundle bundle = getIntent().getExtras();
        totalPoints = bundle.getInt("points");

        player1Points.setText(p1PointsText + " points");
        player2Points.setText(p2PointsText + " points");

        if ((counter == 0 && priority.equals("1"))
        || counter == 1 && priority.equals("2")){
        setListeners();
        setSymbolListeners();
        }

        startTimer(30000);

        HomeFragment.socket.on("giveOpponentAChanceSkocko", args -> {

            JSONObject obj = (JSONObject) args[0];

            try {
                currentLetter = "g";
                if (obj.get("_opponentId").toString().equals(id)) {

                    showSocketData(obj);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //setListeners();
                            //setSymbolListeners();
                            showCorrectCombination();
                            restartTimerEnd(5000);
                            HomeFragment.socket.off("sendPlayerSkocko");
                            HomeFragment.socket.off("sendPlayerSkockoCorrect");
                            HomeFragment.socket.off("notifyOpponentSkocko");
                            finishGame();
                            //nextGame();
                        }
                    });

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });

        HomeFragment.socket.on("showPlayerSkockoCorrect", args -> {

            JSONObject obj = (JSONObject) args[0];
            HomeFragment.socket.off("sendPlayerSkockoCorrect");

            try {
                if (obj.get("_opponentId").toString().equals(id)) {

                    showSocketData(obj);

                    currentLetter = "h";

                    showSocketData(obj);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showCorrectCombination();
                            restartTimerEnd(5000);
                            HomeFragment.socket.off("sendPlayerSkocko");
                            HomeFragment.socket.off("notifyOpponentSkocko");
                            HomeFragment.socket.off("sendPlayerSkockoCorrect");
                            finishGame();
                            //nextGame();
                        }
                    });

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        HomeFragment.socket.on("showPlayerSkocko", args -> {

            JSONObject obj = (JSONObject) args[0];

            try {
                if (numberOfTries == 0) {

                    if (obj.get("_opponentId").toString().equals(id)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                restartTimer(10000);
                            }
                        });
                        showSocketData(obj);

                    } else if (obj.get("_id").toString().equals(id)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //setListeners();
                                //setSymbolListeners();
                                restartTimer(10000);
                            }
                        });
                    }
                } else if (numberOfTries > 0) {
                    if (obj.get("_opponentId").toString().equals(id)) {
                        //setListeners();
                        //setSymbolListeners();
                        showSocketData(obj);
                        changeLetter();

                        if (numberOfTries == 1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setListeners();
                                    setSymbolListeners();
                                }
                            });
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        HomeFragment.socket.on("giveOpponentAChanceSkocko", args -> {

            JSONObject obj = (JSONObject) args[0];

            try {
                if (obj.get("_opponentId").toString().equals(id)) {

                    showSocketData(obj);

                    currentLetter = "h";

                    showSocketData(obj);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            restartTimerEnd(5000);
                            finishGame();
                            showCorrectCombination();
                            //nextGame();
                        }
                    });

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        HomeFragment.socket.on("opponentNotifiedSkocko", args -> {

            String opponent = (String) args[0];

            if (opponent.equals(id)) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        restartTimer(10000);
                    }
                });
            }
        });
    }

    private void setListeners() {

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < symbols.size(); i++) {
                    symbols.get(i).setEnabled(true);
                }

                String letter = "a";

                if (numberOfTries == 5) {
                    letter = "b";
                } else if (numberOfTries == 4) {
                    letter = "c";
                } else if (numberOfTries == 3) {
                    letter = "d";
                } else if (numberOfTries == 2) {
                    letter = "e";
                } else if (numberOfTries == 1) {
                    letter = "f";
                } else {
                    letter = "g";
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

                    if (numberOfTries == -1) {
                        sendPlayerChanceSkocko();
                        showCorrectCombination();
                        HomeFragment.socket.off("sendPlayerSkocko");
                        HomeFragment.socket.off("notifyOpponentSkocko");
                        HomeFragment.socket.off("sendPlayerSkockoCorrect");
                        restartTimerEnd(5000);
                        finishGame();
                        //nextGame();
                    } else {
                        if (isCorrect) {
                            //showCorrectCombination();
                            sendPlayerCorrectSkocko();
                            for (int i = 0; i < symbols.size(); i++) {
                                symbols.get(i).setEnabled(false);
                            }
                            countDownTimer.cancel();
                            restartTimerEnd(5000);
                            finishGame();
                            showCorrectCombination();
                            //nextGame();
                        } else {

                            if (numberOfTries == 0) {
                                // Toast.makeText(getApplicationContext(), "HERE2", Toast.LENGTH_SHORT).show();
                                sendPlayerSkocko();
                                changeLetter();
                                countDownTimer.cancel();
                                restartTimer(10000);
                                HomeFragment.socket.emit("notifyOpponentSkocko", opponentId);
                            } else if (numberOfTries > 0) {
                                //Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_SHORT).show();
                                sendPlayerSkocko();
                                changeLetter();
                                numberOfClicks = 0;
                            }
                        }
                    }
                }
            }
        });
    }

    private void setSymbolListeners() {
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
                if (numberOfClicks < 4) {
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

    private void sendPlayerSkocko() {

        String combo = "";
        String colors = "";
        Toast.makeText(this, currentLetter, Toast.LENGTH_SHORT).show();

        Resources res = getResources();

        for (int i = 1; i < 5; i++) {
            String strId = String.valueOf(i);
            int id = res.getIdentifier(currentLetter + strId, "id", getPackageName());
            TextView textView = findViewById(id);
            String val = textView.getText().toString();
            combo = combo + stringSymbols.indexOf(val) + ";";
        }

        for (int j = 0; j < colorsList.size(); j++) {
            String strId = String.valueOf(j);
            int id = res.getIdentifier(currentLetter + strId, "id", getPackageName());
            TextView textView = findViewById(id);
            if (colorsList.get(j).equals("dark purple")) {
                colors = colors + "1;";
            } else if (colorsList.get(j).equals("purple")) {
                colors = colors + "2;";
            }
        }

        HomeFragment.socket.emit("sendPlayerSkocko", id, opponentId, combo, colors, isCorrect, numberOfTries);

    }

    private void sendPlayerCorrectSkocko() {

        String combo = "";
        String colors = "1;1;1;1;";

        for (int i = 1; i < 5; i++) {
            combo = combo + stringSymbols.indexOf(correctCombo.get(i)) + ";";
        }

        HomeFragment.socket.emit("sendPlayerSkockoCorrect", id, opponentId, combo, colors, true, numberOfTries);

    }

    private void sendPlayerChanceSkocko() {
        String combo = "";
        String colors = "";

        Resources res = getResources();

        for (int i = 1; i < 5; i++) {
            String strId = String.valueOf(i);
            int id = res.getIdentifier(currentLetter + strId, "id", getPackageName());
            TextView textView = findViewById(id);
            String val = textView.getText().toString();
            combo = combo + stringSymbols.indexOf(val) + ";";
        }

        for (int j = 5; j < 9; j++) {
            String strId = String.valueOf(j);
            int id = res.getIdentifier("g" + strId, "id", getPackageName());
            TextView textView = findViewById(id);
            if (textView.getBackground() == getDrawable(R.drawable.correct_circle)) {
                colors = colors + "1;";
            } else if (textView.getBackground() == getDrawable(R.drawable.partially_correct_circle)) {
                colors = colors + "2;";
            }
        }

        if (isCorrect) {
            HomeFragment.socket.emit("sendPlayerSkockoCorrect", id, opponentId, combo, colors, true, numberOfTries);
        } else {
            HomeFragment.socket.emit("sendPlayerSkocko", id, opponentId, combo, colors, false, numberOfTries);
        }
    }

    private void showCorrectCombination() {
        int counter = 5;
        Resources res = getResources();
        for (int i = 1; i < 5; i++) {
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

    private void checkCombination() {
        colorsList = new ArrayList<>();
        ArrayList<String> purples = new ArrayList<>();
        ArrayList<String> darkPurples = new ArrayList<>();
        ArrayList<String> wrongPlaced = new ArrayList<>();
        int counter = 0;

        Resources res = getResources();
        HashMap<Integer, String> comboToCheck = new HashMap<>();
        comboToCheck.putAll(correctCombo);

        for (int i = 1; i < 5; i++) {
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

        for (int i = 0; i < wrongPlaced.size(); i++) {

            if (comboToCheck.containsValue(wrongPlaced.get(i))) {
                purples.add("purple");
            }

            for (Map.Entry<Integer, String> entry : comboToCheck.entrySet()) {
                if (entry.getValue() == wrongPlaced.get(i)) {
                    comboToCheck.remove(entry.getKey());
                    break;
                }
            }
        }

        colorsList.addAll(darkPurples);
        colorsList.addAll(purples);

        int circleCounter = 5;

        for (int i = 0; i < colorsList.size(); i++) {
            int id = res.getIdentifier(currentLetter + circleCounter, "id", getPackageName());
            TextView textView = findViewById(id);
            circleCounter = circleCounter + 1;

            if (colorsList.get(i).equals("dark purple")) {
                textView.setBackground(getDrawable(R.drawable.correct_circle));
            } else {
                textView.setBackground(getDrawable(R.drawable.partially_correct_circle));
            }
        }

        if (counter == 4) {
            isCorrect = true;
        }
    }

    private void changeLetter() {
        if (numberOfTries == 5) {
            currentLetter = "b";
        } else if (numberOfTries == 4) {
            currentLetter = "c";
        } else if (numberOfTries == 3) {
            currentLetter = "d";
        } else if (numberOfTries == 2) {
            currentLetter = "e";
        } else if (numberOfTries == 1) {
            currentLetter = "f";
        } else {
            currentLetter = "g";
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
                //showTimerEndDialog();
                //finishGame();
                restartTimer(5000);
                //nextGame();
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void restartTimer(int millis) {
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(millis, 1000) {
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

    private void restartTimerEnd(int millis) {
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;
                HomeFragment.socket.off("sendPlayerSkocko");
                HomeFragment.socket.off("notifyOpponentSkocko");
                HomeFragment.socket.off("sendPlayerSkockoCorrect");
                //finishGame();
                if (counter != 0){
                    nextGame();
                } else {
                    counter = 1;
                    recreate();
                    //finish();
                }
            }
        };

        countDownTimer.start();
    }

    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }

    private void showTimerEndDialog() {
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
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showSocketData(JSONObject obj) {

        String correctComboStr = "";

        for (Map.Entry<Integer, String> entry : correctCombo.entrySet()) {
            correctComboStr = correctComboStr + entry.getValue() + ";";
        }

        try {
            String combo;
            String colors;

            if (isCorrect && numberOfTries == -1) {
                combo = correctComboStr;
                colors = "1;1;1;1;";
            } else {
                combo = obj.get("combo").toString();
                colors = obj.get("colors").toString();
            }

            String numOfTries = obj.get("numOfTries").toString();

            numberOfTries = Integer.parseInt(numOfTries);

            int circleCounter = 5;

            Resources res = getResources();

            for (int j = 0; j < 4; j++) {
                String comboElement = combo.split(";")[j];
                System.out.println(comboElement);
                int num;
               //if (numberOfTries == -1 && counter != 0){
                 //   num = stringSymbols.indexOf(comboElement);
                //} else {
                    num = Integer.parseInt(comboElement);
                //}
                int id = res.getIdentifier(currentLetter + (j + 1), "id", getPackageName());
                TextView textView = findViewById(id);

                textView.setText(stringSymbols.get(num));
            }

            for (int i = 0; i < colors.split(";").length; i++) {
                int id = res.getIdentifier(currentLetter + circleCounter, "id", getPackageName());
                TextView textView = findViewById(id);
                circleCounter = circleCounter + 1;

                String colorElement = colors.split(";")[i];

                if (colorElement.equals("1")) {
                    textView.setBackground(getDrawable(R.drawable.correct_circle));
                } else if (colorElement.equals("2")) {
                    textView.setBackground(getDrawable(R.drawable.partially_correct_circle));
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void finishGame() {
        int p1Points = Integer.parseInt(p1PointsText);
        int p2Points = Integer.parseInt(p2PointsText);
        if ((numberOfTries == 5 || numberOfTries == 4) && isCorrect == true) {
            if ((counter == 0 && priority.equals("1")) ||
                    counter == 1 && priority.equals("2")) {
                Toast.makeText(this, "20 POINTS", Toast.LENGTH_SHORT).show();
                player1Points.setText(String.valueOf(p1Points + 20) + " points");
                totalPoints = totalPoints + 20;
                p1Points = p1Points + 20;
            } else {
                player2Points.setText(String.valueOf(p2Points + 20) + " points");
                Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
                p2Points = p1Points + 20;
            }
        } else if ((numberOfTries == 3 || numberOfTries == 2) && isCorrect == true) {
            if ((counter == 0 && priority.equals("1")) ||
                    counter == 1 && priority.equals("2")) {
                Toast.makeText(this, "15 POINTS", Toast.LENGTH_SHORT).show();
                player1Points.setText(String.valueOf(p1Points + 15) + " points");
                totalPoints = totalPoints + 15;
                p1Points = p1Points + 15;
            } else {
                player2Points.setText(String.valueOf(p2Points + 20) + " points");
                Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
                p2Points = p2Points + 20;
            }
        } else if ((numberOfTries == 1 || numberOfTries == 0) && isCorrect == true) {
            if ((counter == 0 && priority.equals("1")) ||
                    counter == 1 && priority.equals("2")) {
                Toast.makeText(this, "10 POINTS", Toast.LENGTH_SHORT).show();
                player1Points.setText(String.valueOf(p1Points + 10) + " points");
                totalPoints = totalPoints + 10;
                p1Points = p1Points + 10;
            } else {
                player2Points.setText(String.valueOf(p2Points + 20) + " points");
                Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
                p2Points = p2Points + 10;
            }
        } else if (numberOfTries == -1){
            if ((counter == 0 && priority.equals("1")) ||
            counter == 1 && priority.equals("2")){
                if (isCorrect){
                    Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
                    player2Points.setText(String.valueOf(p1Points + 10) + " points");
                    p2Points = p2Points + 10;
                }
            } else {
                if (isCorrect){
                    Toast.makeText(this, "10 POINTS", Toast.LENGTH_SHORT).show();
                    player1Points.setText(String.valueOf(p2Points + 10) + " points");
                    p1Points = p1Points + 10;
                }
            }
        }
        else {
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText(p1PointsText + " points");
            player2Points.setText(p2PointsText + " points");
        }

        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("POINTS", String.valueOf(p1Points)).apply();

        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("OPPONENT_POINTS", String.valueOf(p2Points)).apply();

        for (int i = 0; i < symbols.size(); i++) {
            symbols.get(i).setEnabled(false);
        }
        okButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void nextGame() {
        //Handler handler = new Handler();
        //handler.postDelayed(new Runnable(){
        //@Override
        //public void run() {

        Bundle bundle = new Bundle();
        bundle.putInt(
                "points",
                totalPoints
        );

        Intent intent = new Intent(GameThreeActivity.this, GameFourActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        //}
        //}, 5000);
    }
}