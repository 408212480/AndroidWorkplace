package qunincey.com.smartcity.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import qunincey.com.smartcity.R;
import qunincey.com.smartcity.domain.NewsMenu;

public class TitleAdpater extends BaseAdapter {

    private NewsMenu mData;
    private Context mContext;
    private TextView textView;

    public TitleAdpater(NewsMenu mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.fruit_item,parent,false);
        textView = convertView.findViewById(R.id.tv_title);
        textView.setText(mData.getData().get(position).title);
        return convertView;
    }
}
