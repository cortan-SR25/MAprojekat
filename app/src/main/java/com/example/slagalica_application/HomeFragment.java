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
    CardView gameFourCard;
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

        gameSixCard = inflatedView.findViewById(R.id.gameSix);
        gameSixCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameSixActivity.class);
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
        return inflatedView;

    }
}