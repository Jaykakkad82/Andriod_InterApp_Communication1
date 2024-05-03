package uic.cs478.jaykumarkakkad.myapplication.clipserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import uic.cs478.jaykumarkakkad.myapplication.commonInterface.MusicPlayerInterface;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MyService extends Service {

    private static final String CHANNEL_ID = "Music player style" ;
    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer mPlayer;
    private int mStartID;

    private List<Integer> musicList = new ArrayList<>(Arrays.asList(R.raw.music1,R.raw.nostalgia,R.raw.dreamy,R.raw.romantic));


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("CLIPSERVER", "Servive was created!") ;


        // Implement a notification channel
        this.createNotificationChannel();

        final Intent notificationIntent = new Intent(getApplicationContext(),
                MyService.class);

        // Implement a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0,notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        // Implement a notification using above channel and pending intent
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)  // Cannot be dismissed by user
                .setContentTitle("Playing Music")
                .setContentText("Background Music service is running")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Show app screen", pendingIntent)
                .build();

        // Start service as a foreground service using the notification
        startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Music player notification";
        String description = "The channel for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel ;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private final MusicPlayerInterface.Stub mBinder = new MusicPlayerInterface.Stub() {

        // Implement the remote method
        @Override
        public void playMusic(int id){
            if (id >= 0 && id < musicList.size()) {
                int musicResource = musicList.get(id);
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                mPlayer = MediaPlayer.create(MyService.this, musicResource);
                mPlayer.start();
            }
        }
        @Override
        public void stopMusic(){
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
            }

        }

        @Override
        public void resume(){
            if (mPlayer != null && !mPlayer.isPlaying()) {
                mPlayer.start();
            }
        }
        @Override
        public void pause(){
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("CLIPSERVER", "Service ONSTARTcommand called!") ;
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("CLIPSERVER", "ONBind() executed") ;

        return mBinder;
    }


    @Override
    public void onDestroy() {
        Log.i("CLIPSERVER", "Servive was destroyed!") ;

        super.onDestroy();
            if (null != mPlayer) {
                mPlayer.stop();
                mPlayer.release();
            }

    }


}

