package com.example.forest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlertListAdapter extends ArrayAdapter {
    Activity context;
    String title[];
    int icon[];
    public AlertListAdapter(Activity context,String [] title,int [] icon) {
        super(context, R.layout.alert_list,title);
        this.context = context;
        this.title = title;
        this.icon = icon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.alert_list,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.imageView.setImageResource(icon[position]);
        viewHolder.btn.setText(title[position]);

        return r;
    }
    class ViewHolder{
        Button btn;
        ImageView imageView;
        ViewHolder(View v){
            btn = v.findViewById(R.id.label_btn);
            imageView = v.findViewById(R.id.alert_img);
        }
    }
}
