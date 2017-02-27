package com.sparrowred.advancedbootoptions;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NameWithImage> list;

    public Integer[] mThumbIds = {
            R.drawable.soft_reboot, R.drawable.power_off, R.drawable.reboot,
            R.drawable.reboot_recovery, R.drawable.reboot_bootloader, R.drawable.factory_reset
    };


    public String[] tempInfo;

    public ImageAdapter(Context c){
        this.mContext = c;
        list = new ArrayList<NameWithImage>();
        Resources res = mContext.getResources();
        tempInfo = res.getStringArray(R.array.information);
        for(int i = 0; i <tempInfo.length; i++){
            NameWithImage tempDateWithImage = new NameWithImage(mThumbIds[i],tempInfo[i]);
            list.add(tempDateWithImage);
        }
    }

    public void changeImage(int position, int image){
        mThumbIds[position] = image;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }
        NameWithImage temp = list.get(position);
        holder.image.setImageResource(temp.getImageID());
        holder.image.setTag(temp);
        return row;
    }
}
