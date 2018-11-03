package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.controllers.helpers.Validation;
import com.ultimustech.cryptowallet.models.Account;
import com.ultimustech.cryptowallet.models.Coinbase;
import com.ultimustech.cryptowallet.models.Transaction;

import java.sql.Timestamp;

public class ProcessTransactionActivity extends AppCompatActivity {
    private static  final String TAG = "PROCESSINGACT";
    private double amount;
    private String userAccount;
    private String mobileMoneyNum;
    private String loyaltyCode;
    private int ccPin;
    private String ccNum;
    private String coinBase;
    private long timestamp;

    private ImageView processingAnim;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener accountListener;
    private DatabaseReference accountRef;



    private FirebaseDBController firebaseDBController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_transaction);
        setTitle("Processing Transaction...");

        processingAnim = findViewById(R.id.processing);

        coinBase = Coinbase.getCoinbaseAddr();

        timestamp = System.currentTimeMillis();

        firebaseDBController = new FirebaseDBController();
        //initialize firebase
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        accountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        if(type.equalsIgnoreCase("buy")){
            String [] arrData = data.split(":");

            String receiver = arrData[1];
            if(Validation.validNumber(arrData[0])){
                amount = Double.parseDouble(arrData[0]);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Amount Received",Toast.LENGTH_LONG).show();
                finish();
            }

            if(arrData.length == 4){ //check if its a credit card payment type
                int flag = Integer.parseInt(arrData[3]);
                if(flag == 0){ //its a mobile money transaction
                    mobileMoneyNum = arrData[2];
                    //TODO: perfom mobile  money transaction
                } else if(flag == 2){ //its a loyalty transaction
                    loyaltyCode = arrData[2];
                    if(firebaseDBController.checkLoyalty(loyaltyCode)){
                        firebaseDBController.updateLoyatyToken(firebaseUser.getUid());
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Loyalty Code",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    //TODO: check loyalty code for validity and amount it can buy to the amount specified
                }
            } else if(arrData.length == 5){
                    ccNum = arrData[2];
                    if(Validation.isCorrectPin(arrData[3])){
                        ccPin  = Integer.parseInt(arrData[3]);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Pin Submittd",Toast.LENGTH_LONG).show();
                        finish();
                    }
            }

            Transaction transaction = new Transaction("buy",coinBase,receiver,amount,timestamp);
            boolean uploaded = firebaseDBController.uploadTransactions(transaction);
            if(uploaded){
                firebaseDBController.updateBalance(amount, receiver, firebaseUser);
                Toast.makeText(getApplicationContext(),"Transaction Completed Successfully",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"Unable to Perform Transactions",Toast.LENGTH_LONG).show();
            }

//            Handler  handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //do something
//                    Toast.makeText(getApplicationContext(), "Transaction Successfully added",Toast.LENGTH_LONG).show();
//                }
//            }, 7000);
//            finish();

            //now add the transaction to the users account
            //TODO: add transaction to the blockchain, to be mined using an async task


        } else {

            if(data != null){
                String [] arrData = data.split(":");
                /**
                 * TODO: sending the data to the blockchain  via an async activity
                 * for now toast the the details
                 */
                String sender = arrData[0];
                String receiver = arrData[1];
                String amountStr = arrData[2];
                Double amount = Double.parseDouble(amountStr);

                Transaction transaction = new Transaction("send",sender,receiver,amount,timestamp);
                boolean uploaded = firebaseDBController.uploadTransactions(transaction);
                if(uploaded){
                    firebaseDBController.updateBalance(amount, sender, firebaseUser);
                    Toast.makeText(getApplicationContext(),"Transaction Completed Successfully",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Unable to Perform Transactions",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "No Transaction Data Received",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        //load image into the
        Glide.with(this).load(R.drawable.processing).into(processingAnim);

    }
}
