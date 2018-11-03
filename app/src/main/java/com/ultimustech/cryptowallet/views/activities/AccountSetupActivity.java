package com.ultimustech.cryptowallet.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.api.RestAPI;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.controllers.helpers.AccountCodeHash;
import com.ultimustech.cryptowallet.controllers.helpers.CryptoCowriesHelper;
import com.ultimustech.cryptowallet.models.Loyalty;

import java.security.Security;

public class AccountSetupActivity extends AppCompatActivity {
    private static final String TAG = "AccountSetupActivity";
    private String message = "";

    private EditText passphrase1;
    private EditText passphrase2;
    private EditText edtContact;
    private Button setupAccount;
    private RestAPI restAPI;

    private FirebaseAuth mFirebaseAuth;
    private AccountCodeHash accountCodeHash;
    private FirebaseDBController firebaseDBController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        //set up security
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //get instances of objects
        mFirebaseAuth = FirebaseAuth.getInstance();
        accountCodeHash = new AccountCodeHash();
        firebaseDBController = new FirebaseDBController();
        restAPI = new RestAPI();

        //get widgets
        passphrase1 = findViewById(R.id.input_passphrase1);
        passphrase2 = findViewById(R.id.input_passphrase2);
        setupAccount = findViewById(R.id.setup_crypto_account);
        edtContact = findViewById(R.id.input_contact);

        //setup account
        setupAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get passphrases
                final String firstPassphrase = passphrase1.getText().toString();
                final String secondPassphrase = passphrase2.getText().toString();
                final String contact = edtContact.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(AccountSetupActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.setting_up_account));
                progressDialog.show();

                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                /**
                 * if account created successfully, user  is automatically generated
                 * get user details and start creating currency account
                 * if user object not null create account code and upload to firebase
                 * TODO: USE MD5 HASH TO HASH THE PASSPHRASES
                 */
                if(user != null){
                    String userEmail = user.getEmail();
                    String accountCode = accountCodeHash.makeHash(userEmail);
                    String accountHash = CryptoCowriesHelper.generateWalletAddress();
                    double balance = 0.000125;
                    boolean uploaded = firebaseDBController.uploadAccount(accountCode,
                            accountHash,user.getUid(), firstPassphrase,secondPassphrase,contact,balance);
                    if(!uploaded){
                        message = getResources().getString(R.string.account_creation_error);
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    } else {
                        Loyalty loyalty = new Loyalty(accountCode, balance, true);
                        firebaseDBController.gaveLoyaltyToken(loyalty, user.getUid());

                        message = getResources().getString(R.string.setup_account_success);
//                        restAPI.nexmoSMS(contact,message);
                        Toast.makeText(getApplication(),message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    progressDialog.dismiss();
                } else {
                    message = getResources().getString(R.string.account_creation_error);
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    finish();
                    progressDialog.dismiss();
                }
            }
        });
    }
}
