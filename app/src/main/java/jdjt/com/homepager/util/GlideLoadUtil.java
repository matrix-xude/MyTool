package jdjt.com.homepager.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by xxd on 2018/9/30.
 */

public class GlideLoadUtil {

    public static void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, 0);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int defaultImage) {
        if (url == null || url.length() == 0)
            return;
        RequestOptions options = new RequestOptions()
//                    .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultImage).error(defaultImage);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
