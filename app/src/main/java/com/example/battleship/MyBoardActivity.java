package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyBoardActivity extends AppCompatActivity {

    //todo: part of settings should include explanation on battleship rules/gameplay
    private static final int MAX_SHIPS_NUMBER= 3;
    private AdapterBoard _myBoard;  //java class representing board
    private GridView _myBoardGrid;  //board view - to interact with xml object
    private GridPoint _currentlySelectedSquare;  //square most recently selected by user
    private TextView _shipsPlacement; //text that declares "place your ships"
    private TextView _instructions; //instructions for user. keeps track of which ship he's up to
    private ArrayList<Integer> _selectedSquares; //list of squares already selected by user
    private int _squareNumber; //1 or 2. number of square in ship currently being placed
    private int _shipsNumber;  //1->8. number of ship currently being placed
    private ArrayList<GridPoint> _shipsPoints; //array which contains the coordinates of all the ships.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_board);

        //set text to place ships
        _shipsPlacement = findViewById(R.id.placeShips);
        _shipsPlacement.setText("Tap squares to place your ships.");

        //set text to place first square of first ship
        _shipsNumber=1;
        _squareNumber=1;
        _instructions=findViewById(R.id.instructions);
        _instructions.setText("Select square "+_squareNumber+" of ship "+_shipsNumber+".");

        //create game board
        //todo: make board size dynamic andor use integers file from values
        _myBoardGrid = (GridView) findViewById(R.id.my_board_grid);
        _myBoard = new AdapterBoard(this, new ArrayList<Cell>());
        _myBoard.addCells(_myBoardGrid, 1, 100);

        //setup onclick listener for game board
        SetupOnclickListener();

        //initialize currently selected square (to impossible square)
        _currentlySelectedSquare=new GridPoint(-1,-1);

        //initialize list of selected squares
        _selectedSquares=new ArrayList<Integer>();

        //initialize ships coordinates
        _shipsPoints=new ArrayList<GridPoint>();
    }


    //switch to opponent's board activity
    public void ShowOpponentBoard(android.view.View view)
    {
        Intent i = new Intent(this, OpponentBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    //sets up on click listener for game board
    //enables keeping track of ship placement
    private void SetupOnclickListener()
    {
        _myBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (_shipsNumber <= MAX_SHIPS_NUMBER)
                {
                    if (_selectedSquares.contains(position))
                    {
                        //toast that square already selected
                    }
                    else
                        {
                        if (_squareNumber == 1)
                        {
                            //change color to occupied
                            SetSquareToOccupied(parent, position);

                            _selectedSquares.add(position);
                            _squareNumber = 2;
                            GridPoint thisSquare=GridPoint.ConvertPositionToPoint(position);
                            _currentlySelectedSquare = thisSquare;
                            _shipsPoints.add(thisSquare);
                            _instructions.setText("Select square " + _squareNumber + " of ship " + _shipsNumber + ".");
                            //todo: (maybe overkill) change color of adjacent squares

                        } else
                            {
                            if (_squareNumber == 2)
                            {

                                GridPoint thisSquare = GridPoint.ConvertPositionToPoint(position);
                                if (_currentlySelectedSquare.adjacent(thisSquare))
                                {
                                    SetSquareToOccupied(parent, position);
                                    _selectedSquares.add(position);
                                    _squareNumber = 1;
                                    _currentlySelectedSquare = thisSquare;
                                    _shipsPoints.add(thisSquare);
                                    if (_shipsNumber == MAX_SHIPS_NUMBER) {
                                        _instructions.setText("Ships placement completed.");
                                    }
                                    else
                                    {
                                        _shipsNumber++;
                                        _instructions.setText("Select square " + _squareNumber + " of ship " + _shipsNumber + ".");
                                    }
                                }
                                else {
                                    //Toast "select adjacent to first square"
                                }
                            }
                        }


                    }
                }
            }
        });
    }

    private void SetSquareToOccupied(AdapterView<?> parent, int position)
    {
        _myBoard.notifyDataSetChanged();
        Cell cell = (Cell) parent.getAdapter().getItem(position);
        cell.setStatus(Cell.Status.OCCUPIED);
    }

    //sends ships info to server
    public void commitShips(View view)
    {

    }




}
