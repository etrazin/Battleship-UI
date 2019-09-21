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
        String gameID = intent.getStringExtra(MainScreen.GAME_ID);
        TextView temp = (TextView) findViewById(R.id.temp);
        temp.append(gameID);
    }
}
