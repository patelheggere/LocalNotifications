package parents.talkative.localnotifications.Servic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import parents.talkative.localnotifications.MainActivity;
import parents.talkative.localnotifications.R;

public class MyService extends Service {
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private Timer timer = new Timer();
    private static final long UPDATE_INTERVAL = 10000;
    private MyService actual=null;
    private int count=0;

    @Override
    public void onCreate() {
        super.onCreate();
        actual=this;
       // MyTimerExecution();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        setDataForSimpleNotification();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                startJob();
            }
        });
        t.start();
       // MyTimerExecution();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    private void setDataForSimpleNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("From Service")
                .setContentText("Service");
        sendNotification();
    }

    private void sendNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        if (count == Integer.MAX_VALUE - 1)
            count = 0;
        notificationManager.notify(count, notification);
    }

    private void startJob(){
        count++;
        //do job here
        if(count%2==0)
        {
            setDataForSimpleNotification();
        }


        System.out.println("\n Count:"+count);
        //System.out.println("\n Serive update");
        //job completed. Rest for 5 second before doing another one
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //do job again
        startJob();
    }
}