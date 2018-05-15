package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ultimustech.cryptowallet.R;

public class ProcessTransactionActivity extends AppCompatActivity {
    private static  final String TAG = "PROCESSINGACT";

    private ImageView processingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_transaction);
        setTitle("Processing Transaction...");

        processingAnim = findViewById(R.id.processing);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        if(type.equalsIgnoreCase("buy")){
            Toast.makeText(this, data,Toast.LENGTH_LONG).show();
        } else {

            if(data != null){
                String [] arrData = data.split(":");
                /**
                 * TODO: sending the data to the blockchain  via an async activity
                 * for now toast the the details
                 */
                String data1 = arrData[0];
                String data2 = arrData[1];
                Toast.makeText(this, "Data 1"+data1 +" Data2 "+data2, Toast.LENGTH_LONG).show();

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
