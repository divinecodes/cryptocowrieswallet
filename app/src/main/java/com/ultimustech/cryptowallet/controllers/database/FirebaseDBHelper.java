package com.ultimustech.cryptowallet.controllers.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDBHelper {
    private boolean isCreated;
    private FirebaseAuth firebaseAuth;


    /**
     * function to check if user account is created
     * @param user //FireBaseUser
     * @return boolean
     */
    public boolean checkIfAccountCreated(FirebaseUser user){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference accountRef = rootRef.child("Accounts").child(user.getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    isCreated = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        accountRef.addListenerForSingleValueEvent(eventListener);
        return isCreated;
    }

    public String getFirebaseUserId(){
        String uid = null;
        firebaseAuth  = firebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            uid = firebaseUser.getUid();
        }

        return uid;
    }
}
