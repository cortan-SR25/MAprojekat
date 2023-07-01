package com.example.slagalica_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourFriendsActivity extends AppCompatActivity {

    ListView myListView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_friends);

        searchView = findViewById(R.id.search_bar);
        myListView = (ListView) findViewById(R.id.listview2);

        final ArrayList<String> myArrayList = new ArrayList<>();
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(YourFriendsActivity.this, android.R.layout.simple_list_item_1, myArrayList);
        myListView.setAdapter(myArrayAdapter);



        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference drugiRef = mRef.child("users");


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                   String value = dataSnapshot.getValue(String.class);
                    myArrayList.add(dataSnapshot.child("username").getValue().toString());
                }
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        drugiRef.addListenerForSingleValueEvent(valueEventListener);


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = myListView.getAdapter().getItem(position).toString();
                Intent intent = new Intent(getApplicationContext(), FriendProfileActivity.class);
                intent.putExtra("name", str);
                intent.putExtra("username", str);
                intent.putExtra("email", str);

                startActivity(intent);

            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myArrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}