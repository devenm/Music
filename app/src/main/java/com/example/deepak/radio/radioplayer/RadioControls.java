package com.example.deepak.radio.radioplayer;

import android.content.Context;

import com.example.deepak.radio.R;

/**
 * Created by deepak on 8/8/2016.
 */
public class RadioControls {
    static String LOG_CLASS = "Controls";

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
    }

    public static void seekToControl(Context context) {
        sendSeekBarMessage(context.getResources().getString(R.string.sek_to));
    }

    public static void nextControl(Context context) {
        boolean isServiceRunning = RadioUtilFunction.isServiceRunning(RadioService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (RadioPlayerConstants.SONGS_LIST1.size() > 0) {
            if (RadioPlayerConstants.SONG_NUMBER < (RadioPlayerConstants.SONGS_LIST1.size() - 1)) {
                RadioPlayerConstants.SONG_NUMBER++;
                RadioPlayerConstants.SONG_CHANGE_HANDLER.sendMessage(RadioPlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                RadioPlayerConstants.SONG_NUMBER = 0;
                RadioPlayerConstants.SONG_CHANGE_HANDLER.sendMessage(RadioPlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        RadioPlayerConstants.SONG_PAUSED = false;
    }

    public static void previousControl(Context context) {
        boolean isServiceRunning = RadioUtilFunction.isServiceRunning(RadioService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (RadioPlayerConstants.SONGS_LIST1.size() > 0) {
            if (RadioPlayerConstants.SONG_NUMBER > 0) {
                RadioPlayerConstants.SONG_NUMBER--;
                RadioPlayerConstants.SONG_CHANGE_HANDLER.sendMessage(RadioPlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                RadioPlayerConstants.SONG_NUMBER = RadioPlayerConstants.SONGS_LIST1.size() - 1;
                RadioPlayerConstants.SONG_CHANGE_HANDLER.sendMessage(RadioPlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        RadioPlayerConstants.SONG_PAUSED = false;
    }

    private static void sendMessage(String message) {
        try {
            RadioPlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(RadioPlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
        }
    }

    private static void sendSeekBarMessage(String message) {
        try {
            RadioPlayerConstants.SEEKBAR_HANDLER.sendMessage(RadioPlayerConstants.SEEKBAR_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
        }
    }
}