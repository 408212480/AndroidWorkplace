package qunincey.com.smartcity.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import qunincey.com.smartcity.base.BasePager;

public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充view
        TextView textView=new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        flcontent.addView(textView);

    }
}
