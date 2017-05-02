package com.example.deepak.radio.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.files.MainAct;
import com.example.deepak.radio.player.Player;
import com.example.deepak.radio.R;
import com.example.deepak.radio.classes.AudioBean;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by deepak on 8/3/2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    ArrayList<AudioBean> arrayList;
    MainAct activity;
    Random rnd;
    public ArtistAdapter(MainAct activity, ArrayList<AudioBean> arrayList) {
        this.activity=activity;
        this.arrayList=arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artistadap,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        AudioBean audioBean=arrayList.get(position);
        holder.artistname.setText(audioBean.getArtist());
        if (audioBean.getArtist()==null){}
        else
        holder.artist.setText(""+audioBean.getArtist().trim().charAt(0));


       // holder.artist.setBackgroundColor(audioBean.getBoxcolor());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(activity, Player.class);
                intent.putExtra("pos",position);
                activity.startActivity(intent);*/
                activity.setsong(position,arrayList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView artistname,artist;
        Typeface tf;
        RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            artistname= (TextView) itemView.findViewById(R.id.songname);
            artist= (TextView) itemView.findViewById(R.id.box);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.card_view);

        }
    }
}
