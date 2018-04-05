package com.ultimustech.cryptowallet.controllers.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ultimustech.cryptowallet.models.Account;

/**
 * Created by Poacher on 2/1/2018.
 */

public class FirebaseDBController {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mTransactionReference;
    private DatabaseReference mCodeReference;
    private DatabaseReference mCurrencyRef;

    /**
     * function to upload user account code
     * @param   accountCode,accountHash,uid,passphrase1,passphrase2
     * @return uploaded
     */
    public boolean uploadAccountCode(String accountCode, String accountHash,String uid,String passphrase1, String passphrase2){

        mCodeReference = FirebaseDatabase.getInstance().getReference();
        //create new account json tree
        String key = mCodeReference.push().getKey();

        Account account = new Account(accountHash,accountCode,passphrase1, passphrase2);

        mCodeReference.child("Accounts").child(uid).setValue(account);

        return true;
    }

}
