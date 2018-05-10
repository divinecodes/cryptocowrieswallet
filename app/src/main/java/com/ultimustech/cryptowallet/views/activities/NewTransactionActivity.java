package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;

public class NewTransactionActivity extends AppCompatActivity {
    private static  final String TAG = "New Transaction Activity";

    private TextView accountCode;
    private TextView amount;
    private Button btnSend;
    private ProgressBar progressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        setTitle(getResources().getString(R.string.add_new_transaction));

        //set widgets
        accountCode = findViewById(R.id.sendAccount);
        amount  = findViewById(R.id.sendAmount);
        btnSend = findViewById(R.id.sendButton);
        progressSpinner = findViewById(R.id.transactionProgress);

        /**
         * On Focus Changed on account Account. get Account code and get the corresponding account
         * hash code from the database using an asychronous background task
         * For now just change the text
         */
         accountCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View view, boolean b) {
                 accountCode.setText(getResources().getString(R.string.sample_account_hash));
             }
         });

        /**
         * begin transaction when button is clicked
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strAmount = amount.getText().toString();
                String to = accountCode.getText().toString();

                String data = strAmount + ":" + to ;
                Intent intent = new Intent(getApplication(),ProcessTransactionActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
                overridePendingTransition(R.anim.push_top_in,R.anim.push_top_out);
            }
        });
    }
}