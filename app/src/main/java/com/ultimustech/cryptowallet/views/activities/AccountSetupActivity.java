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
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.controllers.helpers.AccountCodeHash;

public class AccountSetupActivity extends AppCompatActivity {
    private static final String TAG = "AccountSetupActivity";
    private String message = "";

    private EditText passphrase1;
    private EditText passphrase2;
    private Button setupAccount;

    private FirebaseAuth mFirebaseAuth;
    private AccountCodeHash accountCodeHash;
    private FirebaseDBController firebaseDBController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        //get instances of objects
        mFirebaseAuth = FirebaseAuth.getInstance();
        accountCodeHash = new AccountCodeHash();
        firebaseDBController = new FirebaseDBController();

        //get widgets
        passphrase1 = findViewById(R.id.input_passphrase1);
        passphrase2 = findViewById(R.id.input_passphrase2);
        setupAccount = findViewById(R.id.setup_crypto_account);

        //setup account
        setupAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get passphrases
                final String firstPassphrase = passphrase1.getText().toString();
                final String secondPassphrase = passphrase2.getText().toString();
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
                 * TODO: connect to crypto api and use that api to create the crypto account
                 * TODO: USE MD5 HASH TO HASH THE PASSPHRASES
                 */
                if(user != null){
                    String userEmail = user.getEmail();
                    String accountCode = accountCodeHash.makeHash(userEmail);
                    String accountHash = "34adfadadfaofdn0134298rehadfadf";
                    boolean uploaded = firebaseDBController.uploadAccountCode(accountCode,
                            accountHash,user.getUid(), firstPassphrase,secondPassphrase);
                    if(!uploaded){
                        message = getResources().getString(R.string.account_creation_error);
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    } else {
                        message = getResources().getString(R.string.setup_account_success);
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
