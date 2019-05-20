package qunincey.com.smartcity.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import qunincey.com.smartcity.base.BaseMenuDetailPager;

import static android.content.ContentValues.TAG;

public class PhotosMenuDetailPager extends BaseMenuDetailPager {


    public PhotosMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        Log.d(TAG, "initData: home");
        //要给帧布局填充view
        TextView textView=new TextView(mActivity);
        textView.setText("照片");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
