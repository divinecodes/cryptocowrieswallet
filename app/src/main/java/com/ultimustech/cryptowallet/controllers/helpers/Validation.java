package com.ultimustech.cryptowallet.controllers.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.app.Activity;

import com.google.android.gms.flags.impl.DataUtils;

public class Validation {
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

    /**
     * validation to see if the input is a number
     * @param input
     * @return boolean true/false
     */
    public static boolean validNumber(String input){
        if(input.isEmpty()){
            return false;
        }

        double val = Double.parseDouble(input);
        if(val < 0.0){
            return false;
        }

        //Too Expensive
//        try {
//            double d = Double.parseDouble(input);
//        } catch (NumberFormatException nfe){
//            return false;
//        }

//        if(!input.matches("-?\\\\d+(\\\\.\\\\d+)?")){
//            return false;
//        }

        return true;
    }

    /**
     * check for the validity of the credit card pin
     * @param input
     * @return
     */
    public static boolean isCorrectPin(String input){
        if(input.isEmpty()){
            return false;
        }

        if(input.length() > 4){
            return false;
        }
        try{
            int val = Integer.parseInt(input);
            if(val < 0){
                return false;
            }
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
