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
import com.ultimustech.cryptowallet.models.AccountCode;

public class NewTransactionActivity extends AppCompatActivity {
    private static  final String TAG = "NewTransactionActivity";
    private String passphrase1Str;
    private String passphrase2Str;

    private EditText sendAccount;
    private EditText sendAmount;
    private EditText passphrase1;
    private EditText passphrase2;
    private Button btnSend;
    private Button scanAddress;
    private ProgressBar progressSpinner;
    private TextView edtReceiverAddress;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference accountRef;
    private DatabaseReference otherAccountRef;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDBController firebaseDBController;
    private ValueEventListener accountListener;
    private ValueEventListener otherAccountListener;

    private String  receiverAddress;
    private Account account;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        setTitle(getResources().getString(R.string.add_new_transaction));

        //set widgets
        sendAccount = findViewById(R.id.sendAccount);
        sendAmount = findViewById(R.id.sendAmount);
        passphrase1 = findViewById(R.id.userPassphrase1);
        passphrase2 = findViewById(R.id.userPassphrase2);
        scanAddress = findViewById(R.id.scan_qr_code);
        btnSend = findViewById(R.id.sendButton);
        progressSpinner = findViewById(R.id.transactionProgress);
        edtReceiverAddress = findViewById(R.id.receiverAddress);

        //disable some widgets
        sendAmount.setVisibility(View.GONE);
        passphrase1.setVisibility(View.GONE);
        passphrase2.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        edtReceiverAddress.setVisibility(View.GONE);

        final String [] gotAddress = new String[1];

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDBController = new FirebaseDBController();
        accountRef = firebaseDatabase.getReference("accounts").child(firebaseUser.getUid());
        otherAccountRef = firebaseDatabase.getReference("codes");



        scanAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tempAddress = sendAccount.getText().toString();
                progressSpinner.setVisibility(View.VISIBLE);

                if(tempAddress.length() != 6 ){
                    sendAccount.setError("code must be 6 characters");
                    progressSpinner.setVisibility(View.GONE);
                } else {

                    ValueEventListener sendAccountListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //get account details
                            AccountCode accountCode = dataSnapshot.getValue(AccountCode.class);

                            if(accountCode != null ){
                                   // edtReceiverAddress.setText(accountCode.accountHash);
                                    sendAccount.setVisibility(View.GONE);
                                    progressSpinner.setVisibility(View.GONE);
                                    sendAmount.setVisibility(View.VISIBLE);
                                    passphrase1.setVisibility(View.VISIBLE);
                                    passphrase2.setVisibility(View.VISIBLE);
                                    scanAddress.setVisibility(View.GONE);
                                    btnSend.setVisibility(View.VISIBLE);
                                    gotAddress[0] = accountCode.accountHash;
                                    Toast.makeText(getApplicationContext(),"Address Found",Toast.LENGTH_LONG).show();


                            } else {
                                sendAccount.setError("Account Not Found");
                                progressSpinner.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    otherAccountRef.child(tempAddress).addValueEventListener(sendAccountListener);
                    otherAccountListener = sendAccountListener;
                }

            }
        });
        /**
         * begin transaction when button is clicked
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    double amntToSend = Double.parseDouble(sendAmount.getText().toString());
                    double balance  = account.balance;
                    receiverAddress = gotAddress[0];
                    Toast.makeText(getApplicationContext(),"GOT ADDRESS: "+receiverAddress,Toast.LENGTH_LONG).show();
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
        final ValueEventListener accountsValue = new ValueEventListener() {
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




    }
//


}