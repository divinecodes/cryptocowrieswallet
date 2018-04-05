package com.ultimustech.cryptowallet.controllers.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.app.Activity;

public class DefaultHelpers {
    private boolean isHelping;
    public boolean isOnline(Context context){


        isHelping = false;

        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //NetworkInfo activeNetWorkInfo = connectivityManager.getActiveNetworkInfo();

//            if(activeNetWorkInfo != null && activeNetWorkInfo.isConnected()) isHelping = true;

            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                isHelping = true;
            }
        } catch (NullPointerException e){

            Log.e("CONNECTIVITY", e.toString());
        }
        return isHelping;
    }
}
