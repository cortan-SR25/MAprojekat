package com.example.slagalica_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameOneActivity extends AppCompatActivity {
    private TextView timerText;
    private TextView player1Points;
    private TextView player1Result;

    private TextView player2Points;
    private TextView player2Result;

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

    private Button stopButton;

    private Button confirmButton;
    private Button deleteButton;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    private long startTime = 60000;

    private String procedure = "";
    private double player1ResultNum;

    private ArrayList<Button> buttons;
    private ArrayList<Integer> numbers;

    private int totalPoints = 0;

    private boolean iSolved;

    private String id;
    private String opponentId;

    private String p1PointsText;
    private String p2PointsText;

    private static int counter = 0;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

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
        player1Result = findViewById(R.id.player1_result);
        player2Points = findViewById(R.id.playerTwo_points);
        player2Result = findViewById(R.id.player2_result);
        calculation = findViewById(R.id.calculation);

        player1Points.setText(p1PointsText + " points");
        player2Points.setText(p2PointsText + " points");

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

        stopButton = findViewById(R.id.stopBtn);

        if (counter == 0 && priority.equals("2")){
            stopButton.setVisibility(View.INVISIBLE);
        } else if (counter == 1 && priority.equals("1")){
            stopButton.setVisibility(View.INVISIBLE);
        }

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int randomResult = new Random().nextInt(501) + 1;

                int[] smallies = new int[]{10, 15, 20};
                int[] biggies = new int[]{25, 50, 75, 100};

                int num1 = new Random().nextInt(11) + 1;
                int num2 = new Random().nextInt(11) + 1;
                int num3 = new Random().nextInt(11) + 1;
                int num4 = new Random().nextInt(11) + 1;

                int smallie = smallies[new Random().nextInt(3)];
                int biggie = biggies[new Random().nextInt(4)];

                numbers.add(num1);
                numbers.add(num2);
                numbers.add(num3);
                numbers.add(num4);
                numbers.add(smallie);
                numbers.add(biggie);
                numbers.add(randomResult);

                HomeFragment.socket.emit("stopNumberMojBroj", opponentId, num1, num2, num3,
                        num4, smallie, biggie, randomResult);

                for (int i = 0; i < 7; i++){
                    buttons.get(i).setText(String.valueOf(numbers.get(i)));
                }

                setListeners();
            }
        });

        //setListeners();

        iSolved = false;

        startTimer(startTime);

        HomeFragment.socket.on("endMojBroj", args -> {
            JSONArray mojBrojArr = (JSONArray) args[0];
            System.out.println(args[0]);
            System.out.println(mojBrojArr);
            JSONObject p1mojBroj;
            JSONObject p2mojBroj;
            try {
                p1mojBroj = mojBrojArr.getJSONObject(0);
                p2mojBroj = mojBrojArr.getJSONObject(1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
            if (p1mojBroj.get("_id").toString().equals(id)) { //provera da li je socket poslao poruku za ova dva igraca ili neka druga dva
            MojBroj mojBroj = new MojBroj();

                mojBroj.setId(p2mojBroj.get("_id").toString());
                mojBroj.setOpponentId(p2mojBroj.get("_opponentId").toString());
                mojBroj.setNumber(p2mojBroj.get("number").toString());

                player2Result.setText(mojBroj.getNumber());
                timerText.setText("00");
                isTimerRunning = false;

            } else if (p1mojBroj.get("_opponentId").toString().equals(id)){
                MojBroj mojBroj = new MojBroj();
                mojBroj.setId(p1mojBroj.get("_id").toString());
                mojBroj.setOpponentId(p1mojBroj.get("_opponentId").toString());
                mojBroj.setNumber(p1mojBroj.get("number").toString());

                player2Result.setText(mojBroj.getNumber());
                timerText.setText("00");
                isTimerRunning = false;
            }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (counter != 0) {
                        evalResults();
                        restartTimer();
                        nextGame();
                    } else {
                        counter = 1;
                        evalResults();
                        restartTimerRound();
                    }

                }
            });
        });

        HomeFragment.socket.on("stoppedNumberMojBroj", args -> {

            JSONObject obj = (JSONObject) args[0];

            try {
                if (obj.get("_id").toString().equals(id)){

                    int num1 = Integer.parseInt(obj.get("num1").toString());
                    int num2 = Integer.parseInt(obj.get("num2").toString());
                    int num3 = Integer.parseInt(obj.get("num3").toString());
                    int num4 = Integer.parseInt(obj.get("num4").toString());
                    int num5 = Integer.parseInt(obj.get("num5").toString());
                    int num6 = Integer.parseInt(obj.get("num6").toString());
                    int num7 = Integer.parseInt(obj.get("num7").toString());

                    numbers.add(num1);
                    numbers.add(num2);
                    numbers.add(num3);
                    numbers.add(num4);
                    numbers.add(num5);
                    numbers.add(num6);
                    numbers.add(num7);

                    for (int i = 0; i < 7; i++){
                        buttons.get(i).setText(String.valueOf(numbers.get(i)));
                    }

                    setListeners();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
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
                confirmProcedure();
                //if (player je ulogovan){
                if (!iSolved) {
                    sendSocketMessage();
                    evalResults();
                    restartTimer();
                    nextGame();
                }
                //evalResults();
                //restartTimer();
                //nextGame();
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
    }

    private void restartTimer(){
        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(5000, 1000) {
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

    private void restartTimerRound(){

        countDownTimer.cancel();

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerText.setText("00");
                isTimerRunning = false;
                recreate();
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

                //if (nije ulogovan korisnik){
                //    confirmProcedure();
                //}  else{
                confirmProcedure();
                sendSocketMessage();
                //}
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

        iSolved = true;

        /* (if korisnik nije ulogovan){
            double resultNum = Double.parseDouble(result.getText().toString());

        if (resultNum == player1ResultNum){
            Toast.makeText(this, "20 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("20 points");
            totalPoints = 20;
        } else {
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
            player1Points.setText("0 points");
        }

        restartTimer();
        nextGame();
        }*/
    }

    private void evalResults(){

        int opponentPoints = 0;

        double resultNum = Double.parseDouble(result.getText().toString());

        double player2ResultNum = Double.parseDouble(player2Result.getText().toString());

        int myPoints = Integer.parseInt(p1PointsText);

        int totalOpponentPoints = Integer.parseInt(p2PointsText);

        if (resultNum == player1ResultNum){
            Toast.makeText(this, "20 POINTS", Toast.LENGTH_SHORT).show();
            totalPoints = 20;
            myPoints = myPoints + totalPoints;
            String totalPointsStr = myPoints + " points";
            player1Points.setText(totalPointsStr);
            player2Points.setText(totalOpponentPoints + " points");
        } else if (resultNum == player2ResultNum) {
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            opponentPoints = 20;
            totalOpponentPoints = opponentPoints + totalOpponentPoints;
            String totalPointsStr = totalOpponentPoints + " points";
            player2Points.setText(totalPointsStr);
            player1Points.setText(myPoints + " points");
        } else if (Math.abs((resultNum - player2ResultNum)) >= Math.abs((resultNum - player1ResultNum))){
            //ovaj uslov ce biti u zavistnosti cija je runda
            Toast.makeText(this, "5 POINTS", Toast.LENGTH_SHORT).show();
            totalPoints = 5;
            myPoints = myPoints + totalPoints;
            String totalPointsStr = myPoints + " points";
            player1Points.setText(totalPointsStr);
            player2Points.setText(totalOpponentPoints + " points");
        } else if (Math.abs((resultNum - player1ResultNum)) > Math.abs((resultNum - player2ResultNum))){
            Toast.makeText(this, "0 POINTS", Toast.LENGTH_SHORT).show();
            totalPoints = 0;
            opponentPoints = 5;
            totalOpponentPoints = opponentPoints + totalOpponentPoints;
            String totalPointsStr = totalOpponentPoints + " points";
            player2Points.setText(totalPointsStr);
            player1Points.setText(myPoints + " points");
        }

        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("POINTS", String.valueOf(myPoints)).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString("OPPONENT_POINTS", String.valueOf(totalOpponentPoints)).apply();

        //restartTimer();
        //nextGame();
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

    private void nextGame(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
          public void run() {

                Bundle bundle = new Bundle();
                bundle.putInt(
                        "points",
                        totalPoints
                );

                Intent intent = new Intent(GameOneActivity.this, GameThreeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    public void sendSocketMessage(){
        HomeFragment.socket.emit("playerCalculatedNumber", id, opponentId, player1ResultNum);
    }
}

class MojBroj{
    private String id;
    private String opponentId;
    private String number;

    public MojBroj(String id, String opponentId, String number) {
        this.id = id;
        this.opponentId = opponentId;
        this.number = number;
    }

    public MojBroj() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

