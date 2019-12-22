package com.example.administrator.cse476currencyconverter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 6.11.2017.
 */

public class MyAdapter extends ArrayAdapter<String> {
    String[] names;
    int[] flags;
    Context mContext;
    public MyAdapter(Context context, String[] countryNames, int[] countryFlags ){
        super(context, R.layout.listview_item);
        this.names= countryNames;
        this.flags= countryFlags;
        this.mContext= context;
    }

    @Override
    public int getCount(){
        return names.length;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Log.d("show", "getView:start ");
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item2, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(flags[position]);
        mViewHolder.mName.setText(names[position]);

        return convertView;
    }

    static class ViewHolder {
        ImageView mFlag;
        TextView mName;

    }

}
