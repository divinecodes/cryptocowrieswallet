package com.ultimustech.cryptowallet.controllers.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.models.Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poacher on 2/1/2018.
 */

public class FirebaseDBController {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mTransactionReference;
    private DatabaseReference databaseReference;
    private DatabaseReference mAccountRef;

    private String details;

    /**
     * method to upload user account details
     * @param   accountCode,accountHash,uid,passphrase1,passphrase2,contact,balance
     * @return uploaded
     */
    public boolean uploadAccountCode(String accountCode, String accountHash,String uid,String passphrase1,
                                     String passphrase2,String contact,double balance){
        firebaseDatabase = FirebaseDatabase.getInstance();

        mAccountRef = firebaseDatabase.getReference("accounts");

        //create new account json tree
        String key = mAccountRef.child("accounts").push().getKey();
        Account account = new Account(accountHash,accountCode,passphrase1, passphrase2,contact,balance);
        Map<String, Object> accountDetails = account.toMap();

        Map<String, Object> accountUpdates = new HashMap<>();
        accountUpdates.put(uid, accountDetails);

        mAccountRef.updateChildren(accountUpdates);

        return true;
    }

    public boolean updateAccountDetails(String passphrase1, String passphrase2, String contact, String uid){
        firebaseDatabase =FirebaseDatabase.getInstance();
        mAccountRef = firebaseDatabase.getReference("accounts").child(uid);

        Map<String, Object> accountUpdates = new HashMap<>();
        accountUpdates.put("passphrase1",passphrase1);
        accountUpdates.put("passphrase2",passphrase2);
        accountUpdates.put("contact",contact);

        mAccountRef.updateChildren(accountUpdates);
        return true;
    }

    public String getUserDetails(FirebaseUser user){
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAccountRef = firebaseDatabase.getReference().child("accounts").child(user.getUid());

        //attach a listener to read the data
        mAccountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                if(account != null){
                    details = account.accountCode+":"+account.accountHash;
                } else {
                    details = "test:test";
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FIREBASEDB","The read failed: "+databaseError.getCode());
            }
        });
        return details;
    }


    /**
     * getter and setter methods
     */
    public void setDetails(String details){
        this.details = details;
    }

    public String getDetails(){
        return this.details;
    }

}
