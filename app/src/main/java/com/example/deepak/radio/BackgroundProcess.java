package com.example.deepak.radio;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by Rajesh on 21-07-2015.
 */
public class BackgroundProcess extends Application {
    public static final String TAG = BackgroundProcess.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static BackgroundProcess mInstance;
    public static SharedPreferences shared;
    @Override
    public void onCreate() {
        super.onCreate();
       // Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "XBr5SHufNh4n7jvOHVdKKacfTI11wWegLcdZxRiM", "GzTV35lf2z2o2TDHOn6xOX00K4D6Z1DWuYPZzkQ5");
        shared=getSharedPreferences("Mydata", Context.MODE_APPEND);
        mInstance = this;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .writeDebugLogs()
                        // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static synchronized BackgroundProcess getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
