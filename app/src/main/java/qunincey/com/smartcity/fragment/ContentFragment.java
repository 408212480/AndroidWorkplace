package qunincey.com.smartcity.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import qunincey.com.smartcity.R;
import qunincey.com.smartcity.base.BasePager;
import qunincey.com.smartcity.base.impl.GovAffairsPager;
import qunincey.com.smartcity.base.impl.HomePager;
import qunincey.com.smartcity.base.impl.NewsCenterPager;
import qunincey.com.smartcity.base.impl.SettingPager;
import qunincey.com.smartcity.base.impl.SmartServicePager;

public class ContentFragment extends BaseFragment {

    private ViewPager mViewPager;

    private ArrayList<BasePager> mPagers;

    @Override
    public View initView() {

        View view=View.inflate(mActivity, R.layout.fragment_content,null);
//        mViewPager=(ViewPager)view.findViewById(R.id.vp_content);
        return view;
    }

    @Override
    public void initData() {

        mPagers=new ArrayList<>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
    }




    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        /*
        * 动态加载 将BasePage填充到viewPage中
        * */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View view = pager.rootView;
            pager.initData();
            container.addView(view);
            return view;
        }
    }
}
