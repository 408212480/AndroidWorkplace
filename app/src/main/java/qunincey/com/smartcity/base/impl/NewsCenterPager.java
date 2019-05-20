package qunincey.com.smartcity.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import qunincey.com.smartcity.base.BasePager;

import static android.content.ContentValues.TAG;

public class NewsCenterPager extends BasePager {

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: news");
        //要给帧布局填充view
        TextView textView=new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flcontent.addView(textView);

    }
}
