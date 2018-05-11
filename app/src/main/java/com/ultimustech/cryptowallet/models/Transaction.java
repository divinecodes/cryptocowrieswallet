package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poacher on 1/31/2018.
 * Firebase Model for Transaction
 */

@IgnoreExtraProperties
public class Transaction {
    public String type, sender, receiver;
    public double amount;
    public long timestamp;

    //Default constructor required for class to DataSnapshot.getValues(Transaction.class)
    public Transaction(){

    }

    public Transaction(String type, String sender,String receiver,double amount, long timestamp){
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("type",type);
        result.put("sender",sender);
        result.put("receiver",receiver);
        result.put("amount",amount);
        result.put("timestamp",timestamp);

        return result;
    }
}
