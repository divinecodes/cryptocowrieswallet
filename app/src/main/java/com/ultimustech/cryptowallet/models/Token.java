package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Token {
    public String token;

    //Default constructor required for class to DataSnapshot.getValues(Transaction.class)
    public Token(){

    }

    public Token(String token){
        this.token = token;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("token",token);
        return result;
    }
}
