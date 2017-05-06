package com.rjp.cnvteachers.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.rjp.cnvteachers.ChatRoomActivity;
import com.rjp.cnvteachers.HomeScreen;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.app.Config;
import com.rjp.cnvteachers.beans.FcmNotificationBean;
import com.rjp.cnvteachers.service.PopupServiceBroadcast;
import com.rjp.cnvteachers.utils.AppPreferences;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "Data: " + remoteMessage.getData());
        //Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        FcmNotificationBean obj = new FcmNotificationBean();

        String notification_type = remoteMessage.getData().get("notification_type"); // circular,attendance,activity,chat

        //String msg = remoteMessage.getData().get("message");
        obj.setMessage(checkNull(remoteMessage.getData().get("message")));
        obj.setChrDocNo(checkNull(remoteMessage.getData().get("chrDocNo")));
        obj.setSender_id(checkNull(remoteMessage.getData().get("sender_id")));
        obj.setReceiver_id(checkNull(remoteMessage.getData().get("receiver_id")));
        obj.setSender_type(Integer.parseInt(checkIntegerNull(remoteMessage.getData().get("sender_type"))));
        obj.setReceiver_type(Integer.parseInt(checkIntegerNull(remoteMessage.getData().get("receiver_type"))));
        obj.setMsg_type(Integer.parseInt(checkIntegerNull(remoteMessage.getData().get("msg_type"))));
        obj.setMedia_url(checkNull(remoteMessage.getData().get("media_url")));
        obj.setEntry_date(checkNull(remoteMessage.getData().get("entry_date")));
        obj.setSender_name(checkNull(remoteMessage.getData().get("sender_name")));
        obj.setReceiver_name(checkNull(remoteMessage.getData().get("receiver_name")));
        obj.setTitle(checkNull(remoteMessage.getData().get("title")));

        JSONObject datObj = new JSONObject(remoteMessage.getData());
        Gson gson = new Gson();

        // app is in background. show the message in notification try
        Intent resultIntent = new Intent(getApplicationContext(), HomeScreen.class);

        switch (notification_type) {
            case "chat":
                // push notification belongs to a chat room
                //processChatRoomPush(title, isBackground, data);
                AppPreferences.setCommunicationCount(MyFirebaseMessagingService.this,AppPreferences.getCommunicationCount(MyFirebaseMessagingService.this)+1);
                sendToChatActivity(obj);
                break;

            case "worksheet":
                // push notification belongs to a chat room
                //processChatRoomPush(title, isBackground, data);
                AppPreferences.setWorksheetCount(MyFirebaseMessagingService.this,AppPreferences.getWorksheetCount(MyFirebaseMessagingService.this)+1);
                sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
                break;

//            case "notification":
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    AppPreferences.setNotificationCount(MyFirebaseMessagingService.this,AppPreferences.getNotificationCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;
//            case "achievement":
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    AppPreferences.setAchievementCount(MyFirebaseMessagingService.this,AppPreferences.getAchievementCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;
//            case "circular":
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    AppPreferences.setCircularCount(MyFirebaseMessagingService.this,AppPreferences.getCircularCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;
//            case "event":
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    AppPreferences.setEventCount(MyFirebaseMessagingService.this,AppPreferences.getEventCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;
//            case "activity":
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    AppPreferences.setActivityCount(MyFirebaseMessagingService.this,AppPreferences.getActivityCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;
//            case "ptm_remark":
//                // parent teacher meet remark
//                if(obj.getMessage()!=null && obj.getTitle()!=null) {
//                    //AppPreferences.setActivityCount(MyFirebaseMessagingService.this,AppPreferences.getActivityCount(MyFirebaseMessagingService.this)+1);
//                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
//                }
//                break;

            default:
                if(obj.getMessage()!=null && obj.getTitle()!=null) {
                    sendNotification(obj.getTitle(), obj.getMessage(),obj.getMedia_url());
               }
                break;
        }
    }

    private String checkNull(String input)
    {
        if(input!=null)
        {
            return input;
        }
        return "";
    }

    private String checkIntegerNull(String input)
    {
        if(input!=null && (!TextUtils.isEmpty(input)))
        {
            return input;
        }
        return "0";
    }

    private void sendToChatActivity(FcmNotificationBean obj)
    {
        // verifying whether the app is in background or foreground
        //if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
        if (ChatRoomActivity.is_running)
        {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
            pushNotification.putExtra("obj", obj);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        } else
        {
            // app is in background. show the message in notification try
            Intent resultIntent = new Intent(getApplicationContext(), HomeScreen.class);
            //showNotificationMessage(getApplicationContext(),obj.getTitle(),obj.getMessage(),obj.getEntry_date(),resultIntent);
            // check for push notification image attachment
            if (TextUtils.isEmpty(obj.getMedia_url())) {
                showNotificationMessage(getApplicationContext(), obj.getTitle(), obj.getSender_name() + " : " + obj.getMessage(), obj.getEntry_date(), resultIntent);
            } else {
                // push notification contains image
                // show it with the image
                showNotificationMessageWithBigImage(getApplicationContext(), obj.getTitle(), obj.getSender_name() + " : " + obj.getMessage(), obj.getEntry_date(), resultIntent, obj.getMedia_url());
            }
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title,String messageBody,String image) {

        try {
            //Intent intent = new Intent(this, Splash.class);
            Intent intent = new Intent(this, PopupServiceBroadcast.class);
            intent.putExtra("title",title);
            intent.putExtra("body",messageBody);
            intent.putExtra("image",image);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,PendingIntent.FLAG_ONE_SHOT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(messageBody);
            bigText.setBigContentTitle(title);
            bigText.setSummaryText(messageBody);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            Random ran = new Random();
            notificationManager.notify(ran.nextInt(100) /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     * */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
