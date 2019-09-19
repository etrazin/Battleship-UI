package com.example.battleship;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

//wrapper on top of okhttp library to simplify REST requests
public class OkHttpHelper {

    // HTTP POST request helper
    public static Request PreparePost(User user,String url)
    {
        //todo: make work for any object not just user
        Gson gson=new Gson();
        String content=gson.toJson(user);

        MediaType PLAIN=MediaType.parse("text/plain");

        RequestBody body = RequestBody.create(PLAIN, content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }
}