package com.ultimustech.cryptowallet.controllers.helpers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;


public class MyFirebaseInstanceIDServices  extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseIIDService";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDBController firebaseDBController;

    public MyFirebaseInstanceIDServices(){
        super();
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh(){
        //get updated instance token
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: "+refreshToken);

        //send token to server
        sendToServer(refreshToken);
    }

    private void sendToServer(String tokens){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDBController = new FirebaseDBController();

        firebaseDBController.updateNotificationToken(tokens, firebaseUser.getUid());
    }
}
