package com.ultimustech.cryptowallet.controllers.api;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestAPI {

    public void callAPI(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CryptoAPI();
            }
        });
    }

    public void CryptoAPI(){
        try{
            URL apiEndpoit = new URL("https://api.github.com/");
            //create connection
            HttpsURLConnection connection = (HttpsURLConnection)apiEndpoit.openConnection();

            //set headers
            connection.setRequestProperty("User-Agent","my-rest-app-v0.1");
            connection.setRequestProperty("Accept","application/vnd.github.v3+json");
            connection.setRequestProperty("Contact-Me","tetteydivine0@gmail.com");

            if(connection.getResponseCode() == 200){
                InputStream responseBody = connection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody,"UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                System.out.println(jsonReader.toString());
                jsonReader.close();
            } else {
                Log.e("REST API","Unable to make connection");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
