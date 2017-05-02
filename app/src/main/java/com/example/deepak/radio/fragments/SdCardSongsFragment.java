package com.example.deepak.radio.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepak.radio.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 8/4/2016.
 */
public class SdCardSongsFragment extends Fragment {
    /*public File file;
    private List<String> myList;
    private List<String> mycountList;
    private ListView listView;
    private TextView pathTextView;
    private String mediapath = new String(Environment.getExternalStorageDirectory().getAbsolutePath());
    String selection = MediaStore.Audio.Media.DATA + " like ?";
    String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME};
    Cursor cursor2;
    //your database elect statement
    String selection2 = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    //your projection statement
    String[] projection2 = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
    };

    private final static String[] acceptedExtensions = {"mp3", "mp2", "wav", "flac", "ogg", "au", "snd", "mid", "midi", "kar"
            , "mga", "aif", "aiff", "aifc", "m3u", "oga", "spx"};

    *//**
     * Called when the activity is first created.
     *//*
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sdlist, container, false);

        listView = (ListView) rootView.findViewById(R.id.pathlist);
        pathTextView = (TextView) rootView.findViewById(R.id.path);

        myList = new ArrayList<String>();
        mycountList = new ArrayList<String>();
        String root_sd = Environment.getExternalStorageDirectory().toString();
        Log.e("Root", root_sd);

        String state = Environment.getExternalStorageState();
        File list[] = null;
    *//* if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
        list=getAllFilesOfDir(Environment.getExternalStorageDirectory());
    }*//*

        pathTextView.setText(root_sd);

        file = new File(root_sd);
        list = file.listFiles(new AudioFilter());
        Log.e("Size of list ", "" + list.length);
        //LoadDirectory(root_sd);

        for (int i = 0; i < list.length; i++) {

            String name = list[i].getName();
            int count = getAudioFileCount(list[i].getAbsolutePath());
            Log.e("Count : " + count, list[i].getAbsolutePath());
            if (count != 0) {
                myList.add(name);
                mycountList.add("" + count);
            }


        }


      //  listView.setAdapter(new SDcardSongListAdapter(getActivity(), myList, mycountList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                File temp_file = new File(file, myList.get(position));

                if (!temp_file.isFile()) {


                    file = new File(file, myList.get(position));
                    File list[] = file.listFiles(new AudioFilter());

                    myList.clear();
                    mycountList.clear();
                    for (int i = 0; i < list.length; i++) {
                        String name = list[i].getName();

                        int count = getAudioFileCount(list[i].getAbsolutePath());
                        Log.e("Count : " + count, list[i].getAbsolutePath());
                        if (count != 0) {
                            myList.add(name);
                            mycountList.add("" + count);
                        }

                    }

                    pathTextView.setText(file.toString());
                    //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
                   // listView.setAdapter(new SDcardSongListAdapter(getActivity(), myList, mycountList));

                } else {
                    Log.e("Not dirctory ", file.getAbsolutePath());
                    Cursor cur = getAudioFileCursor(file.getAbsolutePath());
                    Log.e("Cur count ", "" + cur.getCount());
                    //MusicUtils.playAll(getActivity(), cur, position);
                }


            }
        });
        return rootView;


    }

    private int getAudioFileCount(String dirPath) {

        Log.e("Path :  ", dirPath);
        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor = getActivity().managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection2,
                selection,
                selectionArgs,
                null);


        cursor2 = cursor;
        if (cursor != null)
            Log.e("Cur : ", "" + cursor.getCount());
        return cursor.getCount();
    }

    private Cursor getAudioFileCursor(String dirPath) {


        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor = getActivity().managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection2,
                selection,
                selectionArgs,
                null);
        return cursor;
    }


    public void onBack() {
        String parent = "";
        if (file != null)
            parent = file.getParent().toString();

        file = new File(parent);
        File list[] = file.listFiles(new AudioFilter());

        myList.clear();
        mycountList.clear();
        for (int i = 0; i < list.length; i++) {
            String name = list[i].getName();
            int count = getAudioFileCount(list[i].getAbsolutePath());
            Log.e("Count : " + count, list[i].getAbsolutePath());
            if (count != 0) {
                myList.add(name);
                mycountList.add("" + count);

            }
        *//*int count=getAllFilesOfDir(list[i]);
            Log.e("Songs count ",""+count);
            if(count!=0)*//*

        }
        pathTextView.setText(parent);
        //  Toast.makeText(getApplicationContext(), parent,          Toast.LENGTH_LONG).show();
     //   listView.setAdapter(new SDcardSongListAdapter(getActivity(), myList, mycountList));

    }


    // class to limit the choices shown when browsing to SD card to media files
    public class AudioFilter implements FileFilter {

        // only want to see the following audio file types
        private String[] extension = {".aac", ".mp3", ".wav", ".ogg", ".midi", ".3gp", ".mp4", ".m4a", ".amr", ".flac"};

        @Override
        public boolean accept(File pathname) {

            // if we are looking at a directory/file that's not hidden we want to see it so return TRUE
            if ((pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden()) {
                return true;
            }

            // loops through and determines the extension of all files in the directory
            // returns TRUE to only show the audio files defined in the String[] extension array
            for (String ext : extension) {
                if (pathname.getName().toLowerCase().endsWith(ext)) {
                    return true;
                }
            }

            return false;
        }
    }*/
}