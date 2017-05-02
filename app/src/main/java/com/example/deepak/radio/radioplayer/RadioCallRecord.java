package com.example.deepak.radio.radioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;


/**
 * Created by deepak on 8/8/2016.
 */
public class RadioCallRecord extends BroadcastReceiver {
    TelephonyManager telManager;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                try {
                    RadioControls.pauseControl(context);
                } catch (Exception e) {
                }

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                try {
                    RadioControls.pauseControl(context);
                } catch (Exception e) {
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                try {
                    RadioControls.playControl(context);

                } catch (Exception e) {
                }
            }

        }
    }
}