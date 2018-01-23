package com.ultimustech.cryptowallet.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ultimustech.cryptowallet.R;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassword;
    private Button btnSignUp;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

    }
}
