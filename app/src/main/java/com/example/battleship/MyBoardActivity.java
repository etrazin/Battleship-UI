package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MyBoardActivity extends AppCompatActivity {
    //private Activity _opponentBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_board);
    }

    public void ShowOpponentBoard(android.view.View view)
    {
        Intent i = new Intent(this, OpponentBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);

    }
}
