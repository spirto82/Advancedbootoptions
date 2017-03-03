package com.sparrowred.advancedbootoptions;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/*public class ViewHolder implements View.OnTouchListener{

    protected ImageView image;

    ViewHolder(View v){
        image = (ImageView) v.findViewById(R.id.imageView);
        v.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                v.setAlpha(0.2f);
                break;
            case MotionEvent.ACTION_UP:
                v.setAlpha(1.0f);
                break;
            case MotionEvent.ACTION_MOVE:
                v.setAlpha(0.2f);
                break;
            case MotionEvent.ACTION_SCROLL:
                v.setAlpha(0.2f);
                break;
            default:
                break;
        }
        return true;
    }
}*/

public class ViewHolder{

    protected ImageView image;

    ViewHolder(View v){
        image = (ImageView) v.findViewById(R.id.imageView);
    }
}
