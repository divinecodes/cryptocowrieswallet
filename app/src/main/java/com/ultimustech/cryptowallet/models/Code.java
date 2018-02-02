package com.ultimustech.cryptowallet.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Poacher on 2/1/2018.
 * Firebase Model for account Code
 */

@IgnoreExtraProperties
public class Code {
    public String accountHash,accountCode;

    // Default constructor required for class to DataSnapshot.getValues(Code.class)
    public Code(){

    }

    public Code(String accountHash,String  accountCode){
        this.accountHash = accountHash;
        this.accountCode = accountCode;
    }
}
