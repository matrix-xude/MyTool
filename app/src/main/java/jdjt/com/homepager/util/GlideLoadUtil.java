package jdjt.com.homepager.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by xxd on 2018/9/30.
 */

public class GlideLoadUtil {

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (url == null || url.length() == 0)
            Glide.with(context).load(url).into(imageView);
    }
}
