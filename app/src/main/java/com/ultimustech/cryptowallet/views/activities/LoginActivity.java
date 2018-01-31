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
import com.ultimustech.cryptowallet.controllers.auth.InputValidation;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private String message ="";
    private boolean  valid;

    private EditText emailText;
    private EditText passwordText;
    private Button btnLogin;
    private TextView signupLink;

    private InputValidation inputValidation = new InputValidation();
    private FirebaseAuth mFirebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize firebase
        mFirebaseAuth = FirebaseAuth.getInstance();

        //setup widgets
        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signup_link);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start progress bar
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.creating_account));
                progressDialog.show();

                if(login(emailText, passwordText,TAG)){
                    message = getResources().getString(R.string.login_success);

                    new StyleableToast
                            .Builder(getBaseContext())
                            .text(message)
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.GREEN)
                            .length(Toast.LENGTH_LONG)
                            .show();
                    progressDialog.dismiss(); //dismiss progress dialog
                    Intent intent  = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    message = getResources().getString(R.string.login_failure);
                    progressDialog.dismiss(); //dismiss progress dialog
                    new StyleableToast
                            .Builder(getBaseContext())
                            .text(message)
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.RED)
                            .length(Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        //start login activity when login link text is clicked
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); //start transition animation
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
     * function to login user
     * @param email,password,TAG
     */
    public boolean login(EditText email, EditText password, final String TAG){
        mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth;

        //validate inputs
        if(inputValidation.isValidated(email,password)){
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            mFirebaseAuth.signInWithEmailAndPassword(emailText,passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "signInWithEmail: success");
                                setValid(true);
                            } else {
                                Log.w(TAG,"signInWithEmail: failure",task.getException());
                                setValid(false);
                            }
                        }
                    });
        }

        return getValid();
    }
}
