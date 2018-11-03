package com.ultimustech.cryptowallet.controllers.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.views.activities.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * called when message is received
     *
     * @param remoteMessage Object represeing the message receive from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //check fi the message contains a data payload
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message data payload: "+remoteMessage.getData());

            if(/* check if the data needs to processed for long */ true){
                //use a firebase job scheduler
                scheduleJob();
            } else {
                //handle the message
                handleNow();
            }
        }

        // check if the message contains a notification payload
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: "+remoteMessage.getNotification().getBody());
            String message = remoteMessage.getNotification().getBody();
            sendNotification(message);
        }

        //Also
    }

    /**
     * schedule the job using firebaseJobDipatcher
     */
    private void scheduleJob(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
    }

    /**
     * handle now
     */
    private void handleNow(){
        Log.d(TAG, "Short lived task is done");
    }

    private void sendNotification(String messageBody){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder  notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle("CryptoWallet")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //for oreo notifications

        notificationManager.notify(0, notificationBuilder.build());
    }
}
