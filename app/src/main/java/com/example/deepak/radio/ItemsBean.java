package com.example.deepak.radio;

/**
 * Created by deepak on 7/26/2016.
 */
public class ItemsBean {
    String title;
    String logo;
    String ct;
    String category;
    String titletype;

    public String getTitletype() {
        return titletype;
    }

    public void setTitletype(String titletype) {
        this.titletype = titletype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        if (logo==null)
            this.logo="http://i.radionomy.com/documents/radio/000e2e66-2049-413f-9274-16d909fb42ba.s165.jpg";
        else
        this.logo = logo;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getGanre() {
        return ganre;
    }

    public void setGanre(String ganre) {
        this.ganre = ganre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String lc;
    String ganre;
    String id;
}
