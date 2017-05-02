package com.example.deepak.radio;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepak.radio.player.Controls;
import com.example.deepak.radio.player.Player;
import com.example.deepak.radio.player.PlayerConstants1;
import com.example.deepak.radio.player.SongService1;
import com.example.deepak.radio.radioplayer.RadioControls;
import com.example.deepak.radio.radioplayer.RadioPlayer;
import com.example.deepak.radio.radioplayer.RadioPlayerConstants;
import com.example.deepak.radio.radioplayer.RadioService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Songs songs = new Songs();
    AllStation allStation = new AllStation();
    Artist artist = new Artist();
    Album album = new Album();
    Favourites favourites = new Favourites();
    public static TextView songname, mainArtist;
    public static ImageView mainimg;
    public static ImageButton play, pause;
    public static ProgressBar prog;
    public static FrameLayout playershow;
    static int posi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager.setOffscreenPageLimit(6);
        setUpWithPager();
        tabLayout.setupWithViewPager(viewPager);
        playershow = (FrameLayout) findViewById(R.id.layout);
        play = (ImageButton) findViewById(R.id.mainplay);
        mainimg = (ImageView) findViewById(R.id.stripimage);
        songname = (TextView) findViewById(R.id.mainsongname);
        pause = (ImageButton) findViewById(R.id.pause);
        prog = (ProgressBar) findViewById(R.id.songprog);
        mainArtist = (TextView) findViewById(R.id.mainartist);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.playControl(getApplicationContext());
                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.playControl(getApplicationContext());
                else ;
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class))
                    Controls.pauseControl(getApplicationContext());

                else if (isMyServiceRunning(RadioService.class))
                    RadioControls.pauseControl(getApplicationContext());
                else ;
            }
        });
        playershow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(SongService1.class)) {
                    Intent intent = new Intent(MainActivity.this, Player.class);
                    intent.putExtra("pos", -1);
                    startActivity(intent);

                } else if (isMyServiceRunning(RadioService.class)) {

                    Intent intent = new Intent(MainActivity.this, RadioPlayer.class);
                    intent.putExtra("pos", -1);
                    startActivity(intent);

                } else
                    Toast.makeText(MainActivity.this, "No items selected  ", Toast.LENGTH_LONG).show();
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

    public static void changeUI() {
        //  updateUI();
        changeButton();
    }

    public static void changeButton() {
        try {
            if (prog != null)
                prog.setVisibility(View.GONE);
            if (PlayerConstants1.SONG_PAUSED) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            } else {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("", "");
        }
    }

    public static void changeButtonR() {
        try {
            if (prog != null)
                prog.setVisibility(View.GONE);
            if (RadioPlayerConstants.SONG_PAUSED) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            } else {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("", "");
        }
    }

    public static void setMainimg(String nircover) {
        if (mainimg != null && nircover != null)
            ImageLoader.getInstance().loadImage(nircover, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    // mainimg.setImageBitmap(loadedImage);

                }

            });
        else {
        }
    }

    private static void updateUI() {

        try {
            String songName = PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName();
            songname.setText(songName);
            Log.e("data", songName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {


        } catch (Exception e) {
            e.printStackTrace();
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
}
