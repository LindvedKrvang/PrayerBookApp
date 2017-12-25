package com.lindved.prayerbook.prayerbook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lindved.prayerbook.prayerbook.Entities.Prayer;
import com.lindved.prayerbook.prayerbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Lindved on 05-11-2017.
 */

public class PrayerAdapter extends BaseAdapter {

    private Context mContext;
    private JSONArray mJSONPrayers;
    private List<Prayer> mPrayers;

    public PrayerAdapter(Context context, JSONArray prayers){
        mContext = context;
        mJSONPrayers = prayers;
    }

    private void convertJSONPrayersToPrayers() throws JSONException {
        for (int i = 0; i < mJSONPrayers.length(); i++){
            Prayer prayer = new Prayer();
            JSONObject jsonPrayer = mJSONPrayers.getJSONObject(i);
            int id = Integer.parseInt(jsonPrayer.getString("id"));
            prayer.setId(id);
            prayer.setSubject(jsonPrayer.getString("subject"));
            mPrayers.add(prayer);
        }
    }

    @Override
    public int getCount() {
        return mJSONPrayers.length();
    }

    @Override
    public Prayer getItem(int position) {
        try{
            JSONObject jsonPrayer =  mJSONPrayers.getJSONObject(position);
            int id = Integer.parseInt(jsonPrayer.getString(mContext.getString(R.string.json_id)));
            Prayer prayer = new Prayer();
            prayer.setId(id);
            prayer.setSubject(jsonPrayer.getString(mContext.getString(R.string.json_subject)));
            return prayer;
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
            holder.subjectView = view.findViewById(R.id.txtSubject);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        try{
            createPrayerView(holder, mJSONPrayers.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void createPrayerView(ViewHolder holder, JSONObject prayer) throws JSONException {
        holder.subjectView.setText(prayer.getString(mContext.getString(R.string.json_subject)));
    }

    private static class ViewHolder{;
        TextView subjectView;
    }
}
