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

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.auth.AuthController;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private String message ="";

    private EditText emailText;
    private EditText passwordText;
    private Button btnLogin;
    private TextView signupLink;

    private AuthController authController = new AuthController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                if(authController.login(emailText, passwordText,TAG)){
                    message = getResources().getString(R.string.login_success);
                    progressDialog.dismiss(); //dismiss progress dialog
                    new StyleableToast
                            .Builder(getBaseContext())
                            .text(getResources().getString(R.string.signup_success))
                            .textColor(Color.WHITE)
                            .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .length(Toast.LENGTH_LONG)
                            .show();
                    Intent intent  = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    message = getResources().getString(R.string.login_failure);
                    progressDialog.dismiss(); //dismiss progress dialog
                    new StyleableToast
                            .Builder(getBaseContext())
                            .text(getResources().getString(R.string.signup_success))
                            .textColor(Color.WHITE)
                            .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
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
}
