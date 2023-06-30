package com.example.slagalica_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFiveActivity extends AppCompatActivity {

    public static ArrayList<KoZnaZna> listOfQuestions;
    DatabaseReference databaseReference;

    KoZnaZna koZnaZna;
    int index = 0;

    private TextView timerText;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long startTime = 60000;

    TextView card_question, option1, option2, option3, option4;
    TextView questionCountText;

    int correctCount = 0;
    int wrongCount = 0;

    int questionCounter = 1;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_five);
        timerText = findViewById(R.id.timer);

        listOfQuestions = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("KoZnaZna");

        card_question = findViewById(R.id.card_question);
        option1 = findViewById(R.id.o1);
        option2 = findViewById(R.id.o2);
        option3 = findViewById(R.id.o3);
        option4 = findViewById(R.id.o4);

        questionCountText = findViewById(R.id.questionCount);
        nextButton = findViewById(R.id.nextButton);




//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    KoZnaZna koZnaZna = dataSnapshot.getValue(KoZnaZna.class);
//                    listOfQuestions.add(koZnaZna);
//                }
//
//                Intent intent = new Intent(getApplicationContext(), GameFiveActivity.class);
//                startActivity(intent);
//                //Intent intent start..
////                Intent intent = new Intent(GameFiveActivity.this, GameFiveActivity.class);
////                startActivity(intent);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(GameFiveActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });

        listOfQuestions.add(new KoZnaZna("Glavnu ulogu u filmu Umri Muški glumi", "Brus Vilis", "Silvester Stalone", "Klint Istvud", "Brus Vilis", "Maras"));
        listOfQuestions.add((new KoZnaZna("Zabe spadaju u...", "Vodozemce", "Sisare", "Gmizavce", "Vodozemce", "Ribe")));
        listOfQuestions.add((new KoZnaZna("Prvi svetski rat je poceo...", "1914", "1912", "1914", "1904", "1814")));
        listOfQuestions.add((new KoZnaZna("Koliko NBA titula je osvojio Majkl Džordan u toku svoje karijere?", "6", "4", "5", "6", "3")));
        listOfQuestions.add((new KoZnaZna("Najveća Euro novčanica je...", "500 eura", "1000 eura", "2000 eura", "5000 eura", "500 eura")));


        Collections.shuffle(listOfQuestions);
        koZnaZna = listOfQuestions.get(index);

        setAllData();
        startTimer(startTime);
        nextButton.setClickable(false);

    }

    private void setAllData(){
        card_question.setText(koZnaZna.getQuestion());
        option1.setText(koZnaZna.getO1());
        option2.setText(koZnaZna.getO2());
        option3.setText(koZnaZna.getO3());
        option4.setText(koZnaZna.getO4());

        questionCountText.setText(questionCounter + "/5");
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

    private void Correct(TextView textView){
        textView.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctCount++;
                if(index<listOfQuestions.size()-1){
                    index++;
                    koZnaZna=listOfQuestions.get(index);
                    resetColor();
                    enableButton();
                    setAllData();
                    questionCounter++;
                    questionCountText.setText(questionCounter + "/5");
                }else {
                    NextGame();
                }

            }
        });


    }

    private void Wrong(TextView option1){
        option1.setBackground(getResources().getDrawable(R.drawable.player_two_border));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongCount++;
                if(index<listOfQuestions.size()-1){
                    index++;
                    koZnaZna = listOfQuestions.get(index);
                    resetColor();
                    enableButton();
                    setAllData();
                    questionCounter++;
                    questionCountText.setText(questionCounter + "/5");
                }else{
                    NextGame();              //GameWon Yt
                }
            }
        });
    }

    private void NextGame(){
//        Intent intent = new Intent(GameFiveActivity.this, GameSixActivity.class);
//        startActivity(intent);

        Toast.makeText(GameFiveActivity.this, "You had " + correctCount + " Correcnt points!" + wrongCount + "Wrong ones", Toast.LENGTH_LONG).show();
    }

    private void enableButton(){
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);
        option4.setClickable(true);
    }

    private void disableButton(){
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        option4.setClickable(false);
    }

    private void resetColor(){
        option1.setBackground(getResources().getDrawable(R.drawable.lavender_border));
        option2.setBackground(getResources().getDrawable(R.drawable.lavender_border));
        option3.setBackground(getResources().getDrawable(R.drawable.lavender_border));
        option4.setBackground(getResources().getDrawable(R.drawable.lavender_border));

    }

    public void optionAClick(View view) {

        nextButton.setClickable(true);
        if(koZnaZna.getO1().equals(koZnaZna.getAnswer()))
        {
            option1.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
            disableButton();
            if(index<listOfQuestions.size()-1)
            {
                Correct(option1);
            }
            else
            {
                NextGame();
            }
        }
        else
        {
            disableButton();
            Wrong(option1);
        }
    }

    public void optionBClick(View view) {

        nextButton.setClickable(true);
        if(koZnaZna.getO2().equals(koZnaZna.getAnswer()))
        {
            option2.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
            disableButton();
            if(index<listOfQuestions.size()-1)
            {
                Correct(option2);
            }
            else
            {
                NextGame();
            }
        }
        else
        {
            disableButton();
            Wrong(option2);
        }
    }

    public void optionCClick(View view) {

        nextButton.setClickable(true);
        if(koZnaZna.getO3().equals(koZnaZna.getAnswer()))
        {
            option3.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
            disableButton();
            if(index<listOfQuestions.size()-1)
            {
                Correct(option3);
            }
            else
            {
                NextGame();
            }
        }
        else
        {
            disableButton();
            Wrong(option3);
        }
    }

    public void optionDClick(View view) {

        nextButton.setClickable(true);
        if(koZnaZna.getO4().equals(koZnaZna.getAnswer()))
        {
            option4.setBackground(getResources().getDrawable(R.drawable.correct_answer_border));
            disableButton();
            if(index<listOfQuestions.size()-1)
            {
                Correct(option4);
            }
            else
            {
                NextGame();
            }
        }
        else
        {
            disableButton();
            Wrong(option4);
        }
    }

    private void showTimerEndDialog(){
        ConstraintLayout timerEndConstraintLayout = findViewById(R.id.timerEndConstraintLayout);
        View view = LayoutInflater.from(GameFiveActivity.this).inflate(R.layout.timer_end_dialog, timerEndConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameFiveActivity.this);
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



class KoZnaZna {
    String Question;
    String answer;
    String o1;
    String o2;
    String o3;
    String o4;

    public KoZnaZna() {
    }

    public KoZnaZna(String question, String answer, String o1, String o2, String o3, String o4) {
        Question = question;
        this.answer = answer;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getO1() {
        return o1;
    }

    public void setO1(String o1) {
        this.o1 = o1;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getO4() {
        return o4;
    }

    public void setO4(String o4) {
        this.o4 = o4;
    }
}

