package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GameplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String gameID = extras.getString("GAME_ID");
        String username = extras.getString("USER_NAME");
        TextView temp = (TextView) findViewById(R.id.temp);
        temp.setText("hi " + username + ", your game ID is: " + gameID);
    }
}
