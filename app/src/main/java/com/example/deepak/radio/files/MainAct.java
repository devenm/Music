package com.example.deepak.radio.files;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepak.radio.Album;
import com.example.deepak.radio.AllStation;
import com.example.deepak.radio.Artist;
import com.example.deepak.radio.Bluer;
import com.example.deepak.radio.Favourites;
import com.example.deepak.radio.R;
import com.example.deepak.radio.Songs;
import com.example.deepak.radio.adapters.RadioAdaptor;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.RedioBean;
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.classes.SogListDialog;
import com.example.deepak.radio.player.Constants;
import com.example.deepak.radio.player.Controls;
import com.example.deepak.radio.player.Player;
import com.example.deepak.radio.player.PlayerConstants1;
import com.example.deepak.radio.player.SongService1;
import com.example.deepak.radio.player.UtilFunctions1;
import com.example.deepak.radio.radioplayer.RadioControls;
import com.example.deepak.radio.radioplayer.RadioPlayer;
import com.example.deepak.radio.radioplayer.RadioPlayerConstants;
import com.example.deepak.radio.radioplayer.RadioService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 8/25/2016.
 */
public class MainAct  extends AppCompatActivity {
    static LinearLayout alayout,bottomlayout,seeklayout;
    static TextView songname, artistname;
    Handler mHandler = new Handler();
    static ImageButton playbt, next, pre, pausebt, share;
    ImageButton suffle, repeat, fav, list;
    static ProgressBar pb,ppb;
    static SeekBar seekBar;
    static TextView firstduration, secduration;
    int pos;
    static ImageView songimage;
    static Context context;
    boolean checkbitmap;
   public static Bitmap defalultbt, urlbitmap;
    static String check;
    SharedPreference sharedPreference;
    TabLayout tabLayout;
    ViewPager viewPager;
    Songs songs = new Songs();
    AllStation allStation = new AllStation();
    Artist artist = new Artist();
    Album album = new Album();
    Favourites favourites = new Favourites();
    public static TextView mainsongname, mainArtist;
    public static ImageView mainimg;
    public static ImageButton mainplay, mainpause;
    public static ProgressBar mainprog;
    public static FrameLayout playershow;
    static int posi;
    private static final String TAG = "DemoActivity";
    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_demo);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager.setOffscreenPageLimit(6);
        setUpWithPager();
        tabLayout.setupWithViewPager(viewPager);

        init();
      //  initR();
        context = getApplicationContext();
        sharedPreference = new SharedPreference();
        // setsong(pos,new Sdcard(Player.this).getPlayList());
        defalultbt = BitmapFactory.decodeResource(getResources(), R.drawable.kala);
        fav.setTag("grey");
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset==1.0) {
                }
                else if (slideOffset==0.0) {
                }
                if (isMyServiceRunning(RadioService.class))
                {
                    seeklayout.setVisibility(View.GONE);
                    bottomlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e(TAG, "onPanelStateChanged " + newState);
                // t.setText("helloooooo");
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mainplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.playControl(getApplicationContext());
                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.playControl(getApplicationContext());
                else ;
            }
        });
        mainpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.pauseControl(getApplicationContext());

                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.pauseControl(getApplicationContext());
                else ;
            }
        });
        netCheck();
    }
    private void setUpWithPager() {
        ViewPageAdap viewPageAdap = new ViewPageAdap(getSupportFragmentManager());
        viewPageAdap.add(songs, "   Songs   ");
        viewPageAdap.add(artist, "     Artist   ");
        viewPageAdap.add(album, "   Album    ");
        viewPageAdap.add(favourites, "  Favourites  ");
        viewPager.setAdapter(viewPageAdap);
    }

    public class ViewPageAdap extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public ViewPageAdap(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            posi = position;
            return fragmentArrayList.get(position);

        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public void add(Fragment fragment, String s) {
            stringArrayList.add(s);
            fragmentArrayList.add(fragment);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void netCheck() {
        if (isNetworkAvailable())
        {}
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Network Error");
            builder.setMessage("please check your network connection");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            });
            builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
    private void init() {
        getViews();
        setListner();
        changeUI();
    }
    private void setListner() {
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.playControl(getApplicationContext());
                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.playControl(getApplicationContext());
                //Controls.playControl(getApplicationContext());
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(getApplicationContext());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(getApplicationContext());
            }
        });
        pausebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.pauseControl(getApplicationContext());

                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.pauseControl(getApplicationContext());
                else ;
               // Controls.pauseControl(getApplicationContext());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class)) {
                    Uri uri = Uri.parse(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioUrl());
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/*");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_TEXT, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName());
                    startActivity(Intent.createChooser(share, "Share Sound File"));
                }
                else if (isMyServiceRunning(RadioService.class)){
                    Uri uri = Uri.parse(RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioUrl());
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("Text/*");
                    share.putExtra(Intent.EXTRA_TEXT,"Init Radio App listen "+RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioName());
                    startActivity(Intent.createChooser(share, "Share"));
                }
            }
        });

        suffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suffle.setColorFilter(Color.parseColor("#111111"));
                ArrayList<AudioBean> arrayList=new ArrayList<AudioBean>();
                for (int i=0;i<Songs.arrayList.size();i++){
                    AudioBean audioBean=Songs.arrayList.get(new Random().nextInt(Songs.arrayList.size()));
                    arrayList.add(audioBean);

                }
                setsong(PlayerConstants1.SONG_NUMBER,arrayList);
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat.setColorFilter(Color.parseColor("#111111"));
                ArrayList<AudioBean> arrayList=new ArrayList<AudioBean>();
                AudioBean audioBean=Songs.arrayList.get(PlayerConstants1.SONG_NUMBER);
                arrayList.add(audioBean);
                setsong(0,arrayList);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFav();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SogListDialog(MainAct.this,Songs.arrayList);
            }
        });
        seeklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        songname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        artistname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public static void changeUI() {
        updateUI();
        changeButton();
        updateseekbar();
    }


    private static void updateseekbar() {
        PlayerConstants1.PROGRESSBAR_HANDLER = new C03066();
    }

    static class C03066 extends Handler {
        C03066() {
        }

        public void handleMessage(Message msg) {
            Log.e("called", "handler" + msg);
            Integer[] i = (Integer[]) msg.obj;
            firstduration.setText(UtilFunctions1.getDuration1((long) i[0].intValue()));
            seekBar.setProgress(i[0].intValue());
            seekBar.setMax(i[1].intValue());
            long milliseconds = UtilFunctions1.getDuration11(i[1].intValue());
            if (TimeUnit.MILLISECONDS.toMinutes((long) milliseconds) > 100)
                secduration.setText("00:00");
            else {
                String time = (String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
                        TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) milliseconds))));
                secduration.setText(time);
            }

        }
    }

    public static void changeButton() {
        if (PlayerConstants1.SONG_PAUSED) {
            pausebt.setVisibility(View.GONE);
            playbt.setVisibility(View.VISIBLE);
            mainplay.setVisibility(View.VISIBLE);
            mainpause.setVisibility(View.GONE);

        } else {
            pausebt.setVisibility(View.VISIBLE);
            playbt.setVisibility(View.GONE);
            mainplay.setVisibility(View.GONE);
            mainpause.setVisibility(View.VISIBLE);
        }
    }

    private static void updateUI() {
        try {
            seeklayout.setVisibility(View.VISIBLE);
            bottomlayout.setVisibility(View.VISIBLE);
            /*for (AudioBean myPoint : PlayerConstants1.SONGS_LIST1) {
                if (myPoint.getAudioName() != null && myPoint.getAudioName().equals(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName())) {
                    Log.e("stag", "red");
                    fav.setTag("red");
                } else {
                    Log.e("stag", "red");
                    fav.setTag("grey");
                }
            }*/
            //pb.setVisibility(View.GONE);
            String songName = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName();
            songname.setText(songName);
            mainsongname.setText(songName);
            artistname.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtist());
           mainArtist.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtist());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtistimag() != null) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtistimag(), 0, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtistimag().length);
                ImageViewAnimatedChange(context, songimage, bitmap);
                //songimage.setImageBitmap(bitmap); //associated cover art in bitmap
                songimage.setAdjustViewBounds(true);
                songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Bitmap blurredBitmap = new Bluer(context).blur1(bitmap);
                mainimg.setImageBitmap(bitmap);
                BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                if (background != null && alayout != null) {
                    alayout.setBackgroundDrawable(background);

                }

            } else {
                ImageViewAnimatedChange(context, songimage, defalultbt);
                // songimage.setImageBitmap(defalultbt); //associated cover art in bitmap
                mainimg.setImageBitmap(defalultbt);
                songimage.setAdjustViewBounds(true);
                songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Bitmap blurredBitmap = new Bluer(context).blur1(defalultbt);
                BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                if (background != null && alayout != null) {
                    alayout.setBackgroundDrawable(background);

                }
            }
        } catch (Exception e) {
           Log.e("imger",""+e.toString());
        }
    }

    private void getViews() {
        playershow = (FrameLayout) findViewById(R.id.layout);
        mainplay = (ImageButton) findViewById(R.id.mainplay);
        mainimg = (ImageView) findViewById(R.id.stripimage);
        mainsongname = (TextView) findViewById(R.id.mainsongname);
        mainpause = (ImageButton) findViewById(R.id.pausem);
        mainprog = (ProgressBar) findViewById(R.id.songprog);
        mainArtist = (TextView) findViewById(R.id.mainartist);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        playbt = (ImageButton) findViewById(R.id.play);
        pausebt = (ImageButton) findViewById(R.id.pause);
        songname = (TextView) findViewById(R.id.songname);
        firstduration = (TextView) findViewById(R.id.firstduration);
        secduration = (TextView) findViewById(R.id.secduration);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        next = (ImageButton) findViewById(R.id.next);
        pre = (ImageButton) findViewById(R.id.previous);
        share = (ImageButton) findViewById(R.id.share);
        songimage = (ImageView) findViewById(R.id.songimage);
        artistname = (TextView) findViewById(R.id.artist);
        suffle = (ImageButton) findViewById(R.id.suffle);
        repeat = (ImageButton) findViewById(R.id.repeate);
        fav = (ImageButton) findViewById(R.id.fav);
        list = (ImageButton) findViewById(R.id.list);
        alayout = (LinearLayout) findViewById(R.id.layout1);
        bottomlayout= (LinearLayout) findViewById(R.id.bottomlayout);
        seeklayout= (LinearLayout) findViewById(R.id.seeklayout);
        pb = (ProgressBar) findViewById(R.id.songprog);
        ppb= (ProgressBar) findViewById(R.id.psongprog);
    }


    public static void prog() {
        // pb.setVisibility(View.VISIBLE);
        pausebt.setVisibility(View.GONE);
        playbt.setVisibility(View.GONE);


    }

    public void setsong(final int position, ArrayList<AudioBean> feedItemList) {
        Intent i = new Intent(MainAct.this, RadioService.class);
        stopService(i);
        Log.e("url", "" + feedItemList.get(position).getAudioUrl());
        playbt.setVisibility(View.GONE);
        pausebt.setVisibility(View.GONE);
        PlayerConstants1.SONG_PAUSED = false;
        PlayerConstants1.SONG_CHANGED = false;
        PlayerConstants1.SONG_NUMBER = position;
        PlayerConstants1.SONGS_LIST1 = feedItemList;
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


        //  Log.e("songid", item.getAudioId());
        Intent intent = new Intent(getApplicationContext(), SongService1.class);
        intent.setAction(Constants.ACTION_PLAY);
        startService(intent);

        // Toast.makeText(MainActivity.this, "Buffering Please Wait..", Toast.LENGTH_LONG).show();
    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(1000);
        anim_out.setDuration(1000);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
    private void setFav() {
        String tag = fav.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(MainAct.this, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER));
            Toast.makeText(MainAct.this,
                    getResources().getString(R.string.add_fav),
                    Toast.LENGTH_SHORT).show();
            fav.setTag("red");
            fav.setColorFilter(Color.parseColor("#E91E63"));
        } else {
            sharedPreference.removeFavorite(MainAct.this, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER));
            fav.setTag("grey");
            fav.setColorFilter(Color.parseColor("#ffffff"));
            Toast.makeText(MainAct.this,
                    getResources().getString(R.string.remove_fav),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void initR() {
        getViewsR();
        //setListnerR();
        changeUIR();
    }
    private void setListnerR() {
        playbt.setOnClickListener(new View.OnClickListener() {
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

    }



    public static void changeUIR() {
        updateUiurlR();
        changeButtonR();
        updateseekbarR();

    }

    private static void updateUiurlR() {
        try {
            seeklayout.setVisibility(View.GONE);
            bottomlayout.setVisibility(View.GONE);
            pre.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            ImageLoader.getInstance().loadImage(RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getImgUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    urlbitmap = loadedImage;
                    mainimg.setImageBitmap(loadedImage);
                    songimage.setImageBitmap(loadedImage);
                    try {
                        if (RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getImgUrl()!=null) {
                            Bitmap bitmap =urlbitmap;
                           // ImageViewAnimatedChangeR(context,songimage,bitmap);
                            //songimage.setImageBitmap(bitmap); //associated cover art in bitmap
                            mainimg.setImageBitmap(bitmap);
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
                           // ImageViewAnimatedChangeR(context,songimage,defalultbt);
                            // songimage.setImageBitmap(defalultbt); //associated cover art in bitmap
                            songimage.setAdjustViewBounds(true);
                            songimage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            songimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            Bitmap blurredBitmap = new Bluer(context).blur1(defalultbt);
                            mainimg.setImageBitmap(defalultbt);
                            BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                            if (background != null && alayout != null) {
                                alayout.setBackgroundDrawable(background);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            String songName = RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getAudioName();
            songname.setText(songName);
            artistname.setText("" + RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getArtist());
        mainsongname.setText(songName);
          mainArtist.setText("" + RadioPlayerConstants.SONGS_LIST1.get(RadioPlayerConstants.SONG_NUMBER).getArtist());
        }
        catch (Exception e){
            Log.e("ER",""+e.toString());
        }

    }

    private static void updateseekbarR() {
        RadioPlayerConstants.PROGRESSBAR_HANDLER = new C030660();
    }

    static class C030660 extends Handler {
        C030660() {
        }

    }

    public static void changeButtonR() {
        if (RadioPlayerConstants.SONG_PAUSED) {
            pausebt.setVisibility(View.GONE);
            playbt.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            ppb.setVisibility(View.GONE);
            mainplay.setVisibility(View.VISIBLE);
            mainpause.setVisibility(View.GONE);
        } else {
            pausebt.setVisibility(View.VISIBLE);
            playbt.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            ppb.setVisibility(View.GONE);
            mainplay.setVisibility(View.GONE);
            mainpause.setVisibility(View.VISIBLE);
        }
    }
    private void getViewsR() {
        playbt = (ImageButton) findViewById(R.id.play);
        pausebt = (ImageButton) findViewById(R.id.pause);
        songname = (TextView) findViewById(R.id.songname);
        pb = (ProgressBar) findViewById(R.id.songprog);
        next = (ImageButton) findViewById(R.id.next);
        pre = (ImageButton) findViewById(R.id.previous);
        share= (ImageButton) findViewById(R.id.share);
        songimage= (ImageView) findViewById(R.id.songimage);
        artistname= (TextView) findViewById(R.id.artist);
        bottomlayout= (LinearLayout) findViewById(R.id.bottomlayout);
        seeklayout= (LinearLayout) findViewById(R.id.seeklayout);

    }

    public static void progR() {
        ppb.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        pausebt.setVisibility(View.GONE);
        playbt.setVisibility(View.GONE);
mainplay.setVisibility(View.GONE);
        mainpause.setVisibility(View.GONE);

    }
    public void setsongR(final int position, ArrayList<RedioBean> feedItemList) {
        Intent i = new Intent(MainAct.this, SongService1.class);
        stopService(i);
        Log.e("url", "" + feedItemList.get(position).getAudioUrl());
        playbt.setVisibility(View.GONE);
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
    public static void ImageViewAnimatedChangeR(Context c, final ImageView v, final Bitmap new_image) {
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
