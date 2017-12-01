package com.example.lindved.prayerbook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lindved.prayerbook.Entities.Response;
import com.example.lindved.prayerbook.R;

import java.util.List;

/**
 * Created by Lindved on 25-11-2017.
 */

public class ResponseAdapter extends BaseAdapter {

    private Context mContext;
    private List<Response> mResponses;

    public ResponseAdapter(Context context, List<Response> responses){
        mContext = context;
        mResponses = responses;
    }

    @Override
    public int getCount() {
        return mResponses.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.response_list_item, null);
            holder = new ViewHolder();
            holder.answerView = view.findViewById(R.id.txtAnswer);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        createResponseView(holder, mResponses.get(position));
        return view;
    }

    private void createResponseView(ViewHolder holder, Response response){
        holder.answerView.setText(response.getAnswer() + "");
    }

    private static class ViewHolder{
        TextView answerView;
    }
}
