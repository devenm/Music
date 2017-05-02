package com.example.deepak.radio.classes;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by deepak on 8/4/2016.
 */
public class ArtistInfo {
    String artistname;
    Bitmap artistImage;

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public Bitmap getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(Bitmap artistImage) {
        this.artistImage = artistImage;
    }

    public HashMap<String, String> getHashMapSong() {
        return hashMapSong;
    }

    public void setHashMapSong(HashMap<String, String> hashMapSong) {
        this.hashMapSong = hashMapSong;
    }

    HashMap<String,String> hashMapSong;
}
