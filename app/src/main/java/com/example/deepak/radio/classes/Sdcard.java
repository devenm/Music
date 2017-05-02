package com.example.deepak.radio.classes;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.deepak.radio.R;
import com.example.deepak.radio.Songs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deepak on 2/22/2016.getCacheDir()
 */
public class Sdcard {
    final String MEDIA_PATH;
    Context context;
    byte[] data;
Activity activity;
    android.media.MediaMetadataRetriever mmr;
    /* final String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/khalsaji/";*/
   /* final String MEDIA_PATH = Environment.getE
            .getPath() + "/Android/data/com.init.khalsaji/cache/";*/
//String sp=Environment.ge
    ArrayList<AudioBean> list = new ArrayList<>();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";

    // Constructor
    public Sdcard(Context applicationContext) {
        context = applicationContext;
       /* MEDIA_PATH  = context.getExternalCacheDir()
            .getPath();
        Log.e("path",""+MEDIA_PATH);*/
        // MEDIA_PATH = Environment.getExternalStorageDirectory().getPath();
        MEDIA_PATH = System.getenv("SECONDARY_STORAGE");
     mmr=new MediaMetadataRetriever();
        activity= (Activity) applicationContext;
    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     */
    public ArrayList<AudioBean> getPlayList() {
        System.out.println(MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            home.setReadable(true, false);
            File[] listFiles = home.listFiles();
            Log.e("mediapath", "notnull" + home + " " + listFiles);
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return list;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);

                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            AudioBean audioBean = new AudioBean();
            HashMap<String, String> songMap = new HashMap<String, String>();
            songMap.put("songTitle",
                    song.getName().substring(0, (song.getName().length() - 4)));
            songMap.put("songPath", song.getPath());
            audioBean.setAudioName(song.getName().substring(0, (song.getName().length() - 4)));
            audioBean.setAudioUrl(song.getPath());
            // Adding each song to SongList
            // songsList.add(songMap);
            //coverart is an Imageview object
            // convert the byte array to a bitmap
            try {

                if (song.getPath()!=null) {
                    mmr.setDataSource(song.getPath());
                    data = mmr.getEmbeddedPicture();
                    Log.e("patt",""+data);
                }
                else
                    data=null;
                if (data != null) {
                   // Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    audioBean.setArtistimag(data);
                    audioBean.setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                } else {

                    audioBean.setArtistimag(data);
                    audioBean.setArtist("Unknown");

                }
            }
            catch (Exception e){
                Log.e("SongsError",""+e.toString());
            }
            list.add(audioBean);
        }
    }

    public ArrayList<ArtistInfo> getArtist() {
        ArrayList<ArtistInfo> artistInfos = new ArrayList<>();
        for (int i = 0; i <= getPlayList().size(); i++) {
            artistInfos = getTag(getPlayList(), i);
        }
        return artistInfos;
    }

    public ArrayList<ArtistInfo> getTag(ArrayList<AudioBean> list, int pos) {
        ArrayList<ArtistInfo> artistInfos = new ArrayList<>();
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(list.get(pos).getAudioUrl());
        byte[] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object
        // convert the byte array to a bitmap
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ArtistInfo artistInfo = new ArtistInfo();
            artistInfo.setArtistImage(bitmap);
            artistInfo.setArtistname(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            artistInfos.add(artistInfo);
        } else {
            Bitmap bt = BitmapFactory.decodeResource(context.getResources(), R.drawable.kala);
            ArtistInfo artistInfo = new ArtistInfo();
            artistInfo.setArtistImage(bt);
            artistInfo.setArtistname(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }
        return artistInfos;
    }

   /* class GetSong extends AsyncTask<File, AudioBean, Void> {
        File songs;

        @Override
        protected Void doInBackground(File... params) {
            songs = params[0];
            if (songs.getName().endsWith(mp3Pattern)) {
                AudioBean audioBean = new AudioBean();
                HashMap<String, String> songMap = new HashMap<String, String>();
                songMap.put("songTitle",
                        songs.getName().substring(0, (songs.getName().length() - 4)));
                songMap.put("songPath", songs.getPath());
                audioBean.setAudioName(songs.getName().substring(0, (songs.getName().length() - 4)));
                audioBean.setAudioUrl(songs.getPath());
                // Adding each song to SongList
                // songsList.add(songMap);

                //coverart is an Imageview object
                // convert the byte array to a bitmap
                try {
                    if (songs.getPath()!=null) {
                        mmr.setDataSource(songs.getPath());
                        data = mmr.getEmbeddedPicture();
                    }
                    else
                    data=null;
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        audioBean.setArtistimag(bitmap);
                        audioBean.setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    } else {
                        Bitmap bt = BitmapFactory.decodeResource(context.getResources(), R.mipmap.heart);
                        audioBean.setArtistimag(bt);
                        audioBean.setArtist("Unknown");

                    }
                } catch (Exception e) {
                    Log.e("SongsError", "" + e.toString());
                }
                list.add(audioBean);
            }
return null;
        }
    }*/
   public ArrayList<AudioBean> getAllSongs() {

       Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
       String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

       String[] STAR=null;
       Cursor cursor = activity.getContentResolver().query(allsongsuri, STAR, selection, null, null);
       //or
       //Cursor cursor = getActivity().getContentResolver().query(allsongsuri, null, null, null, selection);




       if (cursor != null) {
           if (cursor.moveToFirst()) {
               do {
                   AudioBean audioBean=new AudioBean();
                   String song_name = cursor
                           .getString(cursor
                                   .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                   int song_id = cursor.getInt(cursor
                           .getColumnIndex(MediaStore.Audio.Media._ID));

                   String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                   String Duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
String artname=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                   String album_name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                   String album_id=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                   String artist_id=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                   long im=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                   Log.e("details", "Song Name ::"+im+"  "+song_name+" Song Id :"+song_id+" fullpath ::"+fullpath+" Duration ::"+Duration +"albumid "+album_id + " "+artist_id);

               audioBean.setArtist(artname);
                   audioBean.setAlbum(album_name);
                   audioBean.setAudioName(song_name);
                   audioBean.setAudioUrl(fullpath);
                   try {

                       if (fullpath!=null) {
                           mmr.setDataSource(fullpath);
                           data = mmr.getEmbeddedPicture();
                         //  Log.e("patt",""+data);
                       }
                       else
                           data=null;
                       if (data != null) {
                           audioBean.setArtistimag(data);
                       } else {

                           audioBean.setArtistimag(data);
                           audioBean.setArtist("Unknown");

                       }
                   }
                   catch (Exception e){
                       Log.e("SongsError",""+e.toString());
                   }
                   list.add(audioBean);
               } while (cursor.moveToNext());

           }
           cursor.close();

       }
       return list;
   }
}
