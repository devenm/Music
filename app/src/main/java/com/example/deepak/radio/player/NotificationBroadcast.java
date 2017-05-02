package com.example.deepak.radio.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;


public class NotificationBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("broadstart","dfsf");
		if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                	if(!PlayerConstants1.SONG_PAUSED){
    					Controls.pauseControl(context);
                	}else{
    					Controls.playControl(context);
                	}
                	break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
					Log.e("play","playyyyyy1");
                	break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
					Log.e("play","playyyyyy2");
                	break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
					Log.e("play","playyyyyy3");
                	break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                	Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                	Controls.nextControl(context);
                	break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                	Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                	Controls.previousControl(context);
                	break;
            }
		}  else{
            	if (intent.getAction().equals(SongService1.NOTIFY_PLAY)) {
					//PlayerDialog.mMediaPlayer.start();
    			Log.e("play","playyyyyy4");
    					Controls.playControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_PAUSE)) {
					//PlayerDialog.mMediaPlayer.pause();
					Log.e("play","playyyyyy5");
    				Controls.pauseControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_NEXT)) {
					Log.e("play","playyyyyy6");
        			Controls.nextControl(context);
        		} else if (intent.getAction().equals(SongService1.NOTIFY_DELETE)) {
					Log.e("play","playyyyyy6");
					Intent i = new Intent(context, SongService1.class);
					context.stopService(i);
//					Intent in = new Intent(context, MainActivity.class);
//			        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			        context.startActivity(in);
					/*Intent intentt=new Intent(context,PopUP.class);
					intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intentt);*/

        		}else if (intent.getAction().equals(SongService1.NOTIFY_PREVIOUS)) {
    				Controls.previousControl(context);
        		}


		}
	}
	
	public String ComponentName() {
		return this.getClass().getName(); 
	}
}
