package com.example.slagalica_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

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
        gameOneCard = inflatedView.findViewById(R.id.gameOne);

        SocketHandler.setSocket();

        socket = SocketHandler.getSocket();
        socket.connect();

        socket.on("startGame", args -> {
           if (args[0] != null){
               JSONArray playerIDs = (JSONArray) args[0];
               /*dva igraca su spremna i ova dva ID smestiti u bazu da bi tokom igre korisnici
                 odnosno aplikacija znala koja dva igraca igraju */
               Intent intent = new Intent(getActivity(), GameOneActivity.class);
               startActivity(intent);
           }
        });

        gameOneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.emit("playerReady", "1");
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
        socket.close();
    }
}