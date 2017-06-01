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


public class Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;
    ImageLoader imageLoader = AppController.getPermission().getImageLoader();

    public Adapter(Activity activity, List<DataSet> dataitem) {
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
            convertView = inflater.inflate(R.layout.listofallproperties, null);

        if (imageLoader == null)
            imageLoader = AppController.getPermission().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView propname = (TextView) convertView.findViewById(R.id.propertyname);
        TextView des = (TextView) convertView.findViewById(R.id.prop_des);
        TextView amount = (TextView) convertView.findViewById(R.id.prop_amount);
        TextView cell = (TextView) convertView.findViewById(R.id.cell);
        TextView city = (TextView) convertView.findViewById(R.id.city);
        TextView area = (TextView) convertView.findViewById(R.id.area);
        TextView purpose = (TextView) convertView.findViewById(R.id.prop_purpose);
        TextView type = (TextView) convertView.findViewById(R.id.prop_type);
        TextView subType = (TextView) convertView.findViewById(R.id.prop_subtype);
        TextView feature = (TextView) convertView.findViewById(R.id.prop_feature);
        TextView clname = (TextView) convertView.findViewById(R.id.clname);
        TextView agname = (TextView) convertView.findViewById(R.id.agent_name);
        TextView sq = (TextView) convertView.findViewById(R.id.sq_yrds);
        TextView estateName = (TextView) convertView.findViewById(R.id.estate_name);

        DataSet m = DataList.get(position);
        thumbNail.setImageUrl(m.getImage(), imageLoader);
        propname.setText(m.getPropname());
        des.setText("Description: " + String.valueOf(m.getDes()));
        amount.setText(String.valueOf(m.getAmount()));
        cell.setText(String.valueOf(m.getCell()));
        city.setText("Located in: " + String.valueOf(m.getCity()));
        area.setText(String.valueOf(m.getArea()));
        purpose.setText("Purpose: " + String.valueOf(m.getPurpose()));
        type.setText("Type: " + String.valueOf(m.getType()));
        subType.setText("Sub Type: " + String.valueOf(m.getSubtype()));
        feature.setText("Feature: " + String.valueOf(m.getFeature()));
        sq.setText("Sq.Yrds of: " + String.valueOf(m.getSquareyrds()));
        clname.setText("Owner: " + String.valueOf(m.getClient()));
        agname.setText("Estate Agent: " + String.valueOf(m.getAgent()));
        estateName.setText("RealEstate: " + String.valueOf(m.getEstate()));


        return convertView;
    }

}