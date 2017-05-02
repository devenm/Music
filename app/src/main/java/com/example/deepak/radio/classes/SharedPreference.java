package com.example.deepak.radio.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<AudioBean> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, AudioBean product) {
        List<AudioBean> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<AudioBean>();

        favorites.add(0,product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, AudioBean product) {
        ArrayList<AudioBean> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<AudioBean> getFavorites(Context context) {
        SharedPreferences settings;
        List<AudioBean> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            AudioBean[] favoriteItems = gson.fromJson(jsonFavorites,
                    AudioBean[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<AudioBean>(favorites);
        } else
            return null;

        return (ArrayList<AudioBean>) favorites;
    }
}