package com.ultimustech.cryptowallet.views.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.ultimustech.cryptowallet.controllers.helpers.Validation;
import com.ultimustech.cryptowallet.models.Account;

import static java.lang.String.join;

public class BuyCoinsActivity extends AppCompatActivity {
    private static final String TAG = "BUYCOINSACTIVITY";
    private String userPassphrase1;
    private String userPassphrase2;
    private String userAccount;
    private String data;
    private int paymentFlag = 0;

    private Spinner spnPaymentMode;
    private LinearLayout momoLayout;
    private LinearLayout creditCardLayout;
    private LinearLayout loyaltyLayout;
    private Button buyCurrency;
    private EditText edtMobileMoney;
    private EditText edtLoyaltyCode;
    private EditText edtCCardNum;
    private EditText edtCCardPin;
    private EditText edtPassphrase1;
    private EditText edtPassphrase2;
    private Button displayAmnt;
    private Button displayAccountId;
    private TextView ghanaCedis;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener accountListener;
    private DatabaseReference accountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coins);
        setTitle(getResources().getString(R.string.buyCoinsTitle));

        Intent intent = getIntent();
        final Double amount = intent.getDoubleExtra("amount",0.0);

        //initialize firebase
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        accountRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(firebaseUser.getUid());

        spnPaymentMode = findViewById(R.id.selectPaymentType);
        momoLayout = findViewById(R.id.momoLayout);
        creditCardLayout = findViewById(R.id.credit_card_layout);
        loyaltyLayout = findViewById(R.id.loyaltyLayout);
        buyCurrency = findViewById(R.id.buyCurrency);
        edtMobileMoney = findViewById(R.id.mobile_money_number);
        edtLoyaltyCode = findViewById(R.id.loyalty_code);
        edtCCardNum = findViewById(R.id.credit_card_number);
        edtCCardPin = findViewById(R.id.credit_card_pin);
        edtPassphrase1 = findViewById(R.id.enter_passphrase1);
        edtPassphrase2 = findViewById(R.id.enter_passphrase2);
        displayAccountId = findViewById(R.id.buyer_public_address);
        displayAmnt = findViewById(R.id.amount_to_buy);
        ghanaCedis = findViewById(R.id.ghanacedis);


        //set values
        displayAmnt.setText(amount +" CCW");
        //TODO: do the currency evalaution and set the currency value (M = PQ/V)
        //set spinner elements
        final ArrayAdapter<CharSequence> paymentType = ArrayAdapter.createFromResource(this,
                R.array.paymentTypes, android.R.layout.simple_spinner_dropdown_item);
        paymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    paymentFlag = 1;
                } else if (itemPosition == 2){
                    momoLayout.setVisibility(View.GONE);
                    creditCardLayout.setVisibility(View.GONE);
                    loyaltyLayout.setVisibility(View.VISIBLE);
                    paymentFlag = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileMoney = edtMobileMoney.getText().toString();
                String loyaltyCode = edtLoyaltyCode.getText().toString();
                String cardNum = edtCCardNum.getText().toString();
                String cardPin = edtCCardPin.getText().toString();
                String passphrase1 = edtPassphrase1.getText().toString();
                String passphrase2 = edtPassphrase2.getText().toString();
                data= amount+":"+userAccount+":";

                if(userPassphrase1.equals(passphrase1) && userPassphrase2.equals(passphrase2)){
                    if(paymentFlag == 0){ //user is paying with mobile money
                        mobileMoney = "233"+mobileMoney.substring(1);
                        if(!Validation.isValidPhoneNumber(mobileMoney)){
                            Snackbar.make(v, "Please enter a valid phone number",Snackbar.LENGTH_LONG).show();
                        } else {
                            data += mobileMoney +":"+paymentFlag;
                        }
                    } else if(paymentFlag == 1){ //user is paying with credit card
                        if(!Validation.isCorrectPin(cardPin)){ //validate pin
                               Snackbar.make(v, "Wrong Pin, Re-enter",Snackbar.LENGTH_LONG).show();
                        } else {
                            data += cardNum +":"+cardPin+":"+paymentFlag;
                        }

                    } else if(paymentFlag == 2 ){
                        data += loyaltyCode+":"+paymentFlag;
                    }

                    /**
                     * let the user confirm the transaction
                     */
                    final AlertDialog.Builder  confirmBuilder= new AlertDialog.Builder(v.getContext());
                    confirmBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    confirmBuilder.setTitle("Confirm Transaction?");
                    confirmBuilder.setMessage("Confirm This Transaction. Should We Process the Transaction?");
                    confirmBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogInterface1, int which1){
                            Intent intent = new Intent(getApplication(), ProcessTransactionActivity.class);
                            intent.putExtra("type","buy");
                            intent.putExtra("data",data);
                            startActivity(intent);
                        }
                    });

                    confirmBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogInterface2, int which2){
                            confirmBuilder.setOnCancelListener(new DialogInterface.OnCancelListener(){
                                @Override
                                public void onCancel(DialogInterface dialog2){
                                    dialog2.cancel();
                                }
                            });
                        }
                    });

                    confirmBuilder.show();

                } else {
                    //user passphrase wrong
                    Snackbar.make(v,"Wrong Passphrases, Please Re-enter",Snackbar.LENGTH_LONG).show();
                    edtPassphrase1.setError("Wrong Passphrase");
                    edtPassphrase2.setError("Wrong Passphrase");
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        ValueEventListener accountEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getAccount Details
                Account account = dataSnapshot.getValue(Account.class);

                if(account != null) {
                    displayAccountId.setText(account.accountHash);
                    userPassphrase1 = account.passphrase1;
                    userPassphrase2 = account.passphrase2;
                    userAccount = account.accountHash;
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
