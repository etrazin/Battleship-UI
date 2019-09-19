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
        String username=GetFieldText(R.id.email);
        String password=GetFieldText(R.id.password);

        ToastIfFieldIsEmptyOrWhiteSpace(username,password);

        User user=new User(username,password);

        OkHttpClient client = new OkHttpClient();
        Request request = OkHttpHelper.PreparePost(user,"http://ef162c8e.ngrok.io/user/register");//todo enter url dynamically?read from config file?

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
                            ToastString("Bad request!");
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
                                ToastString("Registration succeeded!");
                                EmptyActivity e=new EmptyActivity();
                                SwitchActivity(e);
                            }
                        });
                    }
                    if(response.code()==403)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastString("User already exists!");
                            }
                        });
                    }
                    if(response.code()==500)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastString("Server error!");
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex)
        {

        }
    }

    //handles a click of the login button
    //sends email and password to server for login
    //if login successful -> move to "choose game" screen
    public void login(android.view.View view)
    {
        //get content of username and password fields
        String username=GetFieldText(R.id.email);
        String password=GetFieldText(R.id.password);

        ToastIfFieldIsEmptyOrWhiteSpace(username,password);
        //ToastIfBadFieldFormat(emailAddress,password);

        User user=new User(username,password);


        OkHttpClient client = new OkHttpClient();
        Request request = OkHttpHelper.PreparePost(user,"http://ef162c8e.ngrok.io/user/login");//todo enter url dynamically?read from config file?

        try
        {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastString("Bad request!");
                }

                @Override
                public void onResponse(Call call, Response response){
                    if(response.code()==200)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastString("Login succeeded!");
                                EmptyActivity e=new EmptyActivity();
                                SwitchActivity(e);
                            }
                        });
                    }
                    if(response.code()==403)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastString("Login failed!");
                            }
                        });                    }
                    if(response.code()==500)
                    {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastString("Server error!");
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex)
        {

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
            ToastString("email address too short");
        }
        if(password.length()<8)
        {
            ToastString("password too short");
        }
    }

    private void ToastIfFieldIsEmptyOrWhiteSpace(String emailAddress, String password)
    {
        if(emailAddress.isEmpty()||emailAddress.trim().length()==0)
        {
            ToastString("enter email address");
        }

        if(password.isEmpty()||emailAddress.trim().length()==0)
        {
            ToastString("enter password");
        }
    }

    private void ToastString(String toastContent)
    {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,toastContent , duration);
        toast.show();

    }

    void SwitchActivity(Activity newActivity)
    {
        Intent intent=new Intent(this,newActivity.getClass());
        startActivity(intent);
    }
}

