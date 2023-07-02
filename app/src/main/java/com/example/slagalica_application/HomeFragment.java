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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.socket.client.Socket;


public class HomeFragment extends Fragment {

    View inflatedView;
    CardView startGameCard;

    Button cancelButton;

    static Socket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        String id = PreferenceManager.getDefaultSharedPreferences(getContext()).
                getString("ID", null);

        startGameCard = inflatedView.findViewById(R.id.startGame);
        cancelButton = inflatedView.findViewById(R.id.cancelBtn);
        cancelButton.setVisibility(View.INVISIBLE);

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

                           if (i == 0){
                               PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                                       putString("PRIORITY", "2").apply();
                           } else {
                               PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                                       putString("PRIORITY", "1").apply();
                           }

                           PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                                   putString("POINTS", "0").apply();
                           PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                                   putString("OPPONENT_POINTS", "0").apply();

                       }
                   } catch (JSONException e) {
                       throw new RuntimeException(e);
                   }
               }
               Intent intent = new Intent(getActivity(), GameOneActivity.class);
               startActivity(intent);
           }
        });

        startGameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.emit("playerReady", id);
                startGameCard.setEnabled(false);
                cancelButton.setVisibility(View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.emit("cancelGame", id);
                startGameCard.setEnabled(true);
                cancelButton.setVisibility(View.INVISIBLE);
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