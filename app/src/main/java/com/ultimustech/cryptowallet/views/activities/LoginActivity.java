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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private static final int REQUEST_SIGNUP = 0;
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

                if(inputValidation.isValidated(emailText, passwordText)){
                    final String email = emailText.getText().toString();
                    final String password = passwordText.getText().toString();
                    login(email, password,TAG);
                } else {
                    onLoginFailed();
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

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_SIGNUP){
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed(){
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * function to login user
     * @param email,password,TAG
     */
    public void login(String email, String password, final String TAG){
        mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth;

        //start progress bar
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticate));
        progressDialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail: success");
                            progressDialog.dismiss();
                            onLoginSuccess();
                        } else {
                            Log.w(TAG,"signInWithEmail: failure",task.getException());
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    }
                });
    }

    /**
     * call this function when login is success full
     */
    public void onLoginSuccess(){
        message = getResources().getString(R.string.login_success);
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
        //redirect user to Main Activity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_top_out,R.anim.push_top_in);
        finish();
    }

    /**
     * call this function on login failure
     */
    public void onLoginFailed(){
        message = getResources().getString(R.string.login_failure);
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
}
