package com.example.muchbeer.thebestway.retrieve;

import android.content.Context;
import android.widget.ImageView;

import com.example.muchbeer.thebestway.R;
import com.squareup.picasso.Picasso;

/**
 * Created by muchbeer on 27/07/2017.
 */

public class PicassoAio {

    public static void downloadImage(Context mContext,
                                     String url, ImageView img) {

        if(url != null & url.length()>0) {
            Picasso.with(mContext).
                    load(url).
                    placeholder(R.drawable.recycler).
                    transform(new RoundedTransformation()).
                    fit().
                    centerCrop().
                    into(img);
        }else {
            Picasso.with(mContext).load(R.drawable.recycler).into(img);
        }
    }
}
