package com.ultimustech.cryptowallet.controllers.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.custom.AccountCodeHash;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;

import java.util.concurrent.Executor;

/**
 * Created by Poacher on 1/23/2018.
 * This class handles all authentication logic
 * The class would implement the Google Firebase Authentication library
 */

public class AuthController {
    private FirebaseAuth mFirebaseAuth;
    private InputValidation inputValidation = new InputValidation();
    private boolean valid;
    private Activity activity;





    /**
     * function to send verification email
     *
     */

    public void sendEmailVerification(){
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        user.sendEmailVerification();
    }

}

