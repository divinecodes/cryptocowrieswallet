package com.ultimustech.cryptowallet.views.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.models.Account;

public class UpdateAccountActivity extends AppCompatActivity {
    private static final String TAG ="UPDATEACCOUNT";

    private TextView txtEditPassphrase1;
    private TextView txtEditPassphrase2;
    private TextView txtEditContact;
    private Button btnUpdate;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener accountListener;
    private DatabaseReference accountRef;

    private FirebaseDBController firebaseDBController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        setTitle(getString(R.string.update_account));

        //initialize firebase
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        accountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());

        //initialize firebase db controller
        firebaseDBController = new FirebaseDBController();

        txtEditPassphrase1 = findViewById(R.id.edit_passphrase1);
        txtEditPassphrase2 = findViewById(R.id.edit_passphrase2);
        txtEditContact = findViewById(R.id.edit_contact);
        btnUpdate  = findViewById(R.id.update_crypto_account);


        //TODO: validate phone number etc
        //TODO: CREATE A VALIDATION CLASS AND INPUT ALL VALIDATION RULES IN THERE
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passphrase1 = txtEditPassphrase1.getText().toString();
                String passphrase2 = txtEditPassphrase2.getText().toString();
                String contact = txtEditContact.getText().toString();

                if(firebaseDBController.updateAccountDetails(passphrase1,passphrase2,contact,firebaseUser.getUid())){
                    Snackbar.make(v.getRootView(),"Account Details Updated!",Snackbar.LENGTH_LONG).show();
                    finish();
                } else {
                    Snackbar.make(v,"Unable to Update Account Details!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        ValueEventListener accountEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getAccount Details
                Account account = dataSnapshot.getValue(Account.class);

                if(account != null){
                   txtEditContact.setText(account.contact);
                   txtEditPassphrase1.setText(account.passphrase1);
                   txtEditPassphrase2.setText(account.passphrase2);
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG,"loadingAccount Details Cancelled", databaseError.toException());
            }
        };
        accountRef.addValueEventListener(accountEventListener);

        accountListener = accountEventListener;
    }
}
