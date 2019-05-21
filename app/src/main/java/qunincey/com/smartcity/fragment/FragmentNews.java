package qunincey.com.smartcity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class FragmentNews extends Fragment {

    private ImageButton imageButton;
    private View view;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private NewsMenu.NewsMenuData newsMenuDataArrayList;
    private List<FragmentInfo> mFragments;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content2,container,false);
        Bundle bundle = getArguments();
        newsMenuDataArrayList = (NewsMenu.NewsMenuData) bundle.getSerializable("news_menu");
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tab_layout);
        initFragments();
        PagerAdapter adapter = new
                ViewPagerAdapter(getFragmentManager(),initFragments());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private List<FragmentInfo> initFragments() {
        mFragments = new ArrayList<>(4);

        for (int i=0;i<newsMenuDataArrayList.children.size();i++){
            FragmentInfo fragmentInfo=new FragmentInfo(newsMenuDataArrayList.children.get(i).getTitle(),FragmentNewsMenuDetail.class);
            mFragments.add(fragmentInfo);
        }
        return mFragments;
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


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<FragmentInfo> mFragments;

        public ViewPagerAdapter(FragmentManager fm, List<FragmentInfo> fragments) {
            super(fm);

            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {


            try {
                return (Fragment) mFragments.get(position).getFragment().newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }

            return  null;

        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }
}
