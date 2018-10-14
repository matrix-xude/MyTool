package jdjt.com.homepager.view.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import jdjt.com.homepager.view.banner.transformer.AccordionTransformer;
import jdjt.com.homepager.view.banner.transformer.BackgroundToForegroundTransformer;
import jdjt.com.homepager.view.banner.transformer.CubeInTransformer;
import jdjt.com.homepager.view.banner.transformer.CubeOutTransformer;
import jdjt.com.homepager.view.banner.transformer.DefaultTransformer;
import jdjt.com.homepager.view.banner.transformer.DepthPageTransformer;
import jdjt.com.homepager.view.banner.transformer.FlipHorizontalTransformer;
import jdjt.com.homepager.view.banner.transformer.FlipVerticalTransformer;
import jdjt.com.homepager.view.banner.transformer.ForegroundToBackgroundTransformer;
import jdjt.com.homepager.view.banner.transformer.RotateDownTransformer;
import jdjt.com.homepager.view.banner.transformer.RotateUpTransformer;
import jdjt.com.homepager.view.banner.transformer.ScaleInOutTransformer;
import jdjt.com.homepager.view.banner.transformer.StackTransformer;
import jdjt.com.homepager.view.banner.transformer.TabletTransformer;
import jdjt.com.homepager.view.banner.transformer.ZoomInTransformer;
import jdjt.com.homepager.view.banner.transformer.ZoomOutSlideTransformer;
import jdjt.com.homepager.view.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
