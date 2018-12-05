package com.hardkernel.voodik.odroidutility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


// View Holder used when displaying views
public class AppViewHolder {

    public View rootView;
    public TextView appName;
    public ImageView appIcon;
    public TextView summary;
    public TextView disabled;

    static public AppViewHolder createOrRecycle(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.preference_app, null);
            inflater.inflate(R.layout.widget_text_views,
                    (ViewGroup) convertView.findViewById(android.R.id.widget_frame));

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            AppViewHolder holder = new AppViewHolder();
            holder.rootView = convertView;
            holder.appName = convertView.findViewById(android.R.id.title);
            holder.appIcon = convertView.findViewById(android.R.id.icon);
            holder.summary = convertView.findViewById(R.id.widget_text1);
            holder.disabled = convertView.findViewById(R.id.widget_text2);
            convertView.setTag(holder);
            return holder;
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            return (AppViewHolder) convertView.getTag();
        }
    }
}