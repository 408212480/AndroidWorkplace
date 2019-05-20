package qunincey.com.smartcity.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity =activity;
        mRootView=initView();
    }
    //初始化布局
    public abstract View initView();

    //初始化布局
    public void initData(){

    }
}
