package com.example.deepak.radio.classes;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.deepak.radio.CircularProgressBar;
import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.R;
import com.example.deepak.radio.adapters.ListAdaptor;
import com.example.deepak.radio.adapters.SongAdapter;
import com.example.deepak.radio.files.MainAct;
import com.example.deepak.radio.player.Player;

import java.util.ArrayList;

/**
 * Created by deepak on 8/8/2016.
 */
public class SogListDialog {
  public   Dialog dialog;
    RecyclerView recyclerView;
ListAdaptor listAdaptor;
    public static ArrayList<AudioBean> arrayList;
    ArrayList<AudioBean> list;
    ProgressBar progressBar;
    public SogListDialog(MainAct context, ArrayList<AudioBean> listt){
        dialog=new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.recyview);
        dialog.setTitle("Songs List >");
        arrayList=listt;
        list=new ArrayList<>();
        recyclerView= (RecyclerView) dialog.findViewById(R.id.content);
        progressBar= (ProgressBar) dialog.findViewById(R.id.progressBar);
        listAdaptor=new ListAdaptor(context,arrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(listAdaptor);
        dialog.show();
        progressBar.setVisibility(View.GONE);
    }
}
