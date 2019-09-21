package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //handles a click of the register button
    //sends email and password to server for verification
    //if registration succcessful -> move to "choose game" screen
    public void register(android.view.View view)
    {

        //get content of username and password fields
        final String username=GetFieldText(R.id.user_name);
        String password=GetFieldText(R.id.password);

        ToastIfFieldIsEmptyOrWhiteSpace(username,password);

        User user=new User(username,password);

        OkHttpClient client = new OkHttpClient();
        Request request = OkHttpHelper.preparePost(user,"user", "register");

        try
        {
            client.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString("Bad request!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response)
                {
                    if(response.code()==200)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Registration succeeded!");
                                switchToMainScreen(username);
                            }
                        });
                    }
                    else if(response.code()==403)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("User already exists!");
                                switchToMainScreen(username);
                            }
                        });
                    }
                    else if(response.code()==500)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Server error!");
                            }
                        });
                    }
                    else
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Bad request!");
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex)
        {
            toastString("Exception");

        }
    }

    //handles a click of the login button
    //sends email and password to server for login
    //if login successful -> move to "choose game" screen
    public void login(android.view.View view)
    {
        //get content of username and password fields
        final String username=GetFieldText(R.id.user_name);
        String password=GetFieldText(R.id.password);

        ToastIfFieldIsEmptyOrWhiteSpace(username,password);
        //ToastIfBadFieldFormat(emailAddress,password);

        User user=new User(username,password);


        OkHttpClient client = new OkHttpClient();
        Request request = OkHttpHelper.preparePost(user,"user", "login");//todo enter url dynamically?read from config file?

        try
        {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastString("bad request!");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response){
                    if(response.code()==200)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Login succeeded!");
                                switchToMainScreen(username);
                            }
                        });
                    }
                    else if(response.code()==403)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Login failed!");
                            }
                        });                    }
                    else if(response.code()==500)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toastString("Server error!");
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex)
        {
            toastString("Exception");
        }
    }

    //gets text contents from field by field Id
    private String GetFieldText(int fieldId)
    {
        EditText field = findViewById(fieldId);
        return field.getText().toString();
    }

    private void ToastIfBadFieldFormat(String emailAddress, String password)
    {
        if(emailAddress.length()<8)
        {
            toastString("email address too short");
        }
        if(password.length()<8)
        {
            toastString("password too short");
        }
    }

    private void ToastIfFieldIsEmptyOrWhiteSpace(String userName, String password)
    {
        if(userName.isEmpty()||userName.trim().length()==0)
        {
            toastString("enter email address");
        }

        if(password.isEmpty()||userName.trim().length()==0)
        {
            toastString("enter password");
        }
    }

    private void toastString(String toastContent)
    {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,toastContent , duration);
        toast.show();
    }

    void switchToMainScreen(String username){
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra(USER_NAME, username);
        startActivity(intent);
    }
}

