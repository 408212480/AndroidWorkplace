package qunincey.com.smartcity.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import qunincey.com.smartcity.R;

/*
* 基本类
* */
public class BasePager {

    public Activity mActivity;

    public TextView tvTitle;
    public ImageButton imageButton;
    public FrameLayout flcontent;
    public  View rootView;

    public BasePager(Activity activity){
        mActivity=activity;
        View rootView=initView();
    }

    public View initView(){
        View view=View.inflate(mActivity, R.layout.base_pager,null);
        tvTitle=(TextView)view.findViewById(R.id.tv_title);
        imageButton=(ImageButton)view.findViewById(R.id.btn_menu);
        flcontent=(FrameLayout)view.findViewById(R.id.fl_content);
        return view;
    }

    public void initData(){



    }
}
