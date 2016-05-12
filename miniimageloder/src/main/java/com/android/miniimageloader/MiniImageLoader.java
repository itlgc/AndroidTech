package com.android.miniimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.android.miniimageloader.config.BitmapConfig;
import com.android.miniimageloader.utils.CloseUtil;
import com.android.miniimageloader.utils.MLog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yuchengluo on 2016/4/29.
 */
public class MiniImageLoader extends ImageLoader{
    private volatile static MiniImageLoader miniImageLoader = null;
    private ImageCache mImageCache = null;
    public static MiniImageLoader getInstance(){
        if(null == miniImageLoader){
            synchronized (MiniImageLoader.class){
                miniImageLoader = new MiniImageLoader();
            }
        }
        return miniImageLoader;
    }
    public MiniImageLoader(){
        mImageCache = new ImageCache();
    }

    public Bitmap downLoadBitmap(String urlString,BitmapConfig bmConfig) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            final BitmapFactory.Options options = bmConfig.getBitmapOptions(in);
            in.close();
            urlConnection.disconnect();
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            Bitmap bitmap = decodeSampledBitmapFromStream(in,options );
            return bitmap;

        } catch (final IOException e) {
            Log.e("text", "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseUtil.closeQuietly(in);
        }
        return null;
    }

    public Bitmap decodeSampledBitmapFromStream(
            InputStream is, BitmapFactory.Options options) {
        MLog.d(TAG,"mSampleSize:" + options.inSampleSize);
        return BitmapFactory.decodeStream(is, null, options);
    }
}
