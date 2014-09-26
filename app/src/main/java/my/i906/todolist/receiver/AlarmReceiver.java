package my.i906.todolist.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import my.i906.todolist.MainActivity;
import my.i906.todolist.R;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }
    private NotificationManager nm;
    @Override

        public void onReceive(Context ctx, Intent intent) {

        //Log is a helper class that will output log messages to the LogCat panel.
        //Log.d(“AlarmReceiver”, “I got something!”);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        String nm = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        String title = intent.getStringExtra("todoedit_title");
        String desc = intent.getStringExtra("todoedit_desc");
        long id = intent.getLongExtra("todoedit_id", -1);

        Intent ms = new Intent(ctx, MainActivity.class);
        ms.putExtra("todoedit_id", id);
        ms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, ms, 0);





        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(ctx)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(desc)
                .setTicker(title)
                .setSound(alarmSound)

                .setSmallIcon(R.drawable.ic_launcher2) //stat_notify_voicemail
      // start Activity when the user clicks the notification text in the notification drawer.
        .setContentIntent(pi);

        Notification notification = builder.getNotification();
        mNotificationManager.notify(100,notification);


    }



}
