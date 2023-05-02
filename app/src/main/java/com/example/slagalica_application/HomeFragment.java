package com.example.slagalica_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    View inflatedView;
    CardView gameOneCard;
    CardView gameTwoCard;
    CardView gameFourCard;
    CardView gameFiveCard;
    CardView gameSixCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        gameOneCard = inflatedView.findViewById(R.id.gameOne);
        gameOneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameOneActivity.class);
                startActivity(intent);
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
}