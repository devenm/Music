package com.example.deepak.radio;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.deepak.radio.adapters.AlbumAdapter;
import com.example.deepak.radio.adapters.ArtistAdapter;
import com.example.deepak.radio.adapters.RadioAdaptor;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.files.MainAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 8/2/2016.
 */
public class Album extends Fragment {
    StringRequest stringRequest;
    RecyclerView recyclerView;
    AlbumAdapter radioAdaptor;
    ArrayList<AudioBean> arrayList;
    ProgressBar progressBar;
    ArrayList<String> albumname;
    HashMap<String,AudioBean> arrayListHashMap;
    byte[] data;
    Activity activity;
    android.media.MediaMetadataRetriever mmr;
    View v;
    HashSet<Integer> hashSet=new HashSet<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v!=null)
            return v;
        v=inflater.inflate(R.layout.recyview,container,false);
        arrayList=new ArrayList<>();
        arrayListHashMap=new HashMap<>();
        albumname=new ArrayList<>();
        mmr=new MediaMetadataRetriever();
        recyclerView= (RecyclerView) v.findViewById(R.id.content);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        radioAdaptor=new AlbumAdapter((MainAct) getActivity(),arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(radioAdaptor);
        new GetArtist().execute();
        return v;
    }
    class GetArtist extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            String[] STAR=null;
            Cursor cursor = getActivity().getContentResolver().query(allsongsuri, STAR, selection, null, null);
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
                        int im= (int) cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    //    Log.e("detailsaaa", "Song Name ::"+im+"  "+song_name+" Song Id :"+song_id+" fullpath ::"+fullpath+" Duration ::"+Duration);
if (hashSet.add(im)) {
    audioBean.setArtist(artname);
    audioBean.setAlbum(album_name);
    audioBean.setAudioName(song_name);
    audioBean.setAudioUrl(fullpath);
    try {

        if (fullpath != null) {
            mmr.setDataSource(fullpath);
            data = mmr.getEmbeddedPicture();
        } else
            data = null;
        if (data != null) {
            audioBean.setArtistimag(data);

        } else {
            audioBean.setArtistimag(null);
            audioBean.setArtist("Unknown");

        }
    } catch (Exception e) {
        Log.e("SongsError", "" + e.toString());
    }
    albumname.add(album_name);
    arrayList.add(audioBean);
}
                      // arrayListHashMap.put(artname,audioBean);
                    } while (cursor.moveToNext());

                }
                cursor.close();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            radioAdaptor=new AlbumAdapter((MainAct) getActivity(),arrayList);
            recyclerView.setAdapter(radioAdaptor);
            radioAdaptor.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }
}
