package com.example.slagalica_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    private TextView timerText;
    private TextView player1Points;
    private TextView player2Points;

    private CountDownTimer countDownTimer;
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

    private String currentMatch;

    private int numOfTries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_four);

        assignment = findViewById(R.id.assignment);

        numOfTries = 0;

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

        wordsToMatch = new ArrayList<>();
        wordsToBeMatchedWith = new ArrayList<>();
        matches = new ArrayList<>();

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

        aWord1.setBackground(getDrawable(R.drawable.player_one_border));
        setListeners();

    }

    private void getData(){

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

        for(int i = 0; i < spojnice.size(); i++){
            Spojnica sp = spojnice.get(i);

            if (sp.getWordTwo() == null){
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
            } else if (counter != 0 && sp.getRound().equals("2")){
                int id1 = res.getIdentifier(sp.getPlaceOne(), "id", getPackageName());
                int id2 = res.getIdentifier(sp.getPlaceTwo(), "id", getPackageName());

                TextView tv1 = findViewById(id1);
                TextView tv2 = findViewById(id2);
                tv1.setText(sp.getWordOne());
                tv2.setText(sp.getWordTwo());

                String match = sp.getWordOne() + ";" + spojnica.getWordTwo();
                matches.add(match);
            }
        }

        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("spojnice");
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

    private void setListeners(){

        Resources res = getResources();
        List<String> subList;
        if (counter == 0) {
            subList = matches.subList(0, 4);
        } else {
            subList = matches.subList(5, 9);
        }



        for (int i = 0; i < bWords.size(); i++){
            int finalI = i;
            bWords.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentMatch = aWords.get(numOfTries).getText().toString() + ";" + bWords.get(finalI).getText().toString();
                    //if (!currentMatch.equals("")){

                        if (matches.contains(currentMatch)){
                            bWords.get(finalI).setBackground(getDrawable(R.drawable.player_one_border));
                            bWords.get(finalI).setEnabled(false);
                            matches.remove(currentMatch);
                            numOfTries = numOfTries + 1;
                            if (numOfTries < 5) {
                                aWords.get(numOfTries).setBackground(getDrawable(R.drawable.player_one_border));
                            }

                        } else {
                            aWords.get(numOfTries).setBackground(getDrawable(R.drawable.lavender_border));
                            if (numOfTries < 4) {
                                aWords.get(numOfTries + 1).setBackground(getDrawable(R.drawable.player_one_border));
                            }
                            numOfTries = numOfTries + 1;
                        }
                    }
                //}
            });
        }
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

    public Spojnica(){

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