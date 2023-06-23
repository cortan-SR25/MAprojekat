package com.example.slagalica_application;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.socket.client.Socket;


public class HomeFragment extends Fragment {

    View inflatedView;
    CardView gameOneCard;
    CardView gameTwoCard;
    CardView gameThreeCard;
    CardView gameFourCard;
    CardView gameFiveCard;
    CardView gameSixCard;

    static Socket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        String id = PreferenceManager.getDefaultSharedPreferences(getContext()).
                getString("ID", null);

        gameOneCard = inflatedView.findViewById(R.id.gameOne);

        SocketHandler.setSocket();

        socket = SocketHandler.getSocket();
        try {
            socket.connect();
        } catch (Exception e){
            System.out.println("ERROR " + e);
        }

        socket.on("startGame", args -> {
           if (args[0] != null){
               JSONArray playerIDs = (JSONArray) args[0];
               for (int i = 0; i < playerIDs.length(); i++){
                   try {
                       if (!playerIDs.get(i).toString().equals(id)){
                           PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                                   putString("OPPONENT_ID", playerIDs.get(i).toString()).apply();
                       }
                   } catch (JSONException e) {
                       throw new RuntimeException(e);
                   }
               }
               /*dva igraca su spremna i ova dva ID smestiti u bazu da bi tokom igre korisnici
                 odnosno aplikacija znala koja dva igraca igraju */
               Intent intent = new Intent(getActivity(), GameOneActivity.class);
               startActivity(intent);
           }
        });

        gameOneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.emit("playerReady", id);
                //Intent intent = new Intent(getActivity(), GameOneActivity.class);
                //startActivity(intent);
            }
        });

        gameTwoCard = inflatedView.findViewById(R.id.gameTwo);
        gameTwoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameTwoActivity.class);
                startActivity(intent);
            }
        });

        gameThreeCard = inflatedView.findViewById(R.id.gameThree);
        gameThreeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameThreeActivity.class);
                startActivity(intent);
            }
        });

        gameFourCard = inflatedView.findViewById(R.id.gameFour);
        gameFourCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameFourActivity.class);
                startActivity(intent);
            }
        });

        gameFiveCard = inflatedView.findViewById(R.id.gameFive);
        gameFiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameFiveActivity.class);
                startActivity(intent);
            }
        });

        gameSixCard = inflatedView.findViewById(R.id.gameSix);
        gameSixCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameSixActivity.class);
                startActivity(intent);
            }
        });


        return inflatedView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //socket.close();
    }
}