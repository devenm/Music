package com.example.deepak.radio.player;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RemoteControlClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


import com.example.deepak.radio.R;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.files.MainAct;


import java.util.Timer;
import java.util.TimerTask;


public class SongService1 extends Service implements AudioManager.OnAudioFocusChangeListener {
    String LOG_CLASS = "SongService";
public  static MediaPlayer mediaPlayer;
    int NOTIFICATION_ID = 1111;
    public static final String NOTIFY_PREVIOUS = "com.init.khalsaji.audioplayer.previous";
    public static final String NOTIFY_DELETE = "com.init.khalsaji.audioplayer.delete";
    public static final String NOTIFY_PAUSE = "com.init.khalsaji.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.init.khalsaji.audioplayer.play";
    public static final String NOTIFY_NEXT = "com.init.khalsaji.audioplayer.next";
    public static final String NOTIFY_START = "com.init.khalsaji.audioplayer.share";
    private ComponentName remoteComponentName;
    private RemoteControlClient remoteControlClient;
    AudioManager audioManager;
    Bitmap mDummyAlbumArt;
    private static Timer timer;
    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;
    long sleptime;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVersionSupportBigNotification = UtilFunctions1.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions1.currentVersionSupportLockScreenControls();
        timer = new Timer();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(PlayerConstants1.SONG_CHANGED==true) {
                    Controls.nextControl(getApplicationContext());
                    MainAct.prog();
                    PlayerConstants1.SONG_CHANGED=false;
                }
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //if ()
                return false;
            }
        });
        super.onCreate();
    }


    /**
     * Send message from timer
     * @author jonty.ankit
     */
    private class MainTask extends TimerTask{
        public void run(){
            handler.sendEmptyMessage(0);
        }
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(mediaPlayer != null&&mediaPlayer.isPlaying()){
                try {
                    Log.e("duration",mediaPlayer+" "+mediaPlayer.getCurrentPosition()+" "+mediaPlayer.getDuration());
                    int progress = (mediaPlayer.getCurrentPosition() * 100) / mediaPlayer.getDuration();
                    Integer i[] = new Integer[3];
                    i[0] = mediaPlayer.getCurrentPosition();
                    i[1] = mediaPlayer.getDuration();
                    i[2] = progress;
                    PlayerConstants1.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants1.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                }catch(Exception e){
                    Log.e("err",e.toString());
                }
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.e("start","dfdsf");
            if(PlayerConstants1.SONGS_LIST1.size() <= 0){

			/*//	PlayerConstants1.SONGS_LIST = UtilFunctions1.listOfSongs(getApplicationContext());
				//PlayerConstants1.SONGS_LIST1=AudioAdap.feedItemList;
				PlayerConstants1.SONGS_LIST1=UtilFunctions1.listOfSongs1(getApplicationContext());*/
            }
            //MediaItem data = PlayerConstants1.SONGS_LIST.get(PlayerConstants1.SONG_NUMBER);
            AudioBean data1=PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER);
            if(currentVersionSupportLockScreenControls){
                RegisterRemoteClient();
            }
          //  String songPath = "http://52.74.238.77/savaan_nirmolak/assets/songs/1450425118_01.Pragtio_Khalsa.mp3";
            //playSong(songPath, data);
           String songPath=data1.getAudioUrl();
            playSong1(songPath,data1);
            newNotification();

            PlayerConstants1.SONG_CHANGE_HANDLER = new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    AudioBean data = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER);
                    String songPath = data.getAudioUrl();
                    newNotification();
                    try{
                        playSong1(songPath, data);
                        MainAct.changeUI();
                       // MainActivity.changeUI();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            PlayerConstants1.SEEKBAR_HANDLER=new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String message = (String) msg.obj;
                    if (mediaPlayer != null && message.equalsIgnoreCase(SongService1.this.getResources().getString(R.string.sek_to))) {
                        mediaPlayer.seekTo(PlayerConstants1.SEEK_TO);

                        return false;
                    }
                    return false;
                }
            });
            PlayerConstants1.PLAY_PAUSE_HANDLER = new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String message = (String)msg.obj;
                    if(mediaPlayer == null)
                        return false;
                    if(message.equalsIgnoreCase(getResources().getString(R.string.play))){
                        PlayerConstants1.SONG_PAUSED = false;
                        if(currentVersionSupportLockScreenControls){
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                        }
                        mediaPlayer.start();
                    }else if(message.equalsIgnoreCase(getResources().getString(R.string.pause))){
                        PlayerConstants1.SONG_PAUSED = true;
                        if(currentVersionSupportLockScreenControls){
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                        }
                        mediaPlayer.pause();
                    }
                    newNotification();
                    try{
                        MainAct.changeButton();
                       // MainActivity.changeButton();
                    }catch(Exception e){}
                    Log.d("TAG", "TAG Pressed: " + message);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    /**
     * Notification
     * Custom Bignotification is available from API 16
     */
    @SuppressLint("NewApi")
    public void newNotification() {
        String songName = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName();
        String albumName =PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtist();
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.notification);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.bignotification);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("InitRadio").build();
        notification.priority=Notification.PRIORITY_MAX;
        setListeners(simpleContentView);
        setListeners(expandedView);
        //notification.bigContentView = expandedView;
        notification.contentView = simpleContentView;
        if(currentVersionSupportBigNotification){
            notification.bigContentView = expandedView;
        }
        try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtistimag(), 0, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtistimag().length);
            //long albumId = PlayerConstants1.SONGS_LIST.get(PlayerConstants1.SONG_NUMBER).getAlbumId();
            Bitmap albumArt = bitmap;
            if (albumArt != null){
                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                if(currentVersionSupportBigNotification){
                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                }
            }else{
                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.ic_launcher);
                if(currentVersionSupportBigNotification){
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.ic_launcher);
                }
            }
        }catch(Exception e){
            Log.e("service error",e.toString());
            e.printStackTrace();
        }
        if(PlayerConstants1.SONG_PAUSED){
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            }
        }else{
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);
            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            }
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
        if(currentVersionSupportBigNotification){
            notification.bigContentView.setTextViewText(R.id.textSongName, songName);
            notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        try {
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            startForeground(NOTIFICATION_ID, notification);
        }
        catch (Exception e){
            Log.e("","");
        }
    }

    /**
     * Notification click listeners
     * @param view
     */
    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent start=new Intent(NOTIFY_START);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

        PendingIntent pStart = PendingIntent.getBroadcast(getApplicationContext(), 0, start, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.imageViewAlbumArt, pStart);
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    /**
     * Play song, Update Lockscreen fields
     * @param songPath
     * @param data
     */
    @SuppressLint("NewApi")

    private void playSong1(String songPath,AudioBean data) {
        try {
            MainAct.prog();
            if(currentVersionSupportLockScreenControls){
                UpdateMetadata1(data);
                remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mpp) {
                    mpp.start();
                    Controls.pauseControl(getApplicationContext());
                    if (isConnectedWifi(getApplicationContext())) {
                        Log.e("wifi", "" + isConnectedWifi(getApplicationContext()));
                        sleptime = 100;
                        timer.scheduleAtFixedRate(new MainTask(), 0, 100);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Controls.playControl(getApplicationContext());
                                PlayerConstants1.SONG_CHANGED = true;
                                MainAct.changeUI();
                              //  MainActivity.changeUI();
                            }
                        }, sleptime);
                    } else if (isConnectedMobile(getApplicationContext())) {
                        Log.e("mob", "" + isConnectedMobile(getApplicationContext()));
                        if (isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                            Log.e("speed","2g");
                            sleptime = 100;
                            timer.scheduleAtFixedRate(new MainTask(), 0, 100);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Controls.playControl(getApplicationContext());
                                    PlayerConstants1.SONG_CHANGED = true;
                                    MainAct.changeUI();
                                  //  MainActivity.changeUI();
                                }
                            }, sleptime);
                        }
                        if (isConnectionFast(ConnectivityManager.TYPE_MOBILE, 5) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 6) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 7) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 8) && isConnectionFast(ConnectivityManager.TYPE_MOBILE, 9)&&isConnectionFast(ConnectivityManager.TYPE_MOBILE, 9)) {
                            Log.e("speed","3g");
                            sleptime = 100;
                            timer.scheduleAtFixedRate(new MainTask(), 0, 100);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Controls.playControl(getApplicationContext());
                                    PlayerConstants1.SONG_CHANGED = true;
                                    MainAct.changeUI();
//MainActivity.changeUI();
                                }
                            }, sleptime);
                        }
                    }
                    else {
                        timer.scheduleAtFixedRate(new MainTask(), 0, 100);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Controls.playControl(getApplicationContext());
                                PlayerConstants1.SONG_CHANGED = true;
                                MainAct.changeUI();
                            }
                        }, 1000);
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    private void RegisterRemoteClient(){
        remoteComponentName = new ComponentName(getApplicationContext(), new NotificationBroadcast().ComponentName());
        try {
            if(remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        }catch(Exception ex) {
        }
    }

    @SuppressLint("NewApi")



    private void UpdateMetadata1(AudioBean data){
        if (remoteControlClient == null)
            return;
        RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
        //metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, data.getAlbum());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, data.getAudioId());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, data.getAudioName());
        //	mDummyAlbumArt = NirmolakPlayer.albumcover;
        if(mDummyAlbumArt == null){
            mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        metadataEditor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, mDummyAlbumArt);
        metadataEditor.apply();
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }
    @Override
    public void onAudioFocusChange(int focusChange) {

    }
    public static boolean isConnected(Context context){
        NetworkInfo info =getNetworkInfo(context);
        return (info != null && info.isConnected());
    }
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return true;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return true; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}