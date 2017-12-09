package com.example.mikailoral.myapplication1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.example.mikailoral.myapplication1.api.response.PushResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MyService extends FirebaseMessagingService {


    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("dorlion", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("dorlion", "Message data payload: " + remoteMessage.getData());
            Map<String, String> map = remoteMessage.getData();
            String ucret = map.get("detail");

            Log.d("dorlion", ucret);
            try {
                MainActivity.MAINACTIVITY_INSTANCE.voiceActivity(ucret);
            }catch (Exception e){

            }

            /*
            if (/* Check if data needs to be processed by long running job // true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
*/
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("dorlion", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
