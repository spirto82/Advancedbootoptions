package com.sparrowred.advancedbootoptions;

import android.view.View;
import android.widget.ImageView;

public class ViewHolder {

    protected ImageView image;
    ViewHolder(View v){
        image = (ImageView) v.findViewById(R.id.imageView);
    }
}
