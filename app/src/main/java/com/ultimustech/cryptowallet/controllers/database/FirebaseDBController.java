package com.ultimustech.cryptowallet.controllers.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ultimustech.cryptowallet.models.Code;

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
        //create new code json tree
        String key = mCodeReference.push().getKey();

        Code code  = new Code(accountHash,accountCode,passphrase1, passphrase2);

        mCodeReference.child("AccountCodes").child(uid).setValue(code);

        return true;
    }
}
