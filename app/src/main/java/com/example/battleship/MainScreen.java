package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        fillStats();
    }

    void fillStats(){
        Intent intent = getIntent();
        final String username = intent.getStringExtra(MainActivity.USER_NAME);

        OkHttpClient client = new OkHttpClient();
        Request request = OkHttpHelper.prepareGetStats(username);

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("in onfailure");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        final String jsonStats = response.body().string();
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
            System.out.println("exception");
        }
    }
}
