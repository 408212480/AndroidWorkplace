package qunincey.com.smartcity.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import qunincey.com.smartcity.base.BaseMenuDetailPager;

public class TabDetailPager extends BaseMenuDetailPager {

    public TabDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView=new TextView(mActivity);
        textView.setText("页签");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        return  textView;
    }

    @Override
    public void initData() {

    }
}
