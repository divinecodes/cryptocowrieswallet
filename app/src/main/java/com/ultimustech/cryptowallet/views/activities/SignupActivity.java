package com.ultimustech.cryptowallet.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.auth.AuthController;
import com.ultimustech.cryptowallet.controllers.auth.InputValidation;
import com.ultimustech.cryptowallet.controllers.helpers.AccountCodeHash;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private String message = "";

    private FirebaseAuth mFirebaseAuth;
    private InputValidation inputValidation = new InputValidation();
    private AccountCodeHash accountCodeHash = new AccountCodeHash();
    private FirebaseDBController firebaseDBController = new FirebaseDBController();

    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassword;
    private Button btnSignUp;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth

        //setup widgets
        emailText = findViewById(R.id.signup_email);
        passwordText = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.input_confirmPassword);
        btnSignUp = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);

        //disable signup button
        btnSignUp.setEnabled(false);
        btnSignUp.setClickable(false);


        //start login activity when login link text is clicked
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); //start transition animation
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordVal = passwordText.getText().toString();
                String confirmPasswordVal = confirmPassword.getText().toString();

                //compare the two strings
                if (passwordVal.equals(confirmPasswordVal)){
                    btnSignUp.setEnabled(true);
                    btnSignUp.setClickable(true);
                } else {
                    String wrongPassword = getResources().getString(R.string.wrong_password);
                    confirmPassword.setError(wrongPassword);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //set actions for signup button clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //verify email
                if(inputValidation.isValidated(emailText,passwordText)){
                    final String  email = emailText.getText().toString();
                    final String password = passwordText.getText().toString();
                    createAccount(email,password,TAG);
                } else {
                    onSignupFailed();
                }
            }
        });
    }


    /**
     * function to signup user
     * @param email,password,TAG
     * @return accountCreated
     */
    public void createAccount(final String email, final String password, final String TAG){

        mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth;

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.creating_account));
        progressDialog.show();

        //firebase login authentication
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail: successs");
                            progressDialog.dismiss();
                            onSignupSuccess();

                            finish();
                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "createUserWithEmail: failure",task.getException());
                            onSignupFailed();
                        }
                    }
                });
    }
    /**
     * function to handle on signup sucess
     */
    public void onSignupSuccess(){
        message = getResources().getString(R.string.signup_success);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        //send email verification
        sendEmailVerification(user);
        //redirect user to main activity
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_top_out,R.anim.push_top_in);
    }

    /**
     * function to handle on signup failed
     */
    public void onSignupFailed(){
        message = getResources().getString(R.string.signup_failure);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    /**
     * send verification email using firebase
     */
    private static void sendEmailVerification(FirebaseUser user){
        user.sendEmailVerification();
    }


}