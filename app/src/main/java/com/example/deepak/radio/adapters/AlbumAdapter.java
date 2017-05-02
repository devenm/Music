package com.example.deepak.radio.adapters;

import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.deepak.radio.R;
import com.example.deepak.radio.classes.AudioBean;
import com.example.deepak.radio.files.MainAct;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by deepak on 8/4/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    ArrayList<AudioBean> arrayList;
    MainAct activity;
    Random rnd;
    MenuPopupHelper optionsMenu;
    public AlbumAdapter(MainAct activity, ArrayList<AudioBean> arrayList) {
        this.activity=activity;
        this.arrayList=arrayList;
        rnd= new Random();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.albumadp,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AudioBean audioBean=arrayList.get(position);
        holder.albumname.setText(audioBean.getAlbum());
        holder.albumdesc.setText(audioBean.getAudioName());
       //holder.almunimg.setImageBitmap(audioBean.getAlbumImg());
        if (audioBean.getArtistimag()==null){}
        else
        Glide.with(activity).load(audioBean.getArtistimag()).placeholder(R.drawable.kala).into(holder.almunimg);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(activity, Player.class);
                intent.putExtra("pos",position);
                activity.startActivity(intent);*/
                activity.setsong(position,arrayList);

                MenuBuilder menuBuilder =new MenuBuilder(activity);
                MenuInflater inflater = new MenuInflater(activity);

                inflater.inflate(R.menu.popup, menuBuilder);
                optionsMenu = new MenuPopupHelper(activity, menuBuilder, holder.albumname);
                optionsMenu.setForceShowIcon(true);
// Set Item Click Listener
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.opt1: // Handle option1 Click
                                return true;
                            case R.id.opt2: // Handle option2 Click
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {}
                });
                optionsMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView albumname,albumdesc;
        RelativeLayout relativeLayout;
        ImageView almunimg;
        ImageButton moreoption;
        public MyViewHolder(View itemView) {
            super(itemView);
            albumname= (TextView) itemView.findViewById(R.id.albumname);
            albumdesc= (TextView) itemView.findViewById(R.id.albumdescription);
            almunimg= (ImageView) itemView.findViewById(R.id.box);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.card_view);
            moreoption= (ImageButton) itemView.findViewById(R.id.moreoption);

        }
    }

}

