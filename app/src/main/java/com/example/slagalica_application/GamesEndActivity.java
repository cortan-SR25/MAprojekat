package com.example.slagalica_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GamesEndActivity extends AppCompatActivity {
    Button backToHomeButton;

    private TextView player1Points, player2Points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_end);

        backToHomeButton = findViewById(R.id.backToHomeButton);
        player1Points = findViewById(R.id.endGamePoints1);
        player2Points = findViewById(R.id.endGamePoints2);

        String p1PointsText = PreferenceManager.getDefaultSharedPreferences(this).
                getString("POINTS", null);

        String p2PointsText = PreferenceManager.getDefaultSharedPreferences(this).
                getString("OPPONENT_POINTS", null);

        player1Points.setText(p1PointsText + " points");
        player2Points.setText(p2PointsText + " points");


        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamesEndActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}