package com.example.slagalica_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameFourActivity extends AppCompatActivity {

    TextView assignment;
    String assignmentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_four);

        assignment = findViewById(R.id.assignment);
        assignmentText = "Spojite fizčke pojmove sa međunarodno " +
                "prihvaćenim jedinicama mere";
        assignment.setText(assignmentText);

        //isSpojniceMatched, isSpojniceSolved, isSpojniceAllSolved, givePlayerAChanceSpojnice - socket
    }
}