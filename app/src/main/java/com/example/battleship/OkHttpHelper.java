package com.example.battleship;

import android.net.Uri;

import com.google.gson.Gson;

import javax.net.ssl.HostnameVerifier;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
//wrapper on top of okhttp library to simplify REST requests
public class OkHttpHelper {

    //Define here the host. (localhost:8080 / 10.0.2.2:8080 / ngrok url)
    public static final String HOST_AND_PORT = "10.0.2.2:8080"; //todo enter url dynamically?read from config file?

    public static Request preparePost(Object payLoad, String queryKey, String queryValue, String ... path ){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(HOST_AND_PORT);
        for(String pathParameter : path) builder.appendPath(pathParameter);
        if(queryKey != null) builder.appendQueryParameter(queryKey, queryValue);

        String url = builder.build().toString();


        Gson gson=new Gson();
        String content=gson.toJson(payLoad);



        MediaType PLAIN=MediaType.parse("text/plain");

        RequestBody body = RequestBody.create(PLAIN, content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }
/*
    //Prepares a Post request for loging or register a new user
    public static Request preparePost(User user, String ... path){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(HOST_AND_PORT);
        for(String pathParameter : path) builder.appendPath(pathParameter);

        String url = builder.build().toString();

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

    /*
    public static Request preparePost(String username, ShipPlacement[] shipPlacements, String ... path){

    }

    public static Request preparePost(String username, GridPoint gridPoint, String ... path){

    }
    */

    //Prepares a Get request for: getStats, getStatus, newGame
    public static Request prepareGet(String username, String ... path){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(HOST_AND_PORT);
        for(String pathParameter : path) builder.appendPath(pathParameter);
        builder.appendQueryParameter("userId", username);

        String url = builder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return request;
    }

    //Prepares a Get request for joinGame
    public static Request prepareGet(String[]queries, String ... path){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(HOST_AND_PORT);
        for(String pathParameter : path) builder.appendPath(pathParameter);
        builder.appendQueryParameter("userId", queries[0]);
        builder.appendQueryParameter("gameId", queries[1]);

        String url = builder.build().toString();

        System.out.println("the url from builder with username and gameId: " + url);

        //String url = HOST_AND_PORT + path + username;
        Request request = new Request.Builder()
                .url(url)
                .build();

        return request;
    }

}