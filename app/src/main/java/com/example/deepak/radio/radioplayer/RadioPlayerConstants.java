package com.example.deepak.radio.radioplayer;

import android.os.Handler;

import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.RedioBean;

import java.util.ArrayList;

/**
 * Created by deepak on 8/8/2016.
 */
public class RadioPlayerConstants {
    public static ArrayList<RedioBean> SONGS_LIST1 = new ArrayList<RedioBean>();
    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //song changed (next, previous)
    public static boolean SONG_CHANGED = false;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
    public static int SEEK_TO;
    public static Handler SEEKBAR_HANDLER;
}
