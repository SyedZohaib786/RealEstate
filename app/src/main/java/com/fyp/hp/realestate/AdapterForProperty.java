package com.fyp.hp.realestate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.AppController;

/**
 * Created by Shah on 11/04/2017.
 */

public class AdapterForProperty extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;
    ImageLoader imageLoader = AppController.getPermission().getImageLoader();

    public AdapterForProperty(Activity activity, List<DataSet> dataitem) {
        this.activity = activity;
        this.DataList = dataitem;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int location) {
        return DataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listofproperty, null);

        if (imageLoader == null)
            imageLoader = AppController.getPermission().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView propname = (TextView) convertView.findViewById(R.id.propertyname);
        TextView amount = (TextView) convertView.findViewById(R.id.prop_amount);
        TextView des = (TextView) convertView.findViewById(R.id.prop_des);

        DataSet m = DataList.get(position);
        thumbNail.setImageUrl(m.getImage(), imageLoader);
        propname.setText(m.getPropname());
        amount.setText("Price: " + String.valueOf(m.getAmount()));
        des.setText("Description: " + String.valueOf(m.getDes()));


        return convertView;
    }

}
