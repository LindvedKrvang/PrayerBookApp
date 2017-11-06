package com.example.lindved.prayerbook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lindved.prayerbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lindved on 05-11-2017.
 */

public class PrayerAdapter extends BaseAdapter {

    private Context mContext;
    private JSONArray mPrayers;

    public PrayerAdapter(Context context, JSONArray prayers){
        mContext = context;
        mPrayers = prayers;
    }

    @Override
    public int getCount() {
        return mPrayers.length();
    }

    @Override
    public Object getItem(int position) {
        try{
            return mPrayers.getJSONObject(position);
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.prayer_list_item, null);
            holder = new ViewHolder();
            holder.idView = (TextView) view.findViewById(R.id.txtId);
            holder.subjectView = (TextView) view.findViewById(R.id.txtSubject);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        try{
            createPrayerView(holder, mPrayers.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void createPrayerView(ViewHolder holder, JSONObject prayer) throws JSONException {
        holder.idView.setText(prayer.getString("id"));
        holder.subjectView.setText(prayer.getString("subject"));
    }

    private static class ViewHolder{
        TextView idView;
        TextView subjectView;
    }
}
