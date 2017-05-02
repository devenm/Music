package com.example.deepak.radio.classes;

import android.graphics.Bitmap;

/**
 * Created by deepak on 8/8/2016.
 */
public class RedioBean {

    private int id;
    private String name;
    String artistid;
    Bitmap albumImg;
    int boxcolor;
    String imgUrl;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        if (imgUrl.equals(""))
            this.imgUrl="http://i.radionomy.com/documents/radio/000e2e66-2049-413f-9274-16d909fb42ba.s165.jpg";
        else
            this.imgUrl = imgUrl;
    }

    public int getBoxcolor() {
        return boxcolor;
    }

    public void setBoxcolor(int boxcolor) {
        this.boxcolor = boxcolor;
    }

    public Bitmap getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(Bitmap albumImg) {
        this.albumImg = albumImg;
    }

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    String albumid;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public byte[] getArtistimag() {
        return artistimag;
    }

    public void setArtistimag(byte[] artistimag) {
        this.artistimag = artistimag;
    }

    private String artist,album;
    String aurl,aname,aid;
    byte[] artistimag;

    public String getAudioId() {
        return aid;
    }
    public void setAudioId(String idd) {
        this.aid = idd;
    }


    public String getAudioUrl() {

        return aurl;
    }
    public void setAudioUrl(String url)

    {
        this.aurl = url;
    }
    public String getAudioName()
    {
        return aname;
    }
    public void setAudioName(String name)

    {
        this.aname = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }


}


