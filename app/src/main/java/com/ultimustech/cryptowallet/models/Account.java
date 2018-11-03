package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poacher on 2/1/2018.
 * Firebase Model for account Account
 */

@IgnoreExtraProperties
public class Account {
    public  String accountHash,accountCode,passphrase1,passphrase2, contact;
    public double balance;

    // Default constructor required for class to DataSnapshot.getValues(Account.class)
    public Account(){

    }

    public Account(String accountHash, String  accountCode, String passphrase1, String passphrase2,String contact,double balance){
        this.accountHash = accountHash;
        this.accountCode = accountCode;
        this.passphrase1 =  passphrase1;
        this.passphrase2 = passphrase2;
        this.contact = contact;
        this.balance = balance;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("accountHash",accountHash);
        result.put("accountCode",accountCode);
        result.put("passphrase1",passphrase1);
        result.put("passphrase2",passphrase2);
        result.put("contact",contact);
        result.put("balance",balance);

        return result;
    }
}
