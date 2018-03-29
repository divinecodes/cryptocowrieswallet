package com.ultimustech.cryptowallet.views.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ultimustech.cryptowallet.R;

import static java.lang.String.join;

public class BuyCoinsActivity extends AppCompatActivity {
    private static final String TAG = "BUYCOINSACTIVITY";
    private String sellerAddress;
    private String sellerAmountForSale;

    private TextView publicAddress;
    private TextView amountForSale;
    private Spinner spnPaymentMode;
    private LinearLayout momoLayout;
    private LinearLayout creditCardLayout;
    private LinearLayout loyaltyLayout;
    private Button buyCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coins);
        setTitle(getResources().getString(R.string.buyCoinsTitle));

        sellerAddress = getIntent().getStringExtra("sellerAddress");
        sellerAmountForSale = getIntent().getStringExtra("coinsForSale");

        publicAddress = findViewById(R.id.seller_public_address);
        amountForSale = findViewById(R.id.seller_coins_for_sale);
        spnPaymentMode = findViewById(R.id.selectPaymentType);
        momoLayout = findViewById(R.id.momoLayout);
        creditCardLayout = findViewById(R.id.credit_card_layout);
        loyaltyLayout = findViewById(R.id.loyaltyLayout);
        buyCurrency = findViewById(R.id.buyCurrency);

        /**
         * TODO: find a way to split the seller amount into floating point value and use as a delimiter for getting the actual amount being sold
         */

        publicAddress.setText(sellerAddress);
        amountForSale.setText(sellerAmountForSale);

        //set spinner elements
        final ArrayAdapter<CharSequence> paymentType = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.paymentTypes, android.R.layout.simple_spinner_dropdown_item);
        spnPaymentMode.setAdapter(paymentType);


        //set on click listener on spinner
        spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = parent.getSelectedItemPosition();
                if(itemPosition == 0){
                    momoLayout.setVisibility(View.VISIBLE);
                    creditCardLayout.setVisibility(View.GONE);
                    loyaltyLayout.setVisibility(View.GONE);
                } else if (itemPosition == 1){
                    momoLayout.setVisibility(View.GONE);
                    creditCardLayout.setVisibility(View.VISIBLE);
                    loyaltyLayout.setVisibility(View.GONE);
                } else if (itemPosition == 2){
                    momoLayout.setVisibility(View.GONE);
                    creditCardLayout.setVisibility(View.GONE);
                    loyaltyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"You have both this currencies ",Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
