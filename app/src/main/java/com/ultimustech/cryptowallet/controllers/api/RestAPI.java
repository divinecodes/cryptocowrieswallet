package com.ultimustech.cryptowallet.controllers.api;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestAPI {

    private final String HUBTEL_API_PASSWORD="altlgunw";
    private final String HUBTEL_API_USER="ckbmlzsb";
    private String NEXMO_API_KEY = "05c54764";
    private String NEXMO_API_SECRET = "d8341d4f55d014e1";

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

    public void nexmoSMS(String to,String message){
        AuthMethod auth = new TokenAuthMethod(NEXMO_API_KEY,NEXMO_API_SECRET);
        NexmoClient client = new NexmoClient(auth);
        String from = "Cryptowallet";
        System.out.println("CryptoWallet");
        try{
            SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(new TextMessage(from, to, message));
            for(SmsSubmissionResult response: responses){
                Log.i("NEXMO CLIENT",response.toString());
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
