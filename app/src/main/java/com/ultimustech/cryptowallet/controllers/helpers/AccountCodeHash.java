package com.ultimustech.cryptowallet.controllers.helpers;

import android.os.Message;

import java.security.MessageDigest;

/**
 * Created by Poacher on 2/1/2018.
 * Class to return 6 character hash
 * would take users email address and return the hash,
 * this has then becomes the account code this,
 * this is to prevent the user from having  memorise his whole account hash number
 */

public class AccountCodeHash {

    /**
     *
     * @param input
     * @return accountCode
     */
    public String makeHash(String input){
        String accountCode = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();


            for (int i = 0; i < 3 ; i++){
                accountCode += Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1);
            }

        } catch (Exception e){
            accountCode = "Error" + e ;
        }

        return accountCode;
    }

}
