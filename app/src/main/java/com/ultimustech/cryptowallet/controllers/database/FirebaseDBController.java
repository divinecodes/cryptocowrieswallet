package com.ultimustech.cryptowallet.controllers.database;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.models.Account;
import com.ultimustech.cryptowallet.models.Loyalty;
import com.ultimustech.cryptowallet.models.Transaction;

import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Poacher on 2/1/2018.
 */

public class FirebaseDBController {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mTransactionRef;
    private DatabaseReference databaseReference;
    private DatabaseReference mAccountRef;
    private DatabaseReference tokensRef;
    private DatabaseReference loyaltyRef;
    private ValueEventListener accountsListener;
    private String details;

    private Account account;
    private String address = null;

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

    public boolean updateNotificationToken(String token, String uid){
        firebaseDatabase =FirebaseDatabase.getInstance();
        tokensRef = firebaseDatabase.getReference("tokens").child(uid).child("notificationTokens");

        Map<String, Object> tokensUpdates = new HashMap<>();
        tokensUpdates.put("token",token);

        tokensRef.updateChildren(tokensUpdates);
        return true;
    }

    public boolean gaveLoyaltyToken(Loyalty loyalty, String uid){
        firebaseDatabase = FirebaseDatabase.getInstance();
        loyaltyRef = firebaseDatabase.getReference("loyalty");

        loyaltyRef.child(uid).setValue(loyalty);
        return true;
    }

    public boolean updateLoyatyToken(String uid){
        firebaseDatabase = FirebaseDatabase.getInstance();
        mTransactionRef = firebaseDatabase.getReference().child("loyalty").child(uid);

        Map<String, Object> loyatyUpdates = new HashMap<>();
        loyatyUpdates.put("used",false);

        mAccountRef.updateChildren(loyatyUpdates);
        return false;
    }

    public boolean checkLoyalty(String uid){
        firebaseDatabase = FirebaseDatabase.getInstance();
        loyaltyRef = firebaseDatabase.getReference("loyalty").child(uid);

        if(loyaltyRef == null){
            return false;
        }


        return true;
    }

    public boolean uploadTransactions(Transaction transaction ){
        firebaseDatabase =FirebaseDatabase.getInstance();
        mTransactionRef = firebaseDatabase.getReference("transactions").child(transaction.sender);

        String key = mTransactionRef.child("transactions").push().getKey();

        mTransactionRef.child(key).setValue(transaction);

        //update the other users transaction tree
        String address = transaction.receiver;
        updateReceiverTransactionTree(address,transaction);
        return true;
    }

    private boolean updateReceiverTransactionTree(String address,Transaction transaction){
        firebaseDatabase = FirebaseDatabase.getInstance();
        mTransactionRef = firebaseDatabase.getReference("transactions").child(address);

        String key = mTransactionRef.child("transactions").push().getKey();

        mTransactionRef.child(key).setValue(transaction);

        return true;
    }

    public Account[] getUserDetails(FirebaseUser user){
        final Account[] myAccount = new Account[1];
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAccountRef = firebaseDatabase.getReference().child("accounts").child(user.getUid());

        //attach a listener to read the data
        ValueEventListener accountsValue = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                account = dataSnapshot.getValue(Account.class);
                myAccount[0] = dataSnapshot.getValue(Account.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mAccountRef.addValueEventListener(accountsValue);

        accountsListener = accountsValue;

//        mAccountRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//              account = dataSnapshot.getValue(Account.class);
//              myAccount[0] = dataSnapshot.getValue(Account.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FIREBASEDB","The read failed: "+databaseError.getCode());
//            }
//        });
        return myAccount;
    }

    public String getAccountAddress(final String code){

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAccountRef  = firebaseDatabase.getReference().child("accounts");

        //attach a listener to read the data
        ValueEventListener accountsValue = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account tempAccount = dataSnapshot.getValue(Account.class);
                if(tempAccount != null){
                    if(tempAccount.accountHash == code){
                        address = tempAccount.accountHash;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mAccountRef.addValueEventListener(accountsValue);

        accountsListener = accountsValue;

        return address;
    }

    public boolean updateBalance(final double amount, final String address,FirebaseUser user){
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAccountRef  = firebaseDatabase.getReference().child("accounts").child(user.getUid());

        ValueEventListener accountEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getAccount Details
                Account account = dataSnapshot.getValue(Account.class);

                if(account != null) {
                    if(account.accountHash == address){

                        double update = amount + account.balance;
                        mAccountRef.child("balance").setValue(update);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG,"loadingAccount Details Cancelled", databaseError.toException());
            }
        };
        mAccountRef.addValueEventListener(accountEventListener);

        accountsListener = accountEventListener;
        return true;
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
