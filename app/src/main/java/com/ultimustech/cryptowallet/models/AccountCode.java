package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class AccountCode {
    public  String accountHash;

    // Default constructor required for class to DataSnapshot.getValues(Account.class)
    public AccountCode(){

    }

    public AccountCode(String accountHash){
        this.accountHash = accountHash;

    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("accountHash",accountHash);

        return result;
    }
}
