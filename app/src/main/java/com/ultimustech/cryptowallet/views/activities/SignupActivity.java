package com.ultimustech.cryptowallet.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.auth.AuthController;
import com.ultimustech.cryptowallet.controllers.auth.InputValidation;



public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private String message  = "";
    private boolean valid;

    private FirebaseAuth mFirebaseAuth;
    private AuthController authController = new AuthController();
    private InputValidation inputValidation = new InputValidation();

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

                    if(signup(emailText, passwordText, TAG)){
                        //TODO: create user currency account
                        message = getResources().getString(R.string.signup_success);
                        progressDialog.dismiss(); //dismiss progress dialog
                        new StyleableToast
                                .Builder(getBaseContext())
                                .text(message)
                                .textColor(Color.WHITE)
                                .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                                .length(Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(getApplication(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        message = getResources().getString(R.string.signup_failure);
                        progressDialog.dismiss();
                        new StyleableToast
                                .Builder(getBaseContext())
                                .text(message)
                                .textColor(Color.WHITE)
                                .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                                .length(Toast.LENGTH_LONG)
                                .show();
                    }

                }
            }
        });

    }

    //getter and setter methods
    public void setValid(boolean valid){
        this.valid = valid;
    }

    public boolean getValid(){
        return valid;
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

    /**
     * function to signup user
     * @param email,password,TAG
     */
    public boolean signup(EditText email, EditText password, final String TAG){

            mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth;
            boolean valid;
            //validate inputs
            if(inputValidation.isValidated(email,password)){
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                //firebase login authentication
                mFirebaseAuth.createUserWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "createUserWithEmail: successs");
                                    setValid(true);
                                } else {
                                    Log.w(TAG, "createUserWithEmail: failure",task.getException());
                                    setValid(false);
                                }
                            }
                        });
            }

            return getValid();
    }



}
