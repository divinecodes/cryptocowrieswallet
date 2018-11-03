package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Loyalty {
    public String token;
    public double value;
    public boolean used;

    //Default constructor required for class to DataSnapshot.getValues(Transaction.class)
    public Loyalty(){

    }

    public Loyalty(String token, double value,boolean used){
        this.token = token;
        this.value = value;
        this.used = used;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("token",token);
        result.put("value", value);
        result.put("used", used);
        return result;
    }
}
