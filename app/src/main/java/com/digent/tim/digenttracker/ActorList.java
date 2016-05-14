package com.digent.tim.digenttracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by tim on 15.05.16.
 */
public class ActorList extends BaseAdapter {

    private final Activity context;

    public ActorList(Activity context, String[] actorNames, String[] roleNames, Bitmap[] actorImages) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
