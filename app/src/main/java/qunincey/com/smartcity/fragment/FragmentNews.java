package qunincey.com.smartcity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qunincey.com.smartcity.R;
import qunincey.com.smartcity.domain.FragmentInfo;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.view.TopNewsViewPager;

public class FragmentNews extends Fragment {

    private ImageButton imageButton;
    private View view;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private NewsMenu.NewsMenuData newsMenuDataArrayList;
    private List<FragmentNewsMenuDetail> mFragments;
    private TabLayout tabLayout;
    private TopNewsViewPager viewPager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content2,container,false);
        Bundle bundle = getArguments();
        newsMenuDataArrayList = (NewsMenu.NewsMenuData) bundle.getSerializable("news_menu");
        initView();
        return view;
    }

    public void initView(){

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tab_layout);
        mFragments = new ArrayList<>();


        for (int i=0;i<newsMenuDataArrayList.children.size();i++){
            FragmentNewsMenuDetail fragmentInfo=new FragmentNewsMenuDetail();
            Bundle bundle1 =new Bundle();
            /*
            * 放id
            * */
            bundle1.putInt("id",newsMenuDataArrayList.getChildren().get(i).getId());
            bundle1.putSerializable(newsMenuDataArrayList.getChildren().get(i).getId()+"",newsMenuDataArrayList.getChildren().get(i));
            fragmentInfo.setArguments(bundle1);
            System.out.println(newsMenuDataArrayList.getChildren().get(i).toString());
            mFragments.add(fragmentInfo);
            tabLayout.addTab(tabLayout.newTab().setText(newsMenuDataArrayList.children.get(i).title));
        }
        PagerAdapter adapter = new
                ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageButton = view.findViewById(R.id.btn_menu);
        drawerLayout = getActivity().findViewById(R.id.dl_content);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return  mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return newsMenuDataArrayList.children.get(position).title;
        }
    }


}
