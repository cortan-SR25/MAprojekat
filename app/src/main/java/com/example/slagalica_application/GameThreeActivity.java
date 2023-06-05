package com.example.slagalica_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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

    private ArrayList<TextView> rows = new ArrayList<>();
    private ArrayList<TextView> symbols;

    private boolean isCorrect = false;

    HashMap<Integer, String> correctCombo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);

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

        String letter = "a";

        /*for (int i = 1; i < 10; i++){
            if (i == 9){
                if (letter == "h"){
                    break;
                }

                if (letter == "g"){
                    letter = "h";
                    i = 1;
                    continue;
                } else if (letter == "f"){
                    letter = "g";
                    i = 1;
                    continue;
                } else if (letter == "e"){
                    letter = "f";
                    i = 1;
                    continue;
                } else if (letter == "d"){
                    letter = "e";
                    i = 1;
                    continue;
                } else if (letter == "c"){
                    letter = "d";
                    i = 1;
                    continue;
                } else if (letter == "b"){
                    letter = "c";
                    i = 1;
                    continue;
                } else if (letter == "a"){
                    letter = "b";
                    i = 1;
                    continue;
                }
            }
            Resources res = getResources();
            int id = res.getIdentifier(letter + String.valueOf(i), "id", getPackageName());
            TextView textView = findViewById(id);

        }*/

        setListeners();
        setSymbolListeners();
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
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numberOfClicks == 4) {

                    checkCombination();

                    numberOfTries = numberOfTries - 1;

                    if (isCorrect || numberOfTries == 1) {
                        showCorrectCombination();
                        for (int i = 0; i < symbols.size(); i++) {
                            symbols.get(i).setEnabled(false);
                        }
                        deleteButton.setEnabled(false);
                        okButton.setEnabled(false);
                    }

                    changeLetter();
                }
            }
        });
    }

    private void setSymbolListeners(){
        mouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
                if (numberOfClicks == 4){
                    for (int i = 0; i < symbols.size(); i++){
                        symbols.get(i).setEnabled(false);
                    }
                } else {
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
        int counter = 0;

        Resources res = getResources();

        for (int i = 1; i < 5; i++){
            int id = res.getIdentifier(currentLetter + i, "id", getPackageName());
            TextView textView = findViewById(id);
            String val = textView.getText().toString();

            if (correctCombo.containsValue(val)) {
                if (correctCombo.get(i).equals(val)) {
                    darkPurples.add("dark purple");
                    counter = counter + 1;
                } else {
                    purples.add("purple");
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

            if (colors.get(i) == "darkPurple"){
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
}