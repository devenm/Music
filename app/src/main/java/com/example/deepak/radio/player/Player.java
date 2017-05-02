package com.example.deepak.radio.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepak.radio.Bluer;
import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.R;
import com.example.deepak.radio.Songs;
import com.example.deepak.radio.adapters.RadioAdaptor;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.classes.SogListDialog;
import com.example.deepak.radio.radioplayer.RadioService;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {
    static LinearLayout alayout;
    static TextView songname, artist;
    Handler mHandler = new Handler();
    static ImageButton play, next, pre, pausebt, share, back;
    ImageButton suffle, repeat, fav, list;
    static ProgressBar pb;
    static SeekBar seekBar;
    static TextView firstduration, secduration;
    int pos;
    static ImageView songimage;
    String artistname;
    static Context context;
    boolean checkbitmap;
    static Bitmap defalultbt, urlbitmap;
    static String check;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        alayout = (LinearLayout) findViewById(R.id.layout);
        pos = getIntent().getIntExtra("pos", 0);
        Log.e("value", "c" + pos);
        init();
        context = getApplicationContext();
        sharedPreference = new SharedPreference();
        // setsong(pos,new Sdcard(Player.this).getPlayList());
        defalultbt = BitmapFactory.decodeResource(getResources(), R.drawable.kala);
        // new GetSongs().execute();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //   PlayerConstants1.SEEK_TO = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    PlayerConstants1.SEEK_TO = seekBar.getProgress();
                    Controls.seekToControl(Player.this);
                } catch (Exception e) {
                }
            }
        });


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
                Controls.playControl(getApplicationContext());
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
                Controls.pauseControl(getApplicationContext());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioUrl());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getAudioName());
                startActivity(Intent.createChooser(share, "Share Sound File"));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//new SogListDialog(Player.this,Songs.arrayList);
            }
        });
    }

    private void setFav() {
        String tag = fav.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(Player.this, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER));
            Toast.makeText(Player.this,
                    getResources().getString(R.string.add_fav),
                    Toast.LENGTH_SHORT).show();
            fav.setTag("red");
            fav.setColorFilter(Color.parseColor("#E91E63"));
        } else {
            sharedPreference.removeFavorite(Player.this, PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER));
            fav.setTag("grey");
            fav.setColorFilter(Color.parseColor("#ffffff"));
            Toast.makeText(Player.this,
                    getResources().getString(R.string.remove_fav),
                    Toast.LENGTH_SHORT).show();
        }
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
            play.setVisibility(View.VISIBLE);
        } else {
            pausebt.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    private static void updateUI() {
        try {
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
            artist.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtist());
            MainActivity.songname.setText(songName);
            MainActivity.mainArtist.setText("" + PlayerConstants1.SONGS_LIST1.get(PlayerConstants1.SONG_NUMBER).getArtist());

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
                MainActivity.mainimg.setImageBitmap(bitmap);
                BitmapDrawable background = new BitmapDrawable(blurredBitmap);
                if (background != null && alayout != null) {
                    alayout.setBackgroundDrawable(background);

                }

            } else {
                ImageViewAnimatedChange(context, songimage, defalultbt);
                // songimage.setImageBitmap(defalultbt); //associated cover art in bitmap
                MainActivity.mainimg.setImageBitmap(defalultbt);
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
            e.printStackTrace();
        }
    }

    private void getViews() {
        play = (ImageButton) findViewById(R.id.play);
        pausebt = (ImageButton) findViewById(R.id.pause);
        songname = (TextView) findViewById(R.id.songname);
        firstduration = (TextView) findViewById(R.id.firstduration);
        secduration = (TextView) findViewById(R.id.secduration);
        // pb = (ProgressBar) findViewById(R.id.beforeprogress);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        next = (ImageButton) findViewById(R.id.next);
        pre = (ImageButton) findViewById(R.id.previous);
        share = (ImageButton) findViewById(R.id.share);
        back = (ImageButton) findViewById(R.id.back);
        songimage = (ImageView) findViewById(R.id.songimage);
        artist = (TextView) findViewById(R.id.artist);
        suffle = (ImageButton) findViewById(R.id.suffle);
        repeat = (ImageButton) findViewById(R.id.repeate);
        fav = (ImageButton) findViewById(R.id.fav);
        list = (ImageButton) findViewById(R.id.list);

        if (pos<0)
        {
            Log.e("vcall"," "+pos);
        }
        else
            sendSong();
    }

    private void sendSong() {
        setsong(pos, Songs.arrayList);

    }

    public static void prog() {
        // pb.setVisibility(View.VISIBLE);
        pausebt.setVisibility(View.GONE);
        play.setVisibility(View.GONE);


    }

    public void setsong(final int position, ArrayList<AudioBean> feedItemList) {
        Intent i = new Intent(Player.this, RadioService.class);
        stopService(i);
        Log.e("url", "" + feedItemList.get(position).getAudioUrl());
        //  pb.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
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
        Controls.pauseControl(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), SongService1.class);
        intent.setAction(Constants.ACTION_PLAY);
        startService(intent);

        // Toast.makeText(MainActivity.this, "Buffering Please Wait..", Toast.LENGTH_LONG).show();
    }

    class GetSongs extends AsyncTask<String, String, ArrayList<AudioBean>> {
        @Override
        protected ArrayList<AudioBean> doInBackground(String... params) {
            return Songs.arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<AudioBean> audioBeen) {
            super.onPostExecute(audioBeen);
            setsong(pos, audioBeen);
            //  getTag(audioBeen,pos);
        }
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
}
