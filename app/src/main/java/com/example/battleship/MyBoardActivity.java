package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ComplexColorCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class MyBoardActivity extends AppCompatActivity {
    AdapterBoard _myBoard;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_board);

        GridView myBoardGrid = (GridView) findViewById(R.id.my_board_grid);
        _myBoard = new AdapterBoard(this, new ArrayList<Cell>());
        _myBoard.addCells(myBoardGrid, 1, 100);
    }

    public void ShowOpponentBoard(android.view.View view)
    {
        Intent i = new Intent(this, OpponentBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);

    }
}
