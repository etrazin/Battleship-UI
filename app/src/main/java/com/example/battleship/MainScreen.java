package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    public static final String GAME_ID = "";
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
                            System.out.println("in onFailure at fillStats");
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
            System.out.println("exception while trying to get user stats");
        }
    }


    public void startNewGame(android.view.View view){
        request = OkHttpHelper.prepareGet(username,"game", "new");
        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("in onFailure at startNewGame");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        final String gameID = response.body().string();
                        MainScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchToGamePlayScreen(gameID);
                            }
                        });
                    }
                }
            });
        }catch(Exception e){
            System.out.println("exception while trying to get a new game ID");
        }
    }


    public void joinExistingGame(View view) {

        EditText gameIdEditText = (EditText) findViewById(R.id.game_id);
        String gameID = gameIdEditText.getText().toString();

        String[] queries = {username, gameID};
        request = OkHttpHelper.prepareGet(queries, "game", "join");

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("in onFailure at joinExistingGame");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String joinGameResponse = response.body().string();
                    System.out.println("response for trying to join an existing game: " + joinGameResponse);
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString(joinGameResponse);
                            if(response.isSuccessful())switchToGamePlayScreen(joinGameResponse);
                            ;
                        }
                    });
                }
            });
        }catch (Exception e){
            System.out.println("Exception while trying to join an existing game");
        }
    }

    private void toastString(String toastContent)
    {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,toastContent , duration);
        toast.show();
    }

    void switchToGamePlayScreen(String gameID){
        Intent intent = new Intent(this, GameplayActivity.class);
        intent.putExtra(GAME_ID, gameID);
        startActivity(intent);
    }
}
