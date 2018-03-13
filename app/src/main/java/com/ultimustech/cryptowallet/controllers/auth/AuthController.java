package com.ultimustech.cryptowallet.controllers.auth;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    public static void sendEmailVerification(FirebaseUser user){
        user.sendEmailVerification();
    }

}

