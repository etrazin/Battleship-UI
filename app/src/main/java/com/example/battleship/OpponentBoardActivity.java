package com.example.battleship;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.battleship.ServerClasses.PlayResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpponentBoardActivity extends AppCompatActivity
{
    private String _username;
    private String _gameId;
    private TextView _attackShips; //text that declares "attack opponent's ships"
    private GridView _opponentBoardGrid;
    private AdapterBoard _opponentBoard;
    private OkHttpClient _okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_board);

        GetUsernameAndGameId();

        //create game board
        //todo: make board size dynamic andor use integers file from values
        _opponentBoardGrid = (GridView) findViewById(R.id.opponent_board_grid);
        _opponentBoard = new AdapterBoard(this, new ArrayList<Cell>());
        _opponentBoard.addCells(_opponentBoardGrid,  100);

        _attackShips=findViewById(R.id.attackShips);
        _attackShips.setText("Attack opponent's ships by selecting squares on the board!");

        _okHttpClient=new OkHttpClient();
        SetupOnclickListener();
    }

    //sets up on click listener for game board
    //enables keeping track of ship placement
    private void SetupOnclickListener()
    {
        _opponentBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id)
            {
                //when a square is selected ask server if its a hit or miss
                //and change square color accordingly
               /// Gson gson=new Gson();
                Point point=new Point();
                point.point=GridPoint.ConvertPositionToPoint(position);

                //String payload=gson.toJson(point);
                Request request = OkHttpHelper.preparePost(point, "userId", _username, "game", _gameId, "play");

                _okHttpClient.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        switch (response.code())
                        {
                            case 200:
                            {//if its already been hit then don't let it change again
                                String responseBody=response.body().string();
                                Gson gson=new Gson();

                                PlayResponse playResponse= gson.fromJson(responseBody,PlayResponse.class);
                                Cell.Status status;
                                if(playResponse.getHit())
                                {
                                    status=Cell.Status.HIT;
                                    OpponentBoardActivity.this.runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run() {
                                            toastString("Hit!");}

                                    });
                                }
                                else
                                {
                                    status=Cell.Status.MISSED;
                                    OpponentBoardActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            toastString("Miss!");
                                        }
                                    });
                                }
                                SetSquareToHitOrMiss(parent,position,status);
                                break;
                            }
                            case 403:
                            {
                                OpponentBoardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toastString("Invalid!");
                                    }
                                });
                                break;
                            }
                            case 500:
                            {
                                OpponentBoardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toastString("Server error!");
                                    }
                                });
                                break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        OpponentBoardActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("bad request!");
                            }
                        });
                    }
                });



            }
        });
    }

    private void SetSquareToHitOrMiss(AdapterView<?> parent, int position, Cell.Status cellStatus) {

            OpponentBoardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _opponentBoard.notifyDataSetChanged();
                }
            });
            Cell cell = (Cell) parent.getAdapter().getItem(position);
            if(cell.getStatus()== Cell.Status.VACANT)
            {
                cell.setStatus(cellStatus);
            }
    }

    private void GetUsernameAndGameId()
    {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        _gameId = extras.getString("GAME_ID");
        _username = extras.getString("USER_NAME");
    }

    public void ShowMyBoard(View view) {
        Intent i = new Intent(this, MyBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    private void toastString(String toastContent)
    {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,toastContent , duration);
        toast.show();
    }
}
