package com.lorem_ipsum.utils.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.lorem_ipsum.utils.AppUtils;

/**
 * Created by hoangminh on 9/8/15.
 */
public class VolleyManager {

    private static VolleyManager mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyManager() {
        initImageLoader();
    }

    public static synchronized VolleyManager getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyManager();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Context context = AppUtils.getAppContext();
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    private void initImageLoader() {
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        };

        mImageLoader = new ImageLoader(mRequestQueue, imageCache);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
