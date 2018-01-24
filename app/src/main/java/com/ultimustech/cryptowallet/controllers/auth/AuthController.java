package com.ultimustech.cryptowallet.controllers.auth;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    /**
     * function to signup user
     * @param email,password,TAG
     * @return accountCreated
     */
    public boolean createAccount(EditText email, EditText password, final String TAG){

        mFirebaseAuth = FirebaseAuth.getInstance(); //initialize firebase auth;

        //validate inputs
        if(inputValidation.isValidated(email,password)){
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            //firebase login authentication
            mFirebaseAuth.createUserWithEmailAndPassword(emailText,passwordText)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
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
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
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


    //getter and setter methods
    public void setValid(boolean valid){
        this.valid = valid;
    }

    public boolean getValid(){
        return valid;
    }

    /**
     * function to send verification email
     *
     */

    public void sendEmailVerification(){
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        user.sendEmailVerification();
    }

}

