package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";

    private EditText emailText;
    private EditText passwordText;
    private Button btnLogin;
    private TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //setup widgets
        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signup_link);


        //start login activity when login link text is clicked
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in); //start transition animation
            }
        });
    }
}
