package com.example.slagalica_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameFourActivity extends AppCompatActivity {

    TextView assignment;
    String assignmentText;

    TextView aWord1;
    TextView aWord2;
    TextView aWord3;
    TextView aWord4;
    TextView aWord5;

    TextView bWord1;
    TextView bWord2;
    TextView bWord3;
    TextView bWord4;
    TextView bWord5;

    private ArrayList<TextView> bWords;
    private ArrayList<TextView> aWords;

    private ArrayList<TextView> originalAWords;
    private ArrayList<TextView> originalBWords;

    private TextView timerText;
    private TextView player1Points;
    private TextView player2Points;

    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;
    private CountDownTimer countDownTimer3;
    private boolean isTimerRunning = false;

    private long startTime = 60000;

    private String p1PointsText;
    private String p2PointsText;

    private static int counter = 0;
    private String priority;

    private String id;
    private String opponentId;

    private ArrayList<String> wordsToMatch;
    private ArrayList<String> wordsToBeMatchedWith;
    private ArrayList<String> matches;
    private ArrayList<String> matched;

    private String currentMatch;

    private int numOfTries;
    private int numOfSecondTries;

    private ArrayList<String> matchesToSend;

    private String matchAB;

    private int player1Matched;
    private int player2Matched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_four);

        assignment = findViewById(R.id.assignment);

        numOfTries = 0;
        numOfSecondTries = 0;

        //isSpojniceMatched, isSpojniceSolved, isSpojniceAllSolved, givePlayerAChanceSpojnice - socket

        timerText = findViewById(R.id.timer);
        player1Points = findViewById(R.id.playerOne_points);
        player2Points = findViewById(R.id.playerTwo_points);

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

        player1Points.setText(p1PointsText + " points");
        player2Points.setText(p2PointsText + " points");

        wordsToMatch = new ArrayList<>();
        wordsToBeMatchedWith = new ArrayList<>();
        matches = new ArrayList<>();
        matched = new ArrayList<>();
        matchesToSend = new ArrayList<>();
        matchAB = "";
        player1Matched = 0;
        player2Matched = 0;

        originalAWords = new ArrayList<>();
        originalBWords = new ArrayList<>();

        bWords = new ArrayList<>();
        aWords = new ArrayList<>();

        aWord1 = findViewById(R.id.a1);
        aWord2 = findViewById(R.id.a2);
        aWord3 = findViewById(R.id.a3);
        aWord4 = findViewById(R.id.a4);
        aWord5 = findViewById(R.id.a5);

        bWord1 = findViewById(R.id.b1);
        bWord2 = findViewById(R.id.b2);
        bWord3 = findViewById(R.id.b3);
        bWord4 = findViewById(R.id.b4);
        bWord5 = findViewById(R.id.b5);

        aWords.add(aWord1);
        aWords.add(aWord2);
        aWords.add(aWord3);
        aWords.add(aWord4);
        aWords.add(aWord5);

        bWords.add(bWord1);
        bWords.add(bWord2);
        bWords.add(bWord3);
        bWords.add(bWord4);
        bWords.add(bWord5);

        getData();

        if ((counter == 0 && priority.equals("1"))
                || (counter == 1 && priority.equals("2"))) {
            aWord1.setBackground(getDrawable(R.drawable.player_one_border));
        }

        socketReceiveChance();
        socketRecieveAllCorrect();
        socketReceiveFinish();
    }

    private void getData() {

        Resources res = getResources();

        Spojnica spojnica1 = new Spojnica("1", "1", "trava", "zelena", "a1", "b3");
        Spojnica spojnica2 = new Spojnica("1", "1", "more", "plava", "a2", "b2");
        Spojnica spojnica3 = new Spojnica("1", "1", "jabuka", "crvena", "a3", "b4");
        Spojnica spojnica4 = new Spojnica("1", "1", "banana", "zuta", "a4", "b5");
        Spojnica spojnica5 = new Spojnica("1", "1", "narandza", "narandzasta", "a5", "b1");
        Spojnica spojnica6 = new Spojnica("1", "2", "nebo", "plava", "a1", "b4");
        Spojnica spojnica7 = new Spojnica("1", "2", "sunce", "zuta", "a2", "b3");
        Spojnica spojnica8 = new Spojnica("1", "2", "oblak", "bela", "a3", "b1");
        Spojnica spojnica9 = new Spojnica("1", "2", "brokoli", "zelena", "a4", "b5");
        Spojnica spojnica10 = new Spojnica("1", "2", "paradajz", "crvena", "a5", "b2");

        Spojnica spojnica = new Spojnica("1", "0", "Spojite boju sa pojmom", "", "", "");

        ArrayList<Spojnica> spojnice = new ArrayList<>();
        spojnice.add(spojnica1);
        spojnice.add(spojnica2);
        spojnice.add(spojnica3);
        spojnice.add(spojnica4);
        spojnice.add(spojnica5);
        spojnice.add(spojnica6);
        spojnice.add(spojnica7);
        spojnice.add(spojnica8);
        spojnice.add(spojnica9);
        spojnice.add(spojnica10);
        spojnice.add(spojnica);

        assignmentText = spojnica.getWordOne();
        assignment.setText(assignmentText);

        for (int i = 0; i < spojnice.size(); i++) {
            Spojnica sp = spojnice.get(i);

            if (sp.getWordTwo() == null) {
                assignmentText = sp.getWordOne();
                assignment.setText(assignmentText);
                continue;
            }

            if (counter == 0 && sp.getRound().equals("1")) {

                int id1 = res.getIdentifier(sp.getPlaceOne(), "id", getPackageName());
                int id2 = res.getIdentifier(sp.getPlaceTwo(), "id", getPackageName());

                TextView tv1 = findViewById(id1);
                TextView tv2 = findViewById(id2);
                tv1.setText(sp.getWordOne());
                tv2.setText(sp.getWordTwo());

                String match = sp.getWordOne() + ";" + sp.getWordTwo();
                matches.add(match);
            } else if (counter != 0 && sp.getRound().equals("2")) {
                int id1 = res.getIdentifier(sp.getPlaceOne(), "id", getPackageName());
                int id2 = res.getIdentifier(sp.getPlaceTwo(), "id", getPackageName());

                TextView tv1 = findViewById(id1);
                TextView tv2 = findViewById(id2);
                tv1.setText(sp.getWordOne());
                tv2.setText(sp.getWordTwo());

                String match = sp.getWordOne() + ";" + sp.getWordTwo();
                matches.add(match);
            }

            if ((counter == 0 && priority.equals("1"))
                || (counter == 1 && priority.equals("2"))){
                setListeners(bWords);
            }
        }

        startTimer(30000);

        /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference("spojnice");
        Query checkSpojnicaDatabase = reference.orderByChild("gameId").equalTo("1");

        checkSpojnicaDatabase.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Resources res = getResources();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Spojnica spojnica = dataSnapshot.getValue(Spojnica.class);

                    if (spojnica.getWordTwo() == null){
                        assignmentText = spojnica.getWordOne();
                        assignment.setText(assignmentText);
                        continue;
                    }

                    int id1 = res.getIdentifier(spojnica.getPlaceOne(), "id", getPackageName());
                    int id2 = res.getIdentifier(spojnica.getPlaceTwo(), "id", getPackageName());

                    TextView tv1 = findViewById(id1);
                    TextView tv2 = findViewById(id2);
                    tv1.setText(spojnica.getWordOne());
                    tv2.setText(spojnica.getWordTwo());

                    String match = spojnica.getWordOne() + ";" + spojnica.getWordTwo();
                    matches.add(match);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    private void socketRecieveAllCorrect() {
        HomeFragment.socket.on("receiveAllCorrectSpojnice", args -> {
            String opponentId = (String) args[0];
            if (opponentId.equals(id)) {
                Resources res = getResources();

                for (int i = 0; i < aWords.size(); i++) {

                    aWords.get(i).setBackground(getDrawable(R.drawable.player_two_border));
                    bWords.get(i).setBackground(getDrawable(R.drawable.player_two_border));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        restartTimerEnd();
                    }
                });
            }

        });
    }

    private void socketReceiveChance() {
        HomeFragment.socket.on("receiveChanceSpojnice", args -> {
            String opponentId;
            String matched;

            opponentId = args[0].toString();
            matched = args[1].toString();

            if (opponentId.equals(id)) {
                Resources res = getResources();

                String[] toMatch = matched.split("-");

                originalAWords.addAll(aWords);
                originalBWords.addAll(bWords);
                if (!toMatch[0].equals("")) {
                    for (int i = 0; i < toMatch.length; i++) {
                        String aColumn;
                        String bColumn;

                        aColumn = toMatch[i].split(";")[0];
                        bColumn = toMatch[i].split(";")[1];

                        int id1 = res.getIdentifier(aColumn, "id", getPackageName());
                        int id2 = res.getIdentifier(bColumn, "id", getPackageName());

                        TextView tv1 = findViewById(id1);
                        TextView tv2 = findViewById(id2);

                        tv1.setBackground(getDrawable(R.drawable.player_two_border));
                        tv2.setBackground(getDrawable(R.drawable.player_two_border));

                        matches.remove(toMatch[i]);
                        //this.matched.add(toMatch[i]);

                        aWords.remove(tv1);
                        bWords.remove(tv2);

                        numOfTries = numOfTries + 1;
                        wordsToMatch.add(toMatch[i]);
                    }
                }

                player1Matched = 5 - wordsToMatch.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListeners(bWords);
                        restartTimer();
                    }
                });

                aWords.get(0).setBackground(getDrawable(R.drawable.player_one_border));
            }
        });
    }

    private void socketReceiveFinish(){
        HomeFragment.socket.on("receiveFinishSpojnice", args -> {
            String opponentId;
            String matched;

            opponentId = args[0].toString();
            matched = args[1].toString();

            if (opponentId.equals(id)) {
                Resources res = getResources();

                String[] toMatch = matched.split("-");

                if (!toMatch[0].equals("")) {
                    for (int i = 0; i < toMatch.length; i++) {
                        String aColumn;
                        String bColumn;

                        aColumn = toMatch[i].split(";")[0];
                        bColumn = toMatch[i].split(";")[1];

                        int id1 = res.getIdentifier(aColumn, "id", getPackageName());
                        int id2 = res.getIdentifier(bColumn, "id", getPackageName());

                        TextView tv1 = findViewById(id1);
                        TextView tv2 = findViewById(id2);

                        tv1.setBackground(getDrawable(R.drawable.player_two_border));
                        tv2.setBackground(getDrawable(R.drawable.player_two_border));

                        wordsToMatch.add(toMatch[i]);
                    }
                }

                player2Matched = wordsToMatch.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishGame();
                        restartTimerEnd();
                        HomeFragment.socket.off("sendFinishSpojnice");
                        HomeFragment.socket.off("receiveFinishSpojnice");
                    }
                });

            }
        });

    }

    private void setListeners(ArrayList<TextView> bWords) {

        Resources res = getResources();

        for (int i = 0; i < bWords.size(); i++) {
            int finalI = i;
            bWords.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((counter == 0 && priority.equals("1"))
                    || (counter == 1 && priority.equals("2"))) {
                        currentMatch = aWords.get(numOfTries).getText().toString() + ";" + bWords.get(finalI).getText().toString();
                    } else {
                        currentMatch = aWords.get(numOfSecondTries).getText().toString() + ";" + bWords.get(finalI).getText().toString();
                    }

                    if (matches.contains(currentMatch)) {
                        if ((counter == 0 && priority.equals("1"))
                                || (counter == 1 && priority.equals("2"))) {
                            bWords.get(finalI).setBackground(getDrawable(R.drawable.player_one_border));
                            bWords.get(finalI).setEnabled(false);
                            matches.remove(currentMatch);
                            matched.add(currentMatch);
                            numOfTries = numOfTries + 1;
                            String matchA = "a" + numOfTries;
                            String matchB = "b" + (finalI + 1);
                            matchAB = matchAB + matchA + ";" + matchB + "-";
                            if (numOfTries < 5) {
                                aWords.get(numOfTries).setBackground(getDrawable(R.drawable.player_one_border));
                            } else {
                                if (matches.size() == 0) {
                                    HomeFragment.socket.emit("sendEverythingCorrectSpojnice", opponentId);
                                    for (int i = 0; i < aWords.size(); i++){
                                        aWords.get(i).setEnabled(false);
                                        bWords.get(i).setEnabled(false);
                                    }
                                    restartTimerEnd();
                                } else {
                                    HomeFragment.socket.emit("sendChanceSpojnice", opponentId, matchAB);
                                    for (int i = 0; i < aWords.size(); i++){
                                        aWords.get(i).setEnabled(false);
                                        bWords.get(i).setEnabled(false);
                                    }
                                    restartTimer();
                                }
                            }
                        } else {
                            bWords.get(finalI).setBackground(getDrawable(R.drawable.player_one_border));
                            bWords.get(finalI).setEnabled(false);
                            matched.add(currentMatch);
                            String matchA = "a" + (originalAWords.indexOf(aWords.get(numOfSecondTries)) + 1);
                            String matchB = "b" + (originalBWords.indexOf(bWords.get(finalI)) + 1);
                            matchAB = matchAB + matchA + ";" + matchB + "-";
                            if ((numOfSecondTries + 1) < bWords.size()) {
                                if ((numOfSecondTries + 1) < aWords.size()) {
                                    aWords.get(numOfSecondTries + 1).setBackground(getDrawable(R.drawable.player_one_border));
                                    numOfSecondTries = numOfSecondTries + 1;
                                }
                            } else {
                                HomeFragment.socket.emit("sendFinishSpojnice", opponentId, matchAB);
                                finishGame();
                                for (int i = 0; i < originalAWords.size(); i++){
                                    originalAWords.get(i).setEnabled(false);
                                    originalBWords.get(i).setEnabled(false);
                                }
                                restartTimerEnd();
                                HomeFragment.socket.off("sendFinishSpojnice");
                                HomeFragment.socket.off("receiveFinishSpojnice");
                            }
                        }

                    } else {

                            if ((counter == 0 && priority.equals("1"))
                                    || (counter != 0 && priority.equals("2"))) {
                                if (numOfTries < 4) {
                                    aWords.get(numOfTries).setBackground(getDrawable(R.drawable.lavender_border));
                                    aWords.get(numOfTries + 1).setBackground(getDrawable(R.drawable.player_one_border));
                                    numOfTries = numOfTries + 1;
                                } else {
                                    aWords.get(numOfTries).setBackground(getDrawable(R.drawable.lavender_border));
                                    HomeFragment.socket.emit("sendChanceSpojnice", opponentId, matchAB);
                                    restartTimer();
                                    for (int i = 0; i < aWords.size(); i++){
                                        aWords.get(i).setEnabled(false);
                                        bWords.get(i).setEnabled(false);
                                    }
                                }
                            } else {
                                aWords.get(numOfSecondTries).setBackground(getDrawable(R.drawable.lavender_border));
                                if ((numOfSecondTries + 1) < aWords.size()) {
                                    aWords.get(numOfSecondTries + 1).setBackground(getDrawable(R.drawable.player_one_border));
                                    numOfSecondTries = numOfSecondTries + 1;
                                } else {
                                    HomeFragment.socket.emit("sendFinishSpojnice", opponentId, matchAB);
                                    restartTimerEnd();
                                    HomeFragment.socket.off("sendFinishSpojnice");
                                    HomeFragment.socket.off("receiveFinishSpojnice");
                                    finishGame();
                                    for (int i = 0; i < originalAWords.size(); i++){
                                        originalAWords.get(i).setEnabled(false);
                                        originalBWords.get(i).setEnabled(false);
                                    }
                                }
                            }
                    }
                }
            });
        }
    }

    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);

        String time = String.format("%02d", seconds);
        timerText.setText(time);
    }

    private void restartTimerEnd(){
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
        }

        countDownTimer3 = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;

                if(counter != 0){
                    Intent intent = new Intent(GameFourActivity.this, GameSixActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    counter = 1;
                    recreate();
                }
            }
        };

        countDownTimer3.start();
    }

    private void restartTimer(){
        countDownTimer.cancel();

        countDownTimer2 = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;

                if ((counter == 0 && priority.equals("2")) ||
                        (counter == 1 && priority.equals("1"))) {
                    HomeFragment.socket.emit("sendFinishSpojnice", opponentId, matchAB);
                    finishGame();
                    restartTimerEnd();

                    for (int i = 0; i < originalAWords.size(); i++){
                        originalAWords.get(i).setEnabled(false);
                        originalBWords.get(i).setEnabled(false);
                    }
                }
            }
        };

        countDownTimer2.start();
    }

    private void startTimer(long time){
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;
                if ((counter == 0 && priority.equals("1")) ||
                        (counter == 1 && priority.equals("2"))) {
                    HomeFragment.socket.emit("sendChanceSpojnice", opponentId, matchAB);
                    restartTimer();
                    for (int i = 0; i < aWords.size(); i++){
                        aWords.get(i).setEnabled(false);
                        bWords.get(i).setEnabled(false);
                    }
                }
                //evalResults();
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void finishGame(){

        int p1Points = Integer.parseInt(p1PointsText);
        int p2Points = Integer.parseInt(p2PointsText);

        int p1GainedPoints = 0;
        int p2GainedPoints = 0;

        if ((counter == 0 && priority.equals("1")) ||
                (counter != 0 && priority.equals("2"))){

            p1GainedPoints = (matched.size() * 2);
            p2GainedPoints = (wordsToMatch.size() * 2);

            p1Points = p1Points + p1GainedPoints;
            p2Points = p2Points + p2GainedPoints;
        } else {

            p1GainedPoints = (matched.size() * 2);
            p2GainedPoints = (wordsToMatch.size() * 2);

            p1Points = p1Points + p1GainedPoints;
            p2Points = p2Points + p2GainedPoints;
        }

        Toast.makeText(this, p1GainedPoints + " points", Toast.LENGTH_SHORT).show();

        player1Points.setText(p1Points + " points");
        player2Points.setText(p2Points + " points");

        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("POINTS", String.valueOf(p1Points)).apply();

        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("OPPONENT_POINTS", String.valueOf(p2Points)).apply();
    }
}

class Spojnica {
    private String gameId;
    private String round;
    private String wordOne;
    private String wordTwo;
    private String placeOne;
    private String placeTwo;

    public Spojnica(String gameId, String round, String wordOne, String wordTwo, String placeOne, String placeTwo) {
        this.gameId = gameId;
        this.round = round;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.placeOne = placeOne;
        this.placeTwo = placeTwo;
    }

    public Spojnica() {

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getWordOne() {
        return wordOne;
    }

    public void setWordOne(String wordOne) {
        this.wordOne = wordOne;
    }

    public String getWordTwo() {
        return wordTwo;
    }

    public void setWordTwo(String wordTwo) {
        this.wordTwo = wordTwo;
    }

    public String getPlaceOne() {
        return placeOne;
    }

    public void setPlaceOne(String placeOne) {
        this.placeOne = placeOne;
    }

    public String getPlaceTwo() {
        return placeTwo;
    }

    public void setPlaceTwo(String placeTwo) {
        this.placeTwo = placeTwo;
    }
}