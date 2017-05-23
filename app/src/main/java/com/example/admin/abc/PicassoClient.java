package com.example.admin.abc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by Geetha on 4/8/2017 for Picasso ImageLoader to download our image into an imageview
 */
public class PicassoClient {
    public static void downloadImage(Context c, String imageUrl, ImageView img)
    {
        if(imageUrl.length()>0 && imageUrl!=null)
        {
            //Picasso.with(c).load(imageUrl).placeholder(R.drawable.pageloader).into(img);
            //Glide.with(c).load(imageUrl).centerCrop().crossFade().placeholder(R.drawable.pageloader).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
           Glide.with(c).load(imageUrl).dontAnimate()
                   .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).crossFade().centerCrop()
                   .placeholder(R.drawable.pageloader).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        }else {
            //Picasso.with(c).load(R.mipmap.ic_launcher).into(img);
            Glide.with(c).load(R.mipmap.ic_launcher).centerCrop().into(img);
        }
    }

}
