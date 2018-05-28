package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class NewTransactionActivity extends AppCompatActivity {
    private static  final String TAG = "NewTransactionActivity";
    private String passphrase1Str;
    private String passphrase2Str;

    private EditText sendAccount;
    private EditText sendAmount;
    private EditText passphrase1;
    private EditText passphrase2;
    private Button btnSend;
    private ProgressBar progressSpinner;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference accountRef;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDBController firebaseDBController;
    private ValueEventListener accountListener;

    private String  receiverAddress;
    private Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        setTitle(getResources().getString(R.string.add_new_transaction));

        //set widgets
        sendAccount = findViewById(R.id.sendAccount);
        sendAmount = findViewById(R.id.sendAmount);
        passphrase1 = findViewById(R.id.userPassphrase1);
        passphrase2 = findViewById(R.id.userPassphrase2);


        btnSend = findViewById(R.id.sendButton);
        progressSpinner = findViewById(R.id.transactionProgress);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDBController = new FirebaseDBController();
        accountRef = firebaseDatabase.getReference("accounts").child(firebaseUser.getUid());



        /**
         * begin transaction when button is clicked
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    receiverAddress = sendAccount.getText().toString();
                    double amntToSend = Double.parseDouble(sendAmount.getText().toString());
                    double balance  = account.balance;

                    //check if the amount is more than the threshold
                    double  threshold = 0.000125;
                    if((balance - amntToSend) <= threshold){
                        sendAmount.setError("Balance less than Amount");
                    }

                String phrase1 = passphrase1.getText().toString();
                String phrase2 = passphrase2.getText().toString();

                if(phrase1.equals(passphrase1Str) && phrase2.equals(passphrase2Str)){
                    //concatenate the transaction data and send across the intent
                    String data = account.accountHash + ":" + receiverAddress + ":"+sendAmount.getText().toString();
                    Intent intent = new Intent(getApplication(),ProcessTransactionActivity.class);
                    intent.putExtra("data",data);
                    intent.putExtra("type","send");
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
                } else {
                    passphrase1.setError("Passphrase may not be correct");
                    passphrase2.setError("Passphrase may not be correct");
                }
            }
        });
    }

    public void onStart(){

        super.onStart();
        //attach a listener to read the data
        ValueEventListener accountsValue = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account tempAccount = dataSnapshot.getValue(Account.class);
                if(tempAccount != null){
                    account = tempAccount;
                    passphrase1Str = tempAccount.passphrase1;
                    passphrase2Str = tempAccount.passphrase2;
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to Find User Account",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        accountRef.addValueEventListener(accountsValue);

        accountListener = accountsValue;


        sendAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String code = sendAccount.getText().toString();
                String address = firebaseDBController.getAccountAddress(code);
                if(address != null){
                    sendAccount.setText(address);
                    receiverAddress = address;
                } else {
                    sendAccount.setError("Account Address Not Found");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }



}