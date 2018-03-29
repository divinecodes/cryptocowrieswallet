package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Poacher on 1/31/2018.
 * Firebase Model for Transaction
 */

@IgnoreExtraProperties
public class Transaction {
    public String type, account, transactionId;
    public float  amount;
    public long transactionDate;

    //Default constructor required for class to DataSnapshot.getValues(Transaction.class)
    public Transaction(){

    }

    public Transaction(String type, String account,String transactionId, float amount, long transactionDate){
        this.type = type;
        this.account = account;
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }
}
