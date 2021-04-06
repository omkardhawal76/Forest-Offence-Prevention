package com.example.forest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//public class Adapter extends BaseAdapter {
public class Adapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Model> ModelArrayList;

    public Adapter(Context context, ArrayList<Model> ModelArrayList) {
        super(context, R.layout.list_item);
        this.context = context;
        this.ModelArrayList = ModelArrayList;
    }
    public void remove(int position) {
        ModelArrayList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return ModelArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return ModelArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            r = layoutInflater.inflate(R.layout.list_item, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.tvhead.setText("Task "+(position+1));
        viewHolder.tvname.setText(ModelArrayList.get(position).getName());

        return r;
//        ViewHolder holder,holder2;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            holder2 = new ViewHolder();
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_item, null, true);
//
//            holder.tvname =  convertView.findViewById(R.id.tv);
//            holder2.tvhead = convertView.findViewById(R.id.headingtv);
//            convertView.setTag(holder);
//            convertView.setTag(holder2);
//        }else {
//            holder = (ViewHolder)convertView.getTag();
//            holder2 = (ViewHolder)convertView.getTag();
//        }
//        holder2.tvhead.setText("Task "+(position+1));
//        holder.tvname.setText(ModelArrayList.get(position).getName());
//        return convertView;
    }
    private class ViewHolder {

        protected TextView tvname;
        protected TextView tvhead;
        ViewHolder(View v){
            tvhead = v.findViewById(R.id.headingtv);
            tvname = v.findViewById(R.id.tv);
        }
    }

}
