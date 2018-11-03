package com.ultimustech.cryptowallet.controllers.auth;

import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by Poacher on 1/23/2018.
 */

public class InputValidation {
    /**
     * this class would contain functionss for validation of inputs
     */

    /**
     * function to validate signup and login inputs
     * @param email,password
     */
    public boolean isValidated(EditText email, EditText password){
        boolean valid  = true;
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if(emailString.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailString).matches() ){
            email.setError("email not valid");
            valid = false;
        } else {
            email.setError(null);
        }

        if(passwordString.isEmpty() || passwordString.length() < 5){
            password.setError("must be more than 5 characters");
            valid = false;
        }

        return valid;
    }
}
