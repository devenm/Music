package com.example.deepak.radio.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.deepak.radio.MainActivity;
import com.example.deepak.radio.R;
import com.example.deepak.radio.classes.RedioBean;
import com.example.deepak.radio.files.MainAct;
import com.example.deepak.radio.radioplayer.RadioPlayer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by deepak on 7/26/2016.
 */
public class RadioAdaptor extends RecyclerView.Adapter<RadioAdaptor.MyViewHolder> {
    MainAct activity;
  public static   ArrayList<Object> arrayList;
    final int HEADER = 0, CHILD = 1,ADV=2;
   public static ArrayList<RedioBean> catlis;

    public RadioAdaptor(MainAct mainActivity, ArrayList<Object> arrayList, ArrayList<RedioBean> categorylist) {
        activity = mainActivity;
        this.arrayList = arrayList;
catlis=categorylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CHILD)
            return new Child(LayoutInflater.from(parent.getContext()).inflate(R.layout.tube, parent, false));
        else if (viewType == HEADER)
            return new Header(LayoutInflater.from(parent.getContext()).inflate(R.layout.titleadap, parent, false));
        else if (viewType == ADV)
            return new Adver(LayoutInflater.from(parent.getContext()).inflate(R.layout.nativadd, parent, false));
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%5==0&&position!=0){
            return ADV;
        }
        else {
            if (arrayList.get(position) instanceof String)
                return HEADER;
            else
                return CHILD;
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER)
            setTitle((Header) holder, position);
        else if (holder.getItemViewType()==CHILD)
        setChild((Child)holder,position);
        else
            setAdv((Adver)holder,position);

    }

    private void setAdv(Adver holder, int position) {
loadad(holder);
    }
    private void loadad(final Adver holder) {
        Log.e("called", "load");
        holder.adView.loadAd(new AdRequest.Builder().build());
        holder.adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("failed", "load" + i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                holder.adView.setVisibility(View.VISIBLE);
                Log.e("ad", "load");
            }

        });

    }
    private void setChild(Child holder, final int position) {
        if (arrayList.size() > 0) {
            final RedioBean itemsBean = (RedioBean) arrayList.get(position);
            holder.title.setText(itemsBean.getAudioName());
            holder.description.setText(itemsBean.getArtist());
            Picasso.with(activity).load(itemsBean.getImgUrl()).into(holder.logo);
            holder.progressBar.setVisibility(View.GONE);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent=new Intent(activity, RadioPlayer.class);
                    intent.putExtra("pos",itemsBean.getPosition());
                    activity.startActivity(intent);*/
                    activity.setsongR(itemsBean.getPosition(),catlis);

                }
            });

        }
    }

    private void setTitle(Header holder, int position) {
        holder.title.setText(arrayList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Header extends MyViewHolder {
        TextView title;

        public Header(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
    class Child extends MyViewHolder{
        TextView title, description;
        ImageView logo;
        ProgressBar progressBar;
        Typeface tf;
        LinearLayout linearLayout;
        public Child(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbar);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.desc);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.vplay1);

        }
    }
    class Adver extends MyViewHolder{
        NativeExpressAdView adView;
        public Adver(View itemView) {
            super(itemView);
            adView = (NativeExpressAdView) itemView.findViewById(R.id.adView);

        }
    }
}
