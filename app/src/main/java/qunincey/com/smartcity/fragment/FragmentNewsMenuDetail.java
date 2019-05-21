package qunincey.com.smartcity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import qunincey.com.smartcity.R;
import qunincey.com.smartcity.base.impl.menu.TabDetailPager;
import qunincey.com.smartcity.domain.NewsMenu;

public class FragmentNewsMenuDetail extends Fragment {


    private View view;
    private ViewPager viewPager;

    private ArrayList<NewsMenu.NewsTabData> mTabData;
    private ArrayList<TabDetailPager> mPagers;// 页签页面集合

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_detail,container,false);


//        initData();
        return view;
    }

//    private void initData() {
//        mPagers = new ArrayList<TabDetailPager>();
//        for (int i = 0; i < mTabData.size(); i++) {
//            TabDetailPager pager = new TabDetailPager(mActivity,
//                    mTabData.get(i));
//            mPagers.add(pager);
//        }
//
//        viewPager.setAdapter(new NewMenuDetailAdapter());
//        mIndicator.setViewPager(mViewPager);// 将viewpager和指示器绑定在一起.注意:必须在viewpager设置完数据之后再绑定
//
//        // 设置页面滑动监听
//        // mViewPager.setOnPageChangeListener(this);
//        mIndicator.setOnPageChangeListener(this);// 此处必须给指示器设置页面监听,不能设置给viewpager
//    }

    class NewMenuDetailAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }



}
