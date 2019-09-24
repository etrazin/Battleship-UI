package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    static final int STATUS_REQUEST_INTERVAL = 10000;

    String username;
    OkHttpClient client;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Get username from auth screen
        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USER_NAME);

        client = new OkHttpClient();
        fillStats();
    }

    //Fills the player's statistics
    void fillStats(){
        request = OkHttpHelper.prepareGet(username, "user", "stats");

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString("Request for user stats failed");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        String jsonStats = response.body().string();
                        Gson gson = new Gson();
                        final UserStats userStats = gson.fromJson(jsonStats, UserStats.class);

                        MainScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView hiTextView = (TextView) findViewById(R.id.hi_title);
                                hiTextView.append(username);

                                TextView winsTextView = (TextView) findViewById(R.id.wins);
                                winsTextView.append(""+userStats.getWins());

                                TextView lossesTextView = (TextView) findViewById(R.id.losses);
                                lossesTextView.append(""+userStats.getLosses());

                                TextView accuracyTextView = (TextView) findViewById(R.id.accuracy);
                                accuracyTextView.append(""+userStats.getAccuracy());
                            }
                        });
                    }

                }
            });
        }catch (Exception e){
            toastString("Request for user stats failed");
        }
    }

    //handles a click of the 'start a new game' button
    public void startNewGame(android.view.View view){
        request = OkHttpHelper.prepareGet(username,"game", "new");
        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString("Failed to create a new game");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        final String gameID = response.body().string();
                        System.out.println("The game ID is: " + gameID); //todo: delete this row
                        MainScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                giveGameIdAndAskToWait(gameID);
                                waitForSecondPlayer(gameID);
                            }
                        });
                    }
                }
            });
        }catch(Exception e){
            toastString("Failed to create a new game");
        }
    }

    //handles a click of the 'join existing game' button
    public void joinExistingGame(View view) {

        EditText gameIdEditText = (EditText) findViewById(R.id.game_id);
        final String gameID = gameIdEditText.getText().toString();

        String[] queries = {username, gameID};
        request = OkHttpHelper.prepareGet(queries, "game", "join");

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString("Failed to join an existing game");
                        }
                    });
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    final String joinGameResponse = response.body().string();

                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString(joinGameResponse);
                            if(response.isSuccessful())switchToMyBoard(gameID);
                        }
                    });
                }
            });
        }catch (Exception e){
            toastString("Failed to join a new game");;
        }
    }

    private void toastString(String toastContent)
    {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,toastContent , duration);
        toast.show();
    }

    //Show the new game ID and ask the user to wait for a second player
    void giveGameIdAndAskToWait(String gameID){

        EditText gameIdEditText = (EditText) findViewById(R.id.game_id);
        Button joinGameButton = (Button) findViewById(R.id.join_existing_game);
        Button startnewGame = (Button) findViewById(R.id.start_new_game);
        TextView yourGameId = (TextView) findViewById(R.id.your_game_id);
        TextView newGameId = (TextView) findViewById(R.id.new_game_id);
        TextView waitingForPlayer = (TextView) findViewById(R.id.waiting_for_player);
        Button backToMainScreen = (Button) findViewById(R.id.back_to_main_screen);

        gameIdEditText.setVisibility(View.GONE);
        joinGameButton.setVisibility(View.GONE);
        startnewGame.setVisibility(View.GONE);
        yourGameId.setVisibility(View.VISIBLE);
        newGameId.setVisibility(View.VISIBLE);
        waitingForPlayer.setVisibility(View.VISIBLE);
        backToMainScreen.setVisibility(View.VISIBLE);

        yourGameId.setText("Your game ID is:");
        newGameId.setText(gameID);
        waitingForPlayer.setText("Waiting for another player ...");
    }

    //Keep checking if a second player joined - by checking the game state
    void waitForSecondPlayer(final String gameID){

        request = OkHttpHelper.prepareGet(username, "game", gameID, "status");
        final JsonParser parser = new JsonParser();

        final Timer t = new Timer( );
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    JsonObject jsonObj = parser.parse(response.body().string()).getAsJsonObject();
                    String gameState = jsonObj.get("state").getAsString();

                    //When a second player join, the game state changes from 'WAITING' to 'PLACEMENT'
                    if(gameState.equals("PLACEMENT")){
                        t.cancel();
                        switchToMyBoard(gameID);
                    }
                }catch (Exception e){
                    toastString("Failed to find a second player");
                }
            }
        }, 0,STATUS_REQUEST_INTERVAL);
    }

    //Move to the game screen
    void switchToMyBoard(String gameID){
        Intent intent = new Intent(this, MyBoardActivity.class);
        Bundle extras = new Bundle();
        extras.putString("GAME_ID", gameID);
        extras.putString("USER_NAME", username);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void setBackVisibility(View view) {
        EditText gameIdEditText = (EditText) findViewById(R.id.game_id);
        Button joinGameButton = (Button) findViewById(R.id.join_existing_game);
        Button startnewGame = (Button) findViewById(R.id.start_new_game);
        TextView yourGameId = (TextView) findViewById(R.id.your_game_id);
        TextView newGameId = (TextView) findViewById(R.id.new_game_id);
        TextView waitingForPlayer = (TextView) findViewById(R.id.waiting_for_player);
        Button backToMainScreen = (Button) findViewById(R.id.back_to_main_screen);

        gameIdEditText.setVisibility(View.VISIBLE);
        joinGameButton.setVisibility(View.VISIBLE);
        startnewGame.setVisibility(View.VISIBLE);
        yourGameId.setVisibility(View.INVISIBLE);
        newGameId.setVisibility(View.INVISIBLE);
        waitingForPlayer.setVisibility(View.INVISIBLE);
        backToMainScreen.setVisibility(View.INVISIBLE);
    }
}
