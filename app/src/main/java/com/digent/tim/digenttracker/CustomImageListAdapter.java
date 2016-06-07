package com.digent.tim.digenttracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tim on 15.05.16.
 */
public class CustomImageListAdapter extends BaseAdapter {

    private final Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;


    public CustomImageListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.image_list_row, parent, false);
        } else {
            view = convertView;
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView subtext = (TextView) view.findViewById(R.id.subtext);
        ImageView thumb_image = (ImageView) view.findViewById(R.id.list_image);

        HashMap<String, String> actor;
        actor = data.get(position);

        title.setText(actor.get("title"));
        subtext.setText(actor.get("subtext"));

        byte[] bImage = Base64.decode(actor.get("bitmap").getBytes(), Base64.DEFAULT);

        thumb_image.setImageBitmap(BitmapFactory.decodeByteArray(bImage, 0, bImage.length));

        return view;


    }
}
