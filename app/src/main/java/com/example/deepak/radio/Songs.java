package com.example.deepak.radio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.StringRequest;
import com.example.deepak.radio.adapters.SongAdapter;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.Sdcard;
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.files.MainAct;

import java.util.ArrayList;

/**
 * Created by deepak on 8/2/2016.
 */
public class Songs extends Fragment {
    StringRequest stringRequest;
    RecyclerView recyclerView;
    SongAdapter songAdapter;
   public static ArrayList<AudioBean> arrayList;
    ArrayList<AudioBean> list;
    ProgressBar progressBar;
    View v;
    boolean check;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v!=null)
            return v;
         v=inflater.inflate(R.layout.recyview,container,false);
        arrayList=new ArrayList<>();
        list=new ArrayList<>();
        recyclerView= (RecyclerView) v.findViewById(R.id.content);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        songAdapter=new SongAdapter((MainAct) getActivity(),arrayList,check);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(songAdapter);
        addItems();
        return v;
    }
    public void addItems() {

    GetSongss getSongss=new GetSongss();
        getSongss.execute();

    }
    class  GetSongss extends AsyncTask<String,String,ArrayList<AudioBean>> {
        @Override
        protected ArrayList<AudioBean> doInBackground(String... params) {
            //list=new Sdcard(getContext()).getPlayList();
           list=new Sdcard(getContext()).getAllSongs();
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<AudioBean> audioBeen) {
           // super.onPostExecute(audioBeen);
            songAdapter=new SongAdapter((MainAct) getActivity(),audioBeen, check);
            recyclerView.setAdapter(songAdapter);
            songAdapter.notifyDataSetChanged();
           arrayList=audioBeen;
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            songAdapter.notifyDataSetChanged();

        }
    }
}
