package com.helper.Helper2Go.firebase_notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.ui.ChatActivity;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.utils.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.json.JSONObject;
import static com.helper.Helper2Go.utils.MyApplication.CHANNEL_1_ID;

public final class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    RemoteMessage mRemoteMessage;
    private NotificationManagerCompat notificationManager;
    String notification_type;

    /*Applied job*/
    //{body=Job Applied, type=job_applied, Title=Job Applied}

    /*accepted job*/
    //{body=Job Accepted By testTwo, data={"payment_mode":null,"other_info":"cgcg",
    // "payment_status":null,"helper_tools":"test tools","payment_amount":null,"created_at":"2020-09-29T07:28:13.000000Z",
    // "admin_approval":"1","user_cost":"26","updated_at":"2020-09-29T07:28:13.000000Z","user_id":"207","job_id":72,
    // "helper_exp":"test experience","id":73,"user_time":null,"user_job_desc":"g v","status":1},
    // type=Job_Accepted, Title=Job Accepted By testTwo}

    /*reject job*/
    //{body=Job Rejected By testTwo,
    // data={"payment_mode":null,"other_info":"vhvjvjv","payment_status":null,"helper_tools":"test tools",
    // "payment_amount":null,"created_at":"2020-09-29T07:28:31.000000Z","admin_approval":"2","user_cost":"896",
    // "updated_at":"2020-09-29T07:28:31.000000Z","user_id":"207","job_id":73,"helper_exp":"test experience",
    // "id":74,"user_time":null,"user_job_desc":"czhc","status":1},
    // type=job_rejected, Title=Job Rejected By testTwo}

    /*Complete Job */
    /*  {body=Job Completed By testOne, data={"payment_mode":null,"other_info":"vhbj","payment_status":null,"helper_tools":"test tools","payment_amount":null,"created_at":"2020-09-29T07:25:25.000000Z","admin_approval":"3","user_cost":"56","updated_at":"2020-09-30T16:15:31.000000Z","user_id":"208","job_id":71,"helper_exp":"test experience","id":72,"user_time":null,"user_job_desc":"ugug","status":1},
    type=job_completed, Title=Job Completed By testOne} /**/


    /*New Message Notification*/
    //{body=New message by testOne, data=New message by testOne, type=chat_message, Title=New message by testOne}

    //{body=New message by testTwo, data={"reciver_id":"215","receiver_job_id":"78","sender_id":"208",
    // "sender_job_id":"88"}, type=chat_message, Title=New message by testTwo}

    ////{"sender_id":"208","reciver_id":"215","sender_job_id":"88","receiver_job_id":"78"}

    /*{body=New message by testTwo,
     data={"reciver_id":"207","receiver_job_id":"53","sender_id":"208",
     "sender_job_id":"42"}, type=chat_message, Title=New message by testTwo}*/


    /*{body=New message by testOne,
    data={"other_info":"","user_cost":"","reciver_id":"208","receiver_job_id":"53","sender_id":"207",
    "sender_job_id":"42","user_job_desc":""}, type=chat_message, Title=New message by testOne}*/



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        this.mRemoteMessage = remoteMessage;
        Log.e("RemoteMessage ",remoteMessage+"");
        Log.e("RemoteMessageData ",remoteMessage.getData()+"");

        notificationManager = NotificationManagerCompat.from(this);

        if (remoteMessage == null)
        return;

        if (mRemoteMessage.getData().size() != 0)
        {
            Log.e(TAG, String.valueOf(mRemoteMessage.getData()));
            handleNotification(remoteMessage.getData().get("body"), mRemoteMessage.getData());
        }
    }

    MyApplication myApplication;
    private void handleNotification(String message, Map<String, String> data)
    {
        Intent intent=null;
        myApplication = (MyApplication) getApplication();
        notification_type = data.get("type");
        Log.e("NotiifcationType ",notification_type);

        if (notification_type.equalsIgnoreCase("job_applied"))
        {
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            intent.putExtra("noti_type","apply");
        }
        else if (notification_type.equalsIgnoreCase("Job_Accepted"))
        {
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            intent.putExtra("noti_type","accept");

        }
        else  if (notification_type.equalsIgnoreCase("job_rejected"))
        {
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            intent.putExtra("noti_type","reject");
        }
        else  if (notification_type.equalsIgnoreCase("job_completed"))
        {
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
            intent.putExtra("noti_type","complete");
        }

        else  if (notification_type.equalsIgnoreCase("chat_message"))
        {
            String obj=data.get("data");

    /* //{body=New message by testTwo, data={"reciver_id":"215","receiver_job_id":"78","sender_id":"208",
    // "sender_job_id":"88"}, type=chat_message, Title=New message by testTwo}*/

            try
            {
                JSONObject jsonObject=new JSONObject(obj);
                Log.e("JSOnObjectData ",jsonObject.toString());
                Log.e("sender_id ",jsonObject.getString("sender_id"));
                Log.e("reciver_id ",jsonObject.getString("reciver_id"));
                Log.e("sender_job_id ",jsonObject.getString("sender_job_id"));
                Log.e("receiver_job_id ",jsonObject.getString("receiver_job_id"));

                intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("noti_type","chat");
                intent.putExtra("senderId",jsonObject.getString("reciver_id"));
                intent.putExtra("receiverId",jsonObject.getString("sender_id"));
                intent.putExtra("sender_job_id",jsonObject.getString("sender_job_id"));
                intent.putExtra("receiver_job_id",jsonObject.getString("receiver_job_id"));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                // intent = new Intent(getApplicationContext(), SplashActivity.class);
                PendingIntent contentIntent=null;
                if (notification_type.equalsIgnoreCase("chat_message"))
                {
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                     contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);
                }
                else
                {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                     contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);
                }


                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.laucher_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(contentIntent)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                        //.setSound(defaultSoundUri)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .build();

                ringtone();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    channel.setShowBadge(true);
                    notificationManager.createNotificationChannel(channel);
                }

                //notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(1, notification);
            }
            else
            {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                //intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // play notification sound

                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                intent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), "StudentApp", message, "", intent);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }




  /*
    @Override
    public void handleIntent(Intent intent)
    {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }*/

    /**
     * Showing notification with text only
     */

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent)
    {
        notificationUtils = new NotificationUtils(context);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message,
                                                     String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    public void ringtone()
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}