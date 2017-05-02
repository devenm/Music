package com.example.deepak.radio.radioplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.deepak.radio.Bluer;
import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.R;
import com.example.deepak.radio.adapters.RadioAdaptor;
import com.example.deepak.radio.classes.RedioBean;
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.player.Constants;
import com.example.deepak.radio.player.SongService1;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by deepak on 8/8/2016.
 */
public class RadioPlayer  extends AppCompatActivity {
    static LinearLayout alayout;
    static TextView songname,artist;
    Handler mHandler = new Handler();
    static ImageButton play, next, pre, pausebt,share,back;
    static ProgressBar pb;
    int pos;
    static ImageView songimage;
    String artistname;
    static Context context;
    boolean checkbitmap;
    static Bitmap defalultbt,urlbitmap;
    static String check;
    SharedPreference sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.radio_player);
        alayout= (LinearLayout) findViewById(R.id.layout);
        pos=getIntent().getIntExtra("pos",0);
        Log.e("value","c"+pos);
        init();
        context=getApplicationContext();
        sharedPreference = new SharedPreference();
        // setsong(pos,new Sdcard(Player.this).getPlayList());
        defalultbt = BitmapFactory.decodeResource(getResources(), R.drawable.kala);
        // new GetSongs().execute();



    }
    private void init() {
        getViews();
        setListner();
        changeUI();
    }
    private void setListner() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioControls.playControl(getApplicationContext());
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioControls.previousControl(getApplicationContext());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioControls.nextControl(getApplicationContext());
            }
        });
        pausebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioControls.pauseControl(getApplicationContext());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioUrl());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("Text/*");
                share.putExtra(Intent.EXTRA_TEXT,"Init Radio App listen "+RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioName());
                startActivity(Intent.createChooser(share, "Share"));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public static void changeUI() {
            updateUiurl();
            changeButton();
            updateseekbar();

    }

    private static void updateUiurl() {
        try {
            ImageLoader.getInstance().loadImage(RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getImgUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    urlbitmap = loadedImage;

                }

            });
            String songName = RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioName();
            songname.setText(songName);
            artist.setText("" + RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getArtist());
            MainActivity.songname.setText(songName);
            MainActivity.mainArtist.setText("" + RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getArtist());
        }
        catch (Exception e){
            Log.e("ER",""+e.toString());
        }
        try {
            if (RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getImgUrl()!=null) {
                Bitmap bitmap =urlbitmap;
                ImageViewAnimatedChange(context,songimage,bitmap);
                //songimage.setImageBitmap(bitmap); //associated cover art in bitmap
                MainActivity.mainimg.setImageBitmap(bitmap);
                songimage.setAdjustViewBounds(true);
                songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Bitmap blurredBitmap = new Bluer(context).blur1(bitmap);
                BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                if (background != null && alayout != null) {
                    alayout.setBackgroundDrawable(background);

                }

            }
            else {
                ImageViewAnimatedChange(context,songimage,defalultbt);
                // songimage.setImageBitmap(defalultbt); //associated cover art in bitmap
                songimage.setAdjustViewBounds(true);
                songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Bitmap blurredBitmap = new Bluer(context).blur1(defalultbt);
                MainActivity.mainimg.setImageBitmap(defalultbt);
                BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                if (background != null && alayout != null) {
                    alayout.setBackgroundDrawable(background);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateseekbar() {
        RadioPlayerConstants.PROGRESSBAR_HANDLER = new C03066();
    }

    static class C03066 extends Handler {
        C03066() {
        }

    }

    public static void changeButton() {
        if (RadioPlayerConstants.SONG_PAUSED) {
            pausebt.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        } else {
            pausebt.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
        }
    }
    private void getViews() {
        play = (ImageButton) findViewById(R.id.play);
        pausebt = (ImageButton) findViewById(R.id.pause);
        songname = (TextView) findViewById(R.id.songname);
         pb = (ProgressBar) findViewById(R.id.beforeprogress);
        next = (ImageButton) findViewById(R.id.next);
        pre = (ImageButton) findViewById(R.id.previous);
        share= (ImageButton) findViewById(R.id.share);
        back= (ImageButton) findViewById(R.id.back);
        songimage= (ImageView) findViewById(R.id.songimage);
        artist= (TextView) findViewById(R.id.artist);
        if (pos<0)
        {

        }
        else
        sendSong();
    }

    private void sendSong() {
            setsong(pos,RadioAdaptor.catlis);
    }

    public static void prog() {
        pb.setVisibility(View.VISIBLE);
        pausebt.setVisibility(View.GONE);
        play.setVisibility(View.GONE);


    }
    public void setsong(final int position, ArrayList<RedioBean> feedItemList) {
        Intent i = new Intent(RadioPlayer.this, SongService1.class);
        stopService(i);
        Log.e("url", "" + feedItemList.get(position).getAudioUrl());
        pb.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
        pausebt.setVisibility(View.GONE);
        RadioPlayerConstants.SONG_PAUSED = false;
        RadioPlayerConstants.SONG_CHANGED = false;
        RadioPlayerConstants.SONG_NUMBER = position;
        RadioPlayerConstants.SONGS_LIST1 = feedItemList;
//                        boolean isServiceRunning = UtilFunctions1.isServiceRunning(SongService1.class.getName(), getApplicationContext());
//                        if (!isServiceRunning) {
//                            Intent i = new Intent(getApplicationContext(), SongService1.class);
//                            startService(i);
//                        } else {
//                            try {
//
//                                PlayerConstants1.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants1.SONG_CHANGE_HANDLER.obtainMessage());
//                            }
//                            catch (Exception e){
//                                Log.e("srror h",e.toString());
//
//                            }
//
//                            }


        RadioControls.pauseControl(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), RadioService.class);
        intent.setAction(Constants.ACTION_PLAY);
        startService(intent);
        // Toast.makeText(MainActivity.this, "Buffering Please Wait..", Toast.LENGTH_LONG).show();
    }
    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(1000);
        anim_out.setDuration(1000);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}

