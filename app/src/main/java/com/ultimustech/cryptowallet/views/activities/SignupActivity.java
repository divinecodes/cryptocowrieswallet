package com.ultimustech.cryptowallet.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.auth.AuthController;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private String message  = "";

    private FirebaseAuth mFirebaseAuth;
    private AuthController authController = new AuthController();

    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassword;
    private Button btnSignUp;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth  = FirebaseAuth.getInstance(); //initialize firebase auth

        //setup widgets
        emailText = findViewById(R.id.signup_email);
        passwordText = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.input_confirmPassword);
        btnSignUp = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);

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

        //set actions for signup button clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!confirmPassword(passwordText, confirmPassword)){
                    confirmPassword.setError("Password Not Correct");
                } else {
                    // start progress bar
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                            R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getResources().getString(R.string.creating_account));
                    progressDialog.show();

                    if(authController.createAccount(emailText, passwordText, TAG)){
                        //TODO: create user currency account
                        message = getResources().getString(R.string.signup_success);
                        progressDialog.dismiss(); //dismiss progress dialog
                        new StyleableToast
                                .Builder(getBaseContext())
                                .text(getResources().getString(R.string.signup_success))
                                .textColor(Color.WHITE)
                                .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                                .length(Toast.LENGTH_LONG)
                                .show();
                        //login
                        if(authController.login(emailText,passwordText,"LoginActivity")){
                            //send verification email
                            authController.sendEmailVerification();
                            //start toast activity
                            finish();
                        } else {
                            Intent i = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    } else {
                        message = getResources().getString(R.string.signup_failure);
                        progressDialog.dismiss();
                        new StyleableToast
                                .Builder(getBaseContext())
                                .text(getResources().getString(R.string.signup_success))
                                .textColor(Color.WHITE)
                                .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                                .length(Toast.LENGTH_LONG)
                                .show();
                    }

                }
            }
        });

    }


    /**
     * function to verify if password is the same
     * @param password,confirmpassword
     * @return isConfirmed
     */
    public boolean confirmPassword(EditText password, EditText confirmPassword){
            boolean isConfirmed = false;
            String passwordVal = password.getText().toString();
            String confirmPasswordVal = confirmPassword.getText().toString();

            //compare the two strings
            if( passwordVal.equals(confirmPasswordVal))
                isConfirmed = true;

            return isConfirmed;
    }

}
