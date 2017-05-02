package com.example.deepak.radio;

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
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.files.MainAct;

import java.util.ArrayList;

/**
 * Created by deepak on 8/2/2016.
 */
public class Favourites extends Fragment{
    StringRequest stringRequest;
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    ArrayList<AudioBean> arrayList;
    ProgressBar progressBar;
    SharedPreference sharedPreference;
    boolean check=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.recyview,container,false);
        arrayList=new ArrayList<>();
        sharedPreference=new SharedPreference();
        recyclerView= (RecyclerView) v.findViewById(R.id.content);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
       songAdapter=new SongAdapter((MainAct) getActivity(),arrayList, check);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(songAdapter);
        addItems();
        return v;
    }
    public void addItems() {
       arrayList=sharedPreference.getFavorites(getActivity());
        songAdapter=new SongAdapter((MainAct) getActivity(),arrayList, check);
        recyclerView.setAdapter(songAdapter);
        songAdapter.notifyDataSetChanged();

    }
}