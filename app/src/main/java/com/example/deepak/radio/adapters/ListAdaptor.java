package com.example.deepak.radio.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.R;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.SogListDialog;
import com.example.deepak.radio.files.MainAct;
import com.example.deepak.radio.player.Player;

import java.util.ArrayList;

/**
 * Created by deepak on 8/8/2016.
 */
public class ListAdaptor extends RecyclerView.Adapter<ListAdaptor.MyViewHolder> {
    ArrayList<AudioBean> arrayList;
    MainAct activity;
    SogListDialog dialog;
    public ListAdaptor(MainAct activity, ArrayList<AudioBean> arrayList, SogListDialog dialog) {
        this.activity=activity;
        this.arrayList=arrayList;
        this.dialog=dialog;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.playerlist,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        AudioBean audioBean=arrayList.get(position);
        holder.songname.setText(audioBean.getAudioName());
        holder.artist.setText(""+audioBean.getArtist());
        holder.img.setColorFilter(Color.parseColor("#ffffff"));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setsong(position,arrayList);
                dialog.dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songname,artist;
        Typeface tf;
        ImageButton img;
        RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            songname= (TextView) itemView.findViewById(R.id.songname);
            img= (ImageButton) itemView.findViewById(R.id.imageView4);
            artist= (TextView) itemView.findViewById(R.id.artist);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.card_view);
            tf=Typeface.createFromAsset(activity.getAssets(),"GIL_____.TTF");
            songname.setTypeface(tf);
        }
    }
}
