package com.example.deepak.radio.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deepak.radio.R;
import com.example.deepak.radio.Songs;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.classes.SharedPreference;
import com.example.deepak.radio.files.MainAct;
import java.util.ArrayList;

/**
 * Created by deepak on 8/3/2016.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    ArrayList<AudioBean> arrayList;
MainAct activity;
    boolean check;
    SharedPreference sharedPreference;
    public SongAdapter(MainAct activity, ArrayList<AudioBean> arrayList, boolean check) {
        this.activity=activity;
        this.arrayList=arrayList;
        this.check=check;
        sharedPreference=new SharedPreference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.playerlist,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AudioBean audioBean=arrayList.get(position);
holder.songname.setText(audioBean.getAudioName());
        holder.artist.setText(""+audioBean.getArtist());
       holder.img.setColorFilter(Color.parseColor("#ffffff"));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Songs.arrayList=arrayList;
                /*Intent intent=new Intent(activity, Player.class);
                intent.putExtra("pos",position);
                activity.startActivity(intent);*/
                activity.setsong(position,arrayList);
            }
        });
        if (check==true){
            holder.favremove.setVisibility(View.VISIBLE);
        }
        holder.favremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.removeFavorite(activity, arrayList.get(position));

                Toast.makeText(activity,
                        activity.getResources().getString(R.string.remove_fav),
                        Toast.LENGTH_SHORT).show();
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songname,artist;
        Typeface tf;
        ImageButton img;
        RelativeLayout relativeLayout;
        ImageButton favremove;
        public MyViewHolder(View itemView) {
            super(itemView);
            songname= (TextView) itemView.findViewById(R.id.songname);
            img= (ImageButton) itemView.findViewById(R.id.imageView4);
            artist= (TextView) itemView.findViewById(R.id.artist);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.card_view);
            favremove= (ImageButton) itemView.findViewById(R.id.favremove);
        }
    }
}
